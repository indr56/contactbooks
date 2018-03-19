package com.plivo.dao.impl;

import com.google.inject.internal.util.Preconditions;
import com.plivo.dao.ContactBookDao;
import com.plivo.exception.ContactDaoException;
import com.plivo.model.Contact;
import com.plivo.model.GetResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository("contactBookDao")
public class ContactBookDaoImpl implements ContactBookDao {

    private static String HEROKU_JDBC_DATABASE_URL = "JDBC_DATABASE_URL";
    private static String INSERT_QUERY_FORMAT = "INSERT INTO %s VALUES ( %s )";
    private static String GET_OUTER_QUERY_FORMAT = "SELECT email, name, info from ( %s )subq WHERE rowNumber >= %d AND " +
                                                           "rowNumber <= %d";
    private static String DELETE_QUERY_FORMAT = "DELETE FROM %s WHERE EMAIL = '%s'";
    private static String UPDATE_QUERY_FORMAT = "UPDATE %s SET info = '%s' WHERE %s";

    private String tableName;
    private int paginationFactor;
	private Statement statement;

    public ContactBookDaoImpl(@Value("contacts") String tableName, 
	                          @Value("10") int paginationFactor) {
		super();
        this.tableName = tableName;
        this.paginationFactor = paginationFactor;
    }

    public boolean create(String email, String name, String info) throws ContactDaoException {
        Contact contact = new Contact(email, name, info);
        String insertQuery = String.format(INSERT_QUERY_FORMAT, tableName, contact.toQuery());
        int rowNumber = runUpdateQuery(insertQuery);
        return rowNumber == 1;
    }

    public GetResponse get(String email, String name, int pageNumber) throws ContactDaoException {
        Preconditions.checkArgument(pageNumber > 0, "PageNumber cannot be 0 or less");
        String selectInnerQuery = "SELECT * , row_number() over() as rowNumber FROM " + tableName + " WHERE ";
		
        if(StringUtils.isNotBlank(email) && StringUtils.isNotBlank(name)) {
			selectInnerQuery = selectInnerQuery.concat(String.format("email = '%s' AND name = '%s' ", email, name));
        } else if(StringUtils.isNotBlank(email)) {
			selectInnerQuery = selectInnerQuery.concat(String.format("email = '%s' ", email));
        } else if(StringUtils.isNotBlank(name)) {
			selectInnerQuery = selectInnerQuery.concat(String.format("name = '%s' ", name));
        } else {
			throw new IllegalArgumentException("Either email or name should be present for search query");
        }
		
        int fromPageNumber = (pageNumber - 1) * paginationFactor + 1;
        int toPageNumber = pageNumber * paginationFactor + 1;
        String selectOuterQuery = String.format(GET_OUTER_QUERY_FORMAT, selectInnerQuery, fromPageNumber, toPageNumber);
        List<Contact> contactList = runExecuteQuery(selectOuterQuery);

        boolean nextPageToken = contactList.size() > paginationFactor;
        
		if(nextPageToken) {
			contactList = contactList.subList(0, paginationFactor);
		}
        return new GetResponse(contactList, nextPageToken);
    }

    public boolean delete(String email) throws ContactDaoException {
        Preconditions.checkNotNull(email);
        String deleteQuery = String.format(DELETE_QUERY_FORMAT, tableName, email);
        int rowNumber = runUpdateQuery(deleteQuery);
        return rowNumber == 1;
    }

    public boolean update(String email, String name, String info) throws ContactDaoException {
        String updateQueryConditions = "";
        if(StringUtils.isNotBlank(email) && StringUtils.isNotBlank(name)) {
            updateQueryConditions = updateQueryConditions.concat(String.format("email = '%s' AND name = '%s' ", email, name));
        } else if(StringUtils.isNotBlank(email)) {
            updateQueryConditions = updateQueryConditions.concat(String.format("email = '%s' ", email));
        } else if(StringUtils.isNotBlank(name)) {
            updateQueryConditions = updateQueryConditions.concat(String.format("name = '%s' ", name));
        } else {
            throw new IllegalArgumentException("Either email or name should be present for search query");
        }

        String updateQuery = String.format(UPDATE_QUERY_FORMAT, tableName, info, updateQueryConditions);
        int rowNumber = runUpdateQuery(updateQuery);
        return rowNumber == 1;
    }

    private int runUpdateQuery(String query) throws ContactDaoException {
        try (
                Connection connection = createConnection();
                Statement statement = connection.createStatement();
        ) {
            return statement.executeUpdate(query);
        } catch (SQLException exception) {
            throw new ContactDaoException(exception.getMessage());
        }
    }

    private List<Contact> runExecuteQuery(String query) throws ContactDaoException {

        try(
                Connection connection = createConnection();
				Statement statement = connection.createStatement();
         ) {
			ResultSet cursor = statement.executeQuery(query);
			List<Contact> contactList = new ArrayList<Contact>();
            while (cursor.next()) {
                Contact contact = new Contact(cursor.getString("email"),
                                              cursor.getString("name"),
                                              cursor.getString("info"));
                contactList.add(contact);	

            }
			return contactList;
        } catch (SQLException exception) {
            throw new ContactDaoException(exception.getMessage());
        }
    }

    //@VisibleForTesting
    protected Connection createConnection() throws SQLException {
        String dbUrl = System.getenv(HEROKU_JDBC_DATABASE_URL);
        return DriverManager.getConnection(dbUrl);
    }
}

package com.plivo.dao.impl;

import com.plivo.dao.ContactBookDao;
import com.plivo.exception.ContactDaoException;
import com.plivo.model.Contact;
import com.plivo.model.GetResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class ContactBookDaoImplTest {

    private static String TEST_EMAIL = "abc@gmail.com";
    private static String TEST_EMAIL2 = "def@gmail.com";
    private static String TEST_EMAIL3 = "xyz@gmail.com";
    private static String TEST_NAME = "John Doe";
    private static String TEST_INFO = "Phone No = 8008008008";


    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    private ContactBookDao contactBookDao;

    @Before
    public void setup() throws Exception {
        this.connection = Mockito.mock(Connection.class);
        this.statement = Mockito.mock(Statement.class);
        this.resultSet = Mockito.mock(ResultSet.class);
        ContactBookDaoImpl contactBookDaoImpl = Mockito.spy(new ContactBookDaoImpl( "testTable", 2));
        this.contactBookDao = contactBookDaoImpl;
        Mockito.doReturn(connection).when(contactBookDaoImpl).createConnection();
        Mockito.doReturn(statement).when(connection).createStatement();
    }

    @Test(expected = ContactDaoException.class)
    public void test_createThrowsExceptionWhenQueryFails() throws Exception {
        Mockito.doThrow(new SQLException("")).when(statement).executeUpdate(Mockito.anyString());
        contactBookDao.create(TEST_EMAIL, TEST_NAME, TEST_INFO);
    }

    @Test
    public void test_create() throws Exception {
        Mockito.doReturn(1).when(statement).executeUpdate(Mockito.anyString());
        contactBookDao.create(TEST_EMAIL, TEST_NAME, TEST_INFO);
        Mockito.verify(statement).executeUpdate(Mockito.anyString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_getThrowsExceptionWhenPageNumberisZero() throws Exception {
        contactBookDao.get(TEST_EMAIL, TEST_NAME, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_getThrowsExceptionWhenBothEmailAndNameIsNull() throws Exception {
        contactBookDao.get(null, null, 10);
    }

    @Test
    public void test_getWithOnlyEmailReturnsOneRow() throws Exception {
        Mockito.doReturn(resultSet).when(statement).executeQuery(Mockito.anyString());
        Mockito.doReturn(true).doReturn(false).when(resultSet).next();
        Mockito.doReturn(1).doReturn(0).when(resultSet).getRow();
        Mockito.doReturn(TEST_EMAIL).when(resultSet).getString("email");
        Mockito.doReturn(TEST_NAME).when(resultSet).getString("name");
        Mockito.doReturn(TEST_INFO).when(resultSet).getString("info");
        GetResponse getResponse = contactBookDao.get(TEST_EMAIL, null, 1);
        List<Contact> contactList = getResponse.getContactList();
        Mockito.verify(statement).executeQuery(Mockito.anyString());
        Assert.assertTrue(contactList.size() == 1);
        Assert.assertEquals(contactList.get(0).getEmail(), TEST_EMAIL);
        Assert.assertEquals(contactList.get(0).getName(), TEST_NAME);
        Assert.assertEquals(contactList.get(0).getInfo(), TEST_INFO);
        Assert.assertFalse(getResponse.isNextPageToken());

    }

    @Test
    public void test_getWithOnlyNameReturnsMultipleRows() throws Exception {
        Mockito.doReturn(resultSet).when(statement).executeQuery(Mockito.anyString());
        Mockito.doReturn(true).doReturn(true).doReturn(false).when(resultSet).next();
        Mockito.doReturn(1).doReturn(2).doReturn(0).when(resultSet).getRow();
        Mockito.doReturn(TEST_EMAIL).doReturn(TEST_EMAIL2).when(resultSet).getString("email");
        Mockito.doReturn(TEST_NAME).doReturn(TEST_NAME).when(resultSet).getString("name");
        Mockito.doReturn(TEST_INFO).doReturn(TEST_INFO).when(resultSet).getString("info");
        GetResponse getResponse = contactBookDao.get(null, TEST_NAME, 1);
        List<Contact> contactList = getResponse.getContactList();
        Mockito.verify(statement).executeQuery(Mockito.anyString());
        Assert.assertTrue(contactList.size() == 2);
        Assert.assertEquals(contactList.get(0).getEmail(), TEST_EMAIL);
        Assert.assertEquals(contactList.get(0).getName(), TEST_NAME);
        Assert.assertEquals(contactList.get(0).getInfo(), TEST_INFO);
        Assert.assertEquals(contactList.get(1).getEmail(), TEST_EMAIL2);
        Assert.assertFalse(getResponse.isNextPageToken());
    }

    @Test
    public void test_getWithBothEmailAndNameReturnsOneRow() throws Exception {
        Mockito.doReturn(resultSet).when(statement).executeQuery(Mockito.anyString());
        Mockito.doReturn(true).doReturn(false).when(resultSet).next();
        Mockito.doReturn(1).doReturn(0).when(resultSet).getRow();
        Mockito.doReturn(TEST_EMAIL).when(resultSet).getString("email");
        Mockito.doReturn(TEST_NAME).when(resultSet).getString("name");
        Mockito.doReturn(TEST_INFO).when(resultSet).getString("info");
        GetResponse getResponse = contactBookDao.get(TEST_EMAIL, TEST_NAME, 1);
        List<Contact> contactList = getResponse.getContactList();
        Mockito.verify(statement).executeQuery(Mockito.anyString());
        Assert.assertTrue(contactList.size() == 1);
        Assert.assertEquals(contactList.get(0).getEmail(), TEST_EMAIL);
        Assert.assertEquals(contactList.get(0).getName(), TEST_NAME);
        Assert.assertEquals(contactList.get(0).getInfo(), TEST_INFO);
        Assert.assertFalse(getResponse.isNextPageToken());
    }

    @Test
    public void test_getWithMoreRecordsThanPaginationFactor() throws Exception {
        Mockito.doReturn(resultSet).when(statement).executeQuery(Mockito.anyString());
        Mockito.doReturn(true).doReturn(true).doReturn(true).doReturn(false).when(resultSet).next();
        Mockito.doReturn(1).doReturn(2).doReturn(3).doReturn(3).when(resultSet).getRow();
        Mockito.doReturn(TEST_EMAIL).doReturn(TEST_EMAIL2).doReturn(TEST_EMAIL3).when(resultSet).getString("email");
        Mockito.doReturn(TEST_NAME).doReturn(TEST_NAME).doReturn(TEST_NAME).when(resultSet).getString("name");
        Mockito.doReturn(TEST_INFO).doReturn(TEST_INFO).doReturn(TEST_INFO).when(resultSet).getString("info");
        GetResponse getResponse = contactBookDao.get(null, TEST_NAME, 1);
        List<Contact> contactList = getResponse.getContactList();
        Mockito.verify(statement).executeQuery(Mockito.anyString());
        Assert.assertTrue(contactList.size() == 2);
        Assert.assertEquals(contactList.get(0).getEmail(), TEST_EMAIL);
        Assert.assertEquals(contactList.get(0).getName(), TEST_NAME);
        Assert.assertEquals(contactList.get(0).getInfo(), TEST_INFO);
        Assert.assertEquals(contactList.get(1).getEmail(), TEST_EMAIL2);
        Assert.assertTrue(getResponse.isNextPageToken());
    }

    @Test(expected = ContactDaoException.class)
    public void test_deleteThrowsExceptionWhenQueryFails() throws Exception {
        Mockito.doThrow(new SQLException("")).when(statement).executeUpdate(Mockito.anyString());
        contactBookDao.delete(TEST_EMAIL);
    }

    @Test(expected = NullPointerException.class)
    public void test_deleteThrowsNPEWhenEmailIsNull() throws Exception {
        contactBookDao.delete(null);
    }

    @Test
    public void delete() throws Exception {
        Mockito.doReturn(1).when(statement).executeUpdate(Mockito.anyString());
        contactBookDao.delete(TEST_EMAIL);
        Mockito.verify(statement).executeUpdate(Mockito.anyString());
    }
}

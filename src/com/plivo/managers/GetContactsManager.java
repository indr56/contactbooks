package com.plivo.managers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.plivo.dao.ContactBookDao;
import com.plivo.exception.ContactDaoException;
import com.plivo.model.GetResponse;

@Component
public class GetContactsManager {

private ContactBookDao contactBookDao;
	
	@Autowired
	public GetContactsManager(ContactBookDao contactBookDao) {
		super();
		this.contactBookDao = contactBookDao;
	}

	public GetResponse getContacts(String name, String email, int pageNumber) throws ContactDaoException{
		return contactBookDao.get(email, name, pageNumber);
	}
}

package com.plivo.managers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.plivo.constants.AppConstants;
import com.plivo.dao.ContactBookDao;
import com.plivo.exception.ContactDaoException;

@Component
public class AddContactsManager {
	
	private ContactBookDao contactBookDao;
	
	@Autowired
	public AddContactsManager(ContactBookDao contactBookDao) {
		super();
		this.contactBookDao = contactBookDao;
	}


	public String addContact(String name, String eMail, String info) throws ContactDaoException{
		
		if(contactBookDao.create(eMail, name, info)){
			return AppConstants.ADD_SUCCESS_MESSAGE;
		}	
		else{
			return AppConstants.ADD_FAIL_MESSAGE;
		}
	}
}

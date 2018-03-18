package com.plivo.managers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.plivo.constants.AppConstants;
import com.plivo.dao.ContactBookDao;
import com.plivo.exception.ContactDaoException;

@Component
public class EditContactsManager {

private ContactBookDao contactBookDao;
	
	@Autowired
	public EditContactsManager(ContactBookDao contactBookDao) {
		super();
		this.contactBookDao = contactBookDao;
	}

	public String editContact(String name, String email, String info) throws ContactDaoException{
		if(contactBookDao.update(email, name, info)){
			return AppConstants.EDIT_SUCCESS_MESSAGE;
		}
		else return AppConstants.EDIT_FAIL_MESSAGE;
	}
}

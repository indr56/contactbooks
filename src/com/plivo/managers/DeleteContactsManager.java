package com.plivo.managers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.plivo.constants.AppConstants;
import com.plivo.dao.ContactBookDao;
import com.plivo.exception.ContactDaoException;

@Component
public class DeleteContactsManager {


	private ContactBookDao contactBookDao;
	
	@Autowired
	public DeleteContactsManager(ContactBookDao contactBookDao) {
		super();
		this.contactBookDao = contactBookDao;
	}

	public String deleteContact(String email) throws ContactDaoException{
		if(contactBookDao.delete(email)){
			return AppConstants.DELETE_SUCCESS_MESSAGE;
		}
		else return AppConstants.DELETE_FAIL_MESSAGE;
	}
}

package com.plivo.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import com.plivo.constants.AppConstants;
import com.plivo.exception.ContactDaoException;
import com.plivo.managers.AddContactsManager;
import com.plivo.utilities.BasicAuthenticator;
import com.plivo.utilities.CredentialsValidator;

import org.junit.Assert;

@RunWith(MockitoJUnitRunner.class)
public class AddContactsControllerTest {
	
	@InjectMocks
	private AddContactsController addContactsController;
	
	@Mock
	private AddContactsManager addContactsManager;
	
	@Mock
	private CredentialsValidator credentialsValidator;
	
	@Mock
	private BasicAuthenticator basicAuthenticator;
	
	@Test
	public void testAddContactSuccess() throws ContactDaoException{
		Mockito.when(credentialsValidator.validateName(Mockito.anyString())).thenReturn(true);
		Mockito.when(credentialsValidator.validateEmail(Mockito.anyString())).thenReturn(true);
		Mockito.when(basicAuthenticator.authenticate(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
		Mockito.when(addContactsManager.addContact(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(AppConstants.ADD_SUCCESS_MESSAGE);
		Assert.assertEquals(addContactsController.addContact("Plivo", "test@plivo.com", "fax=1,phone=2", "admin", "admin").getBody(), AppConstants.ADD_SUCCESS_MESSAGE);
		Assert.assertEquals(addContactsController.addContact("Plivo", "test@plivo.com", "fax=1,phone=2", "admin", "admin").getStatusCode(), HttpStatus.OK);

	}
	
	@Test
	public void testAddContactCredentialFailure(){
		Mockito.when(credentialsValidator.validateName(Mockito.anyString())).thenReturn(false);
		Mockito.when(credentialsValidator.validateEmail(Mockito.anyString())).thenReturn(true);
		Mockito.when(basicAuthenticator.authenticate(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
		Assert.assertEquals(addContactsController.addContact("Plivo", "test@plivo.com", "fax=1,phone=2", "admin", "admin").getBody(), AppConstants.BAD_REQUEST_MESSAGE);
		Assert.assertEquals(addContactsController.addContact("Plivo", "test@plivo.com", "fax=1,phone=2", "admin", "admin").getStatusCode(), HttpStatus.BAD_REQUEST);

	}
	
	@Test
	public void testAddContactAuthorizationFailure(){
		Mockito.when(credentialsValidator.validateName(Mockito.anyString())).thenReturn(true);
		Mockito.when(credentialsValidator.validateEmail(Mockito.anyString())).thenReturn(true);
		Mockito.when(basicAuthenticator.authenticate(Mockito.anyString(), Mockito.anyString())).thenReturn(false);
		Assert.assertEquals(addContactsController.addContact("Plivo", "test@plivo.com", "fax=1,phone=2", "admin", "admin").getBody(), AppConstants.UNAUTHORIZED_REQUEST_MESSAGE);
		Assert.assertEquals(addContactsController.addContact("Plivo", "test@plivo.com", "fax=1,phone=2", "admin", "admin").getStatusCode(), HttpStatus.UNAUTHORIZED);

	}
	
	
}

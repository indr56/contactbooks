package com.plivo.controllers;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import com.plivo.constants.AppConstants;
import com.plivo.exception.ContactDaoException;
import com.plivo.managers.GetContactsManager;
import com.plivo.model.GetResponse;
import com.plivo.utilities.BasicAuthenticator;
import com.plivo.utilities.CredentialsValidator;

@RunWith(MockitoJUnitRunner.class)
public class GetContactsControllerTest {

	@InjectMocks
	private GetContactsController getContactsController;
	
	@Mock
	private GetContactsManager getContactsManager;
	
	@Mock
	private CredentialsValidator credentialsValidator;
	
	@Mock
	private BasicAuthenticator basicAuthenticator;
	
	@Test
	public void testGetContactSuccess() throws ContactDaoException{
		Mockito.when(credentialsValidator.validateName(Mockito.anyString())).thenReturn(true);
		Mockito.when(credentialsValidator.validateEmail(Mockito.anyString())).thenReturn(true);
		Mockito.when(basicAuthenticator.authenticate(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
		Mockito.when(getContactsManager.getContacts(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt())).thenReturn(new GetResponse(null,false));
		Assert.assertEquals(getContactsController.getContact("Plivo", "test@plivo.com",1, "admin", "admin").getStatusCode(), HttpStatus.OK);

	}
	
	@Test
	public void testGetContactCredentialFailure(){
		Mockito.when(credentialsValidator.validateName(Mockito.anyString())).thenReturn(false);
		Mockito.when(credentialsValidator.validateEmail(Mockito.anyString())).thenReturn(false);
		Mockito.when(basicAuthenticator.authenticate(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
		Assert.assertEquals(getContactsController.getContact("Plivo", "test@plivo.com",1, "admin", "admin").getBody(), AppConstants.BAD_REQUEST_MESSAGE);
		Assert.assertEquals(getContactsController.getContact("Plivo", "test@plivo.com",1, "admin", "admin").getStatusCode(), HttpStatus.BAD_REQUEST);

	}
	
	@Test
	public void testGetContactAuthorizationFailure(){
		Mockito.when(credentialsValidator.validateName(Mockito.anyString())).thenReturn(true);
		Mockito.when(credentialsValidator.validateEmail(Mockito.anyString())).thenReturn(true);
		Mockito.when(basicAuthenticator.authenticate(Mockito.anyString(), Mockito.anyString())).thenReturn(false);
		Assert.assertEquals(getContactsController.getContact("Plivo", "test@plivo.com",1, "admin", "admin").getBody(), AppConstants.UNAUTHORIZED_REQUEST_MESSAGE);
		Assert.assertEquals(getContactsController.getContact("Plivo", "test@plivo.com",1, "admin", "admin").getStatusCode(), HttpStatus.UNAUTHORIZED);

	}

}

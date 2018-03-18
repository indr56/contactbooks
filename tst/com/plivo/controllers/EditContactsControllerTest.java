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
import com.plivo.managers.EditContactsManager;
import com.plivo.utilities.BasicAuthenticator;
import com.plivo.utilities.CredentialsValidator;

@RunWith(MockitoJUnitRunner.class)
public class EditContactsControllerTest {

	@InjectMocks
	private EditContactsController editContactsController;
	
	@Mock
	private EditContactsManager editContactsManager;
	
	@Mock
	private CredentialsValidator credentialsValidator;
	
	@Mock
	private BasicAuthenticator basicAuthenticator;
	
	@Test
	public void testEditContactSuccess(){
		Mockito.when(credentialsValidator.validateName(Mockito.anyString())).thenReturn(true);
		Mockito.when(credentialsValidator.validateEmail(Mockito.anyString())).thenReturn(true);
		Mockito.when(basicAuthenticator.authenticate(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
		Assert.assertEquals(editContactsController.editContact("Plivo", "test@plivo.com", "fax=1,phone=2", "admin", "admin").getStatusCode(), HttpStatus.OK);

	}
	
	@Test
	public void testEditContactCredentialFailure(){
		Mockito.when(credentialsValidator.validateName(Mockito.anyString())).thenReturn(false);
		Mockito.when(credentialsValidator.validateEmail(Mockito.anyString())).thenReturn(false);
		Mockito.when(basicAuthenticator.authenticate(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
		Assert.assertEquals(editContactsController.editContact("Plivo", "test@plivo.com", "fax=1,phone=2", "admin", "admin").getBody(), AppConstants.BAD_REQUEST_MESSAGE);
		Assert.assertEquals(editContactsController.editContact("Plivo", "test@plivo.com", "fax=1,phone=2", "admin", "admin").getStatusCode(), HttpStatus.BAD_REQUEST);

	}
	
	@Test
	public void testAddContactAuthorizationFailure(){
		Mockito.when(credentialsValidator.validateName(Mockito.anyString())).thenReturn(true);
		Mockito.when(credentialsValidator.validateEmail(Mockito.anyString())).thenReturn(true);
		Mockito.when(basicAuthenticator.authenticate(Mockito.anyString(), Mockito.anyString())).thenReturn(false);
		Assert.assertEquals(editContactsController.editContact("Plivo", "test@plivo.com", "fax=1,phone=2", "admin", "admin").getBody(), AppConstants.UNAUTHORIZED_REQUEST_MESSAGE);
		Assert.assertEquals(editContactsController.editContact("Plivo", "test@plivo.com", "fax=1,phone=2", "admin", "admin").getStatusCode(), HttpStatus.UNAUTHORIZED);

	}
	
}

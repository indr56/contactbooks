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
import com.plivo.managers.AddContactsManager;
import com.plivo.managers.DeleteContactsManager;
import com.plivo.utilities.BasicAuthenticator;
import com.plivo.utilities.CredentialsValidator;

@RunWith(MockitoJUnitRunner.class)
public class DeleteContactsControllerTest {

	@InjectMocks
	private DeleteContactsController deleteContactsController;
	
	@Mock
	private DeleteContactsManager deleteContactsManager;
	
	@Mock
	private CredentialsValidator credentialsValidator;
	
	@Mock
	private BasicAuthenticator basicAuthenticator;
	
	@Test
	public void testDeleteContactSuccess(){
		Mockito.when(credentialsValidator.validateName(Mockito.anyString())).thenReturn(true);
		Mockito.when(credentialsValidator.validateEmail(Mockito.anyString())).thenReturn(true);
		Mockito.when(basicAuthenticator.authenticate(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
		Assert.assertEquals(deleteContactsController.deleteContact( "test@plivo.com", "admin", "admin").getStatusCode(), HttpStatus.OK);

	}
	
	@Test
	public void testDeleteContactCredentialFailure(){
		Mockito.when(credentialsValidator.validateName(Mockito.anyString())).thenReturn(false);
		Mockito.when(credentialsValidator.validateEmail(Mockito.anyString())).thenReturn(false);
		Mockito.when(basicAuthenticator.authenticate(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
		Assert.assertEquals(deleteContactsController.deleteContact("test@plivo.com", "admin", "admin").getBody(), AppConstants.BAD_REQUEST_MESSAGE);
		Assert.assertEquals(deleteContactsController.deleteContact( "test@plivo.com", "admin", "admin").getStatusCode(), HttpStatus.BAD_REQUEST);

	}
	
	@Test
	public void testAddContactAuthorizationFailure(){
		Mockito.when(credentialsValidator.validateName(Mockito.anyString())).thenReturn(true);
		Mockito.when(credentialsValidator.validateEmail(Mockito.anyString())).thenReturn(true);
		Mockito.when(basicAuthenticator.authenticate(Mockito.anyString(), Mockito.anyString())).thenReturn(false);
		Assert.assertEquals(deleteContactsController.deleteContact( "test@plivo.com", "admin", "admin").getBody(), AppConstants.UNAUTHORIZED_REQUEST_MESSAGE);
		Assert.assertEquals(deleteContactsController.deleteContact( "test@plivo.com", "admin", "admin").getStatusCode(), HttpStatus.UNAUTHORIZED);

	}

}

package com.plivo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.plivo.constants.AppConstants;
import com.plivo.exception.ContactDaoException;
import com.plivo.managers.AddContactsManager;
import com.plivo.managers.DeleteContactsManager;
import com.plivo.utilities.BasicAuthenticator;
import com.plivo.utilities.CredentialsValidator;

@RestController
public class DeleteContactsController {

	private DeleteContactsManager deleteContactsManager;
	private CredentialsValidator credentialsValidator;
	private BasicAuthenticator basicAuthenticator;

	@Autowired
	public DeleteContactsController(DeleteContactsManager deleteContactsManager, CredentialsValidator credentialsValidator,
			BasicAuthenticator basicAuthenticator) {
		super();
		this.deleteContactsManager = deleteContactsManager;
		this.credentialsValidator = credentialsValidator;
		this.basicAuthenticator = basicAuthenticator;
	}

	 
	 @RequestMapping(value = "/", method = RequestMethod.DELETE)
	 public ResponseEntity<String> deleteContact(@RequestParam("email") String email
			 ,@RequestHeader("Username") String username, @RequestHeader("Password") String password) {
		 if(!credentialsValidator.validateEmail(email)){
			 return ResponseEntity.badRequest().body(AppConstants.BAD_REQUEST_MESSAGE);
		 }
		 if(!basicAuthenticator.authenticate(username, password)){
			 return new ResponseEntity<String>(AppConstants.UNAUTHORIZED_REQUEST_MESSAGE, HttpStatus.UNAUTHORIZED);		 
		 }
		 try {
			return ResponseEntity.ok(deleteContactsManager.deleteContact(email));
		} catch (ContactDaoException e) {
			return ResponseEntity.ok(AppConstants.DELETE_FAIL_MESSAGE);
		} 
	 }
}

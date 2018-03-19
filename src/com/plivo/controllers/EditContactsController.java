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
import com.plivo.managers.EditContactsManager;
import com.plivo.utilities.BasicAuthenticator;
import com.plivo.utilities.CredentialsValidator;

@RestController
public class EditContactsController {

	private EditContactsManager editContactManager;
	private CredentialsValidator credentialsValidator;
	private BasicAuthenticator basicAuthenticator;

	@Autowired
	public EditContactsController(EditContactsManager editContactManager, CredentialsValidator credentialsValidator,
			BasicAuthenticator basicAuthenticator) {
		super();
		this.editContactManager = editContactManager;
		this.credentialsValidator = credentialsValidator;
		this.basicAuthenticator = basicAuthenticator;
	}

	 
	 @RequestMapping(value = "/", method = RequestMethod.PUT)
	 public ResponseEntity<String> editContact(@RequestParam(value = "name",required=false) String name, @RequestParam(value = "email",required=false) String email, @RequestBody String info
			 ,@RequestHeader("Username") String username, @RequestHeader("Password") String password) {
		 if(!credentialsValidator.validateName(name) && !credentialsValidator.validateEmail(email)){
			 return ResponseEntity.badRequest().body(AppConstants.BAD_REQUEST_MESSAGE);
		 }
		 if(!basicAuthenticator.authenticate(username, password)){
			 return new ResponseEntity<String>(AppConstants.UNAUTHORIZED_REQUEST_MESSAGE, HttpStatus.UNAUTHORIZED);		 
		 }
		 try {
			return ResponseEntity.ok(editContactManager.editContact(name, email, info));
		} catch (ContactDaoException e) {
			return ResponseEntity.ok(AppConstants.EDIT_FAIL_MESSAGE);
		} 
	 }
}

package com.plivo.controllers;

import javax.servlet.http.HttpServletRequest;

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
import com.plivo.utilities.BasicAuthenticator;
import com.plivo.utilities.CredentialsValidator;

@RestController
public class AddContactsController {

	private AddContactsManager addContactsManager;
	private CredentialsValidator credentialsValidator;
	private BasicAuthenticator basicAuthenticator;

	@Autowired
	public AddContactsController(AddContactsManager addContactsManager, CredentialsValidator credentialsValidator,
			BasicAuthenticator basicAuthenticator) {
		super();
		this.addContactsManager = addContactsManager;
		this.credentialsValidator = credentialsValidator;
		this.basicAuthenticator = basicAuthenticator;
	}

	@RequestMapping(value = "/", method = RequestMethod.POST, produces = "text/html")
	public ResponseEntity<String> addContact(@RequestParam("name") String name, @RequestParam("email") String email,
			@RequestBody String info, @RequestHeader("Username") String username,
			@RequestHeader("Password") String password) {
		if (!credentialsValidator.validateName(name) || !credentialsValidator.validateEmail(email)) {
			
			return ResponseEntity.badRequest().body(AppConstants.BAD_REQUEST_MESSAGE);
		}
		if (!basicAuthenticator.authenticate(username, password)) {
			return new ResponseEntity<String>(AppConstants.UNAUTHORIZED_REQUEST_MESSAGE, HttpStatus.UNAUTHORIZED);
		}
		try {
			return ResponseEntity.ok(addContactsManager.addContact(name, email, info));
		} catch (ContactDaoException e) {
			return ResponseEntity.ok(AppConstants.ADD_FAIL_MESSAGE);
		}
	}
}

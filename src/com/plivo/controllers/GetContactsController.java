package com.plivo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.plivo.constants.AppConstants;
import com.plivo.exception.ContactDaoException;
import com.plivo.managers.GetContactsManager;
import com.plivo.model.GetResponse;
import com.plivo.utilities.BasicAuthenticator;
import com.plivo.utilities.CredentialsValidator;

@Controller
public class GetContactsController {

	private GetContactsManager getContactsManager;
	private CredentialsValidator credentialsValidator;
	private BasicAuthenticator basicAuthenticator;

	@Autowired
	public GetContactsController(GetContactsManager getContactsManager, CredentialsValidator credentialsValidator,
			BasicAuthenticator basicAuthenticator) {
		super();
		this.getContactsManager = getContactsManager;
		this.credentialsValidator = credentialsValidator;
		this.basicAuthenticator = basicAuthenticator;
	}

	 
	 @RequestMapping(value = "/", method = RequestMethod.GET)
	 public ResponseEntity<String> getContact(@RequestParam(value = "name",required=false) String name, @RequestParam(value = "email",required=false) String email
			 ,@RequestParam("pageNumber") int pageNumber, @RequestHeader("Username") String username, @RequestHeader("Password") String password) {
		 if(!credentialsValidator.validateName(name) && !credentialsValidator.validateEmail(email)){
			 return ResponseEntity.badRequest().body(AppConstants.BAD_REQUEST_MESSAGE);
		 }
		 if(!basicAuthenticator.authenticate(username, password)){
			 return new ResponseEntity<String>(AppConstants.UNAUTHORIZED_REQUEST_MESSAGE, HttpStatus.UNAUTHORIZED);		 
		 }
		try {
			GetResponse response = getContactsManager.getContacts(name, email, pageNumber);
		
		 if(response.isNextPageToken() && response.getContactList()!=null){
				return ResponseEntity.ok(response.getContactList().toString()+"\n"+AppConstants.NEXT_PAGE_PROMPT);
		 }
		 else {
			 	if(response.getContactList()!=null){
			 		return ResponseEntity.ok(response.getContactList().toString());
			 	}
			 	else {
			 		return ResponseEntity.ok("No Contacts");
			 	}
		 }
		} catch (ContactDaoException e) {
			return ResponseEntity.ok(AppConstants.GET_FAIL_MESSAGE);

		}
	 }
}

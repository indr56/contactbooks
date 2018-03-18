package com.plivo.utilities;

import org.springframework.stereotype.Component;

import com.plivo.constants.AppConstants;

@Component
public class BasicAuthenticator {
	
	static{
		System.setProperty(AppConstants.USERNAME, "admin");
		System.setProperty(AppConstants.PASSWORD, "admin");
	}
	
	public boolean authenticate(String username, String password){
		if(System.getProperty(AppConstants.PASSWORD).equals(password) && System.getProperty(AppConstants.USERNAME).equals(username)){
		return true;
		}
		else return false;
	}
}

package com.plivo.utilities;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class CredentialsValidator {
	
	public boolean validateName(String name){
		if(StringUtils.isEmpty(name)){
			return false;
		}
		else{		
			return true;
		}	
	}
	
	public boolean validateEmail(String eMail){
		if(StringUtils.isEmpty(eMail) || !eMail.matches("(.*)@(.*)(\\.)(.*)")){
			return false;
		}
		else{
			return true;
		}	
	}
}

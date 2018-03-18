package com.plivo.utiltites;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.junit.Assert;

import com.plivo.utilities.CredentialsValidator;

@RunWith(MockitoJUnitRunner.class)
public class CredentialsValidatorTest {

	CredentialsValidator credentialsValidator = new CredentialsValidator();
	
	@Test
	public void validateNameTestSuccess(){
		Assert.assertEquals(credentialsValidator.validateName("Plivo"), true);
	}
	
	@Test
	public void validateNameTestFail(){
		Assert.assertEquals(credentialsValidator.validateName(null), false);
	}
	
	@Test
	public void validateEmailTestSuccess(){
		Assert.assertEquals(credentialsValidator.validateEmail("Plivo@test.com"), true);
	}
	
	@Test
	public void validateEmailTestFail(){
		Assert.assertEquals(credentialsValidator.validateEmail("Plivotest"), false);
	}
	
}

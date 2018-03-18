package com.plivo.utiltites;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.plivo.constants.AppConstants;
import com.plivo.utilities.BasicAuthenticator;


@RunWith(MockitoJUnitRunner.class)
public class BasicAuthenticatorTest {

	BasicAuthenticator basicAuthenticator = new BasicAuthenticator();
	
	@Test
	public void vaildatorTestSuccess(){
		Assert.assertEquals(basicAuthenticator.authenticate(System.getProperty(AppConstants.USERNAME), System.getProperty(AppConstants.PASSWORD)), true);
	}
	
	@Test
	public void vaildatorTestFail(){
		Assert.assertEquals(basicAuthenticator.authenticate("test", System.getProperty(AppConstants.PASSWORD)), false);
	}
}

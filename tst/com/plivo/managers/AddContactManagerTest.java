package com.plivo.managers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.plivo.constants.AppConstants;
import com.plivo.dao.ContactBookDao;
import com.plivo.exception.ContactDaoException;
import com.plivo.model.GetResponse;

import org.junit.Assert;

@RunWith(MockitoJUnitRunner.class)
public class AddContactManagerTest {

	@InjectMocks
	private AddContactsManager addContactsManager;
	
	@InjectMocks
	private DeleteContactsManager deleteContactsManager;
	
	@InjectMocks
	private EditContactsManager editContactsManager;
	
	@InjectMocks
	private GetContactsManager getContactsManager;
	
	@Mock
	private ContactBookDao contactBookDao;

	@Test
	public void addContactTest() throws ContactDaoException{
		Mockito.when(contactBookDao.create(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(true);
		Assert.assertEquals(addContactsManager.addContact(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()), AppConstants.ADD_SUCCESS_MESSAGE);
	}
	
	@Test
	public void deleteContactTest() throws ContactDaoException{
		Mockito.when(contactBookDao.delete(Mockito.anyString())).thenReturn(true);
		Assert.assertEquals(deleteContactsManager.deleteContact(Mockito.anyString()), AppConstants.DELETE_SUCCESS_MESSAGE);
	}
	
	@Test
	public void editContactTest() throws ContactDaoException{
		Mockito.when(contactBookDao.update(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(true);
		Assert.assertEquals(editContactsManager.editContact(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()), AppConstants.EDIT_SUCCESS_MESSAGE);
	}
	
	@Test
	public void getContactTest() throws ContactDaoException{
		Mockito.when(contactBookDao.get(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt())).thenReturn(new GetResponse(null, false));
		Assert.assertEquals(getContactsManager.getContacts(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt()).isNextPageToken(), new GetResponse(null, false).isNextPageToken());
	}
}

package com.plivo.model;

import java.util.List;

public class GetResponse {

    List<Contact> contactList;

    boolean nextPageToken;


    public GetResponse(List<Contact> contactList, boolean nextPageToken) {
        this.contactList = contactList;
        this.nextPageToken = nextPageToken;
    }

    public List<Contact> getContactList() {
        return contactList;
    }

    public boolean isNextPageToken() {
        return nextPageToken;
    }

}

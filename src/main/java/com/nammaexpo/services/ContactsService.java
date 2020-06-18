package com.nammaexpo.services;

import com.nammaexpo.payload.request.ContactsDTO;
import com.nammaexpo.payload.response.MessageResponse;
import com.nammaexpo.persistance.model.Contacts;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

public interface ContactsService {

    ResponseEntity<MessageResponse> addNewContact(ContactsDTO contact, String createdBy);
    ResponseEntity<MessageResponse> deleteContact(Contacts contacts);
    ResponseEntity<MessageResponse> updateContact(Contacts contact, String updatedBy);
}

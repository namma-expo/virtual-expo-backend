package com.nammaexpo.services;

import com.nammaexpo.payload.request.ContactsDTO;
import com.nammaexpo.payload.response.MessageResponse;
import com.nammaexpo.persistance.model.ExhibitionContactEntity;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import java.util.List;

public interface ContactsService {

    ResponseEntity<MessageResponse> addNewContact(ContactsDTO contact, String createdBy);

    ResponseEntity<MessageResponse> updateContact(ExhibitionContactEntity exhibitionContactEntity, @Valid ContactsDTO contact, String updatedBy);

    ResponseEntity<List<ContactsDTO>> getAllContacts();

    ResponseEntity<ContactsDTO> getContact(String email);
}

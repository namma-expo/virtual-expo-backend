package com.nammaexpo.services.impl;

import com.nammaexpo.models.enums.MessageCode;
import com.nammaexpo.payload.request.ContactsDTO;
import com.nammaexpo.payload.response.MessageResponse;
import com.nammaexpo.persistance.dao.ContactsRepo;
import com.nammaexpo.persistance.model.Contacts;
import com.nammaexpo.services.ContactsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ContactsServiceImpl implements ContactsService {

    @Autowired
    ContactsRepo contactsRepo;

    @Override
    public ResponseEntity<MessageResponse> addNewContact(ContactsDTO contact, String createdBy) {

        contactsRepo.save(Contacts.builder()
                .userEmail(contact.getUserEmail())
                .userName(contact.getUserName())
                .occupation(contact.getOccupation())
                .companyName(contact.getCompanyName())
                .address1(contact.getAddress1())
                .address2(contact.getAddress2())
                .country(contact.getCountry())
                .state(contact.getState())
                .city(contact.getCity())
                .phone1(contact.getPhone1())
                .phone2(contact.getPhone2())
                .clientReq(contact.getClientReq())
                .notes(contact.getNotes())
                .createdBy(createdBy).build());

        log.debug("Added new contact");

        return ResponseEntity.ok(MessageResponse.builder()
                .messageCode(MessageCode.CREATE_CONTACT_SUCCESS)
                .message("Added contact to DB").build());
    }

    @Override
    public ResponseEntity<MessageResponse> deleteContact(Contacts contacts) {

            contactsRepo.delete(contacts);

            log.debug("Contact deleted");
            return new ResponseEntity<>(MessageResponse
                    .builder()
                    .messageCode(MessageCode.CONTACT_DELETE_SUCCESS)
                    .message("Contact deleted successfully").build(), HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<MessageResponse> updateContact(Contacts contact, String updatedBy) {

        contactsRepo.save(Contacts.builder()
                .userEmail(contact.getUserEmail())
                .userName(contact.getUserName())
                .occupation(contact.getOccupation())
                .companyName(contact.getCompanyName())
                .address1(contact.getAddress1())
                .address2(contact.getAddress2())
                .country(contact.getCountry())
                .state(contact.getState())
                .city(contact.getCity())
                .phone1(contact.getPhone1())
                .phone2(contact.getPhone2())
                .clientReq(contact.getClientReq())
                .notes(contact.getNotes())
                .createdBy(contact.getCreatedBy())
                .modifiedBy(updatedBy).build());

        return new ResponseEntity<>(MessageResponse
                .builder()
                .messageCode(MessageCode.UPDATE_CONTACT_SUCCESS)
                .message("Successfully updated contact")
                .build(), HttpStatus.ACCEPTED);
    }
}

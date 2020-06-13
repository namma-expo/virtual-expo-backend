package com.nammaexpo.controllers;

import com.nammaexpo.expection.ErrorCode;
import com.nammaexpo.expection.ExpoException;
import com.nammaexpo.models.enums.MessageCode;
import com.nammaexpo.payload.request.ContactsDTO;
import com.nammaexpo.payload.response.MessageResponse;
import com.nammaexpo.persistance.dao.ContactsRepo;
import com.nammaexpo.persistance.model.Contacts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.Optional;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class ContactsController {

    @Autowired
    ContactsRepo contactsRepo;

    @PostMapping("/newContact")
    @PreAuthorize("hasAuthority('EXHIBITOR')")
    public ResponseEntity<MessageResponse> addContact(@RequestBody @Valid ContactsDTO contact) {

        Optional<Contacts> optionalContacts = contactsRepo.findByUserEmail(contact.getUserEmail());

        if (optionalContacts.isPresent()) {
            throw ExpoException.error(ErrorCode.EMAIL_IN_USE);
        }

        String createdBy = SecurityContextHolder.getContext().getAuthentication().getName();

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

        return ResponseEntity.ok(MessageResponse.builder()
                .messageCode(MessageCode.CREATE_CONTACT_SUCCESS)
                .message("Added contact to DB").build());
    }


}

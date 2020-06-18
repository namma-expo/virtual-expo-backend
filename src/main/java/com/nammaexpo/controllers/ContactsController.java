package com.nammaexpo.controllers;

import com.nammaexpo.expection.ErrorCode;
import com.nammaexpo.expection.ExpoException;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class ContactsController {

    @Autowired
    ContactsService contactsService;

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
        if (createdBy != null) {
            return contactsService.addNewContact(contact, createdBy);
        }

        return new ResponseEntity<>(MessageResponse
                .builder()
                .messageCode(MessageCode.CONTACT_REGISTRATION_FAILED)
                .message("Couldn't add new user")
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/allContacts")
    @PreAuthorize("hasAuthority('EXHIBITOR')")
    public ResponseEntity<Object> getAllContacts() {

        List<Contacts> contactsList = contactsRepo.findAll();
        if (contactsList.size() > 0)
            return new ResponseEntity<>(contactsList, HttpStatus.OK);

        return new ResponseEntity<>(MessageResponse
                .builder()
                .messageCode(MessageCode.USER_NOT_FOUND)
                .message("Contacts list is empty"), HttpStatus.NO_CONTENT);
    }

    @GetMapping("/getContact")
    @PreAuthorize("hasAuthority('EXHIBITOR')")
    public ResponseEntity<Object> getContact(@RequestParam String emailId) {

        Optional<Contacts> contact = contactsRepo.findByUserEmail(emailId);
        if (contact.isPresent()) {
            return new ResponseEntity<>(contact, HttpStatus.OK);
        }

        return new ResponseEntity<>(MessageResponse
                .builder()
                .messageCode(MessageCode.USER_NOT_FOUND)
                .message("Email id is not registered")
                .build(), HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/deleteContact")
    @PreAuthorize("hasAuthority('EXHIBITOR')")
    public ResponseEntity<MessageResponse> deleteContact(@RequestParam String emailId) {

        Optional<Contacts> contacts = contactsRepo.findByUserEmail(emailId);
        if (contacts.isPresent()) {
           return contactsService.deleteContact(contacts.get());
        }

        return new ResponseEntity<>(MessageResponse
                .builder()
                .messageCode(MessageCode.USER_NOT_FOUND)
                .message("Email id is not found")
                .build(), HttpStatus.NOT_FOUND);
    }

    @PutMapping("/updateContact")
    public ResponseEntity<MessageResponse> updateContact(@RequestBody @Valid Contacts contacts, @RequestParam String emailId) {

        Optional<Contacts> optionalContacts = contactsRepo.findByUserEmail(emailId);
        String updatedBy = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!optionalContacts.isPresent()) {
            throw ExpoException.error(ErrorCode.EMAIL_NOT_FOUND);
        }
        if (optionalContacts.isPresent() && updatedBy != null) {
            return contactsService.updateContact(contacts, updatedBy);
        }

        throw ExpoException.error(ErrorCode.UPDATE_CONTACT_FAILED);
    }
}

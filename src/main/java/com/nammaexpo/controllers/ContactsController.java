package com.nammaexpo.controllers;

import com.nammaexpo.expection.ErrorCode;
import com.nammaexpo.expection.ExpoException;
import com.nammaexpo.models.enums.MessageCode;
import com.nammaexpo.payload.request.ContactsDTO;
import com.nammaexpo.payload.response.MessageResponse;
import com.nammaexpo.persistance.dao.ExhibitionContactRepository;
import com.nammaexpo.persistance.model.ExhibitionContactEntity;
import com.nammaexpo.services.ContactsService;
import io.swagger.annotations.*;
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
@Api(value = "contacts", description = "operations pertaining to vistor contact details")
public class ContactsController {

    @Autowired
    ContactsService contactsService;

    @Autowired
    ExhibitionContactRepository contactsRepo;

    @ApiOperation(value = "Add new contact details",
            notes = "Exhibitor can collect visitors contact details",
            response = MessageResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = MessageResponse.class,
                    message = "CREATE_CONTACT_SUCCESS"),
            @ApiResponse(code = 400, response = ErrorCode.class,
                    message = "EMAIL_IN_USE"),
            @ApiResponse(code = 500, response = MessageResponse.class,
                    message = "CONTACT_REGISTRATION_FAILED")
    })
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true,
            allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @PostMapping("/newContact")
    @PreAuthorize("hasAuthority('EXHIBITOR')")
    public ResponseEntity<MessageResponse> addContact(@RequestBody @Valid ContactsDTO contact) {

        Optional<ExhibitionContactEntity> optionalContacts = contactsRepo.findByEmail(contact.getEmail());

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

    @ApiOperation(value = "Get all contacts details",
            notes = "Returns all the contacts that has access for this exhibitor",
            response = ContactsDTO.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = ContactsDTO.class, responseContainer = "List",
                    message = "List of all contacts"),
            @ApiResponse(code = 204, response = ErrorCode.class,
                    message = "CONTACTS_NOT_FOUND")
    })
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true,
            allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @GetMapping("/allContacts")
    @PreAuthorize("hasAuthority('EXHIBITOR')")
    public ResponseEntity<List<ContactsDTO>> getAllContacts() {
        return contactsService.getAllContacts();
    }

    @ApiOperation(value = "Get single contact details",
            notes = "Returns the contact details of the visitor based on his email id",
            response = ContactsDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = ContactsDTO.class,
                    message = "Visitor contact details"),
            @ApiResponse(code = 204, response = ErrorCode.class,
                    message = "CONTACTS_NOT_FOUND")
    })
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true,
            allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @GetMapping("/getContact")
    @PreAuthorize("hasAuthority('EXHIBITOR')")
    public ResponseEntity<ContactsDTO> getContact(@RequestHeader String email) {
        return contactsService.getContact(email);
    }

    @ApiOperation(value = "Delete single contact details",
            notes = "Deletes the contact details of the visitor based on his email id",
            response = MessageResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = ContactsDTO.class,
                    message = "CONTACT_DELETE_SUCCESS"),
            @ApiResponse(code = 204, response = ErrorCode.class,
                    message = "CONTACTS_NOT_FOUND")
    })
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true,
            allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @DeleteMapping("/deleteContact")
    @PreAuthorize("hasAuthority('EXHIBITOR')")
    public ResponseEntity<MessageResponse> deleteContact(@RequestHeader String email) {

        Optional<ExhibitionContactEntity> contact = contactsRepo.findByEmail(email);
        if (contact.isPresent()) {
            contactsRepo.delete(contact.get());
            log.debug("Contact deleted");
            return new ResponseEntity<>(MessageResponse
                    .builder()
                    .messageCode(MessageCode.CONTACT_DELETE_SUCCESS)
                    .message("Contact deleted successfully").build(), HttpStatus.ACCEPTED);
        }
        throw ExpoException.error(ErrorCode.CONTACTS_NOT_FOUND);
    }

    @ApiOperation(value = "Update contact details",
            notes = "Update the contact details of the visitor based on his email id",
            response = MessageResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, response = MessageResponse.class,
                    message = "UPDATE_CONTACT_SUCCESS"),
            @ApiResponse(code = 400, response = ErrorCode.class,
                    message = "EMAIL_NOT_FOUND"),
            @ApiResponse(code = 500, response = MessageResponse.class,
                    message = "UPDATE_CONTACT_FAILED")
    })
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true,
            allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @PutMapping("/updateContact")
    @PreAuthorize("hasAuthority('EXHIBITOR')")
    public ResponseEntity<MessageResponse> updateContact(@RequestBody @Valid ContactsDTO contact, @RequestHeader String email) {

        Optional<ExhibitionContactEntity> visitorContact = contactsRepo.findByEmail(email);
        String updatedBy = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!visitorContact.isPresent()) {
            throw ExpoException.error(ErrorCode.EMAIL_NOT_FOUND);
        }
        if (visitorContact.isPresent() && updatedBy != null) {
            return contactsService.updateContact(visitorContact.get(), contact, updatedBy);
        }

        return new ResponseEntity<>(MessageResponse
                .builder()
                .messageCode(MessageCode.UPDATE_CONTACT_FAILED)
                .message("Couldn't update contact")
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

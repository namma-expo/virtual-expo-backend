package com.nammaexpo.controllers;

import com.nammaexpo.models.enums.MessageCode;
import com.nammaexpo.expection.ExpoException;
import com.nammaexpo.payload.response.MessageResponse;
import com.nammaexpo.payload.request.ContactsDTO;
import com.nammaexpo.persistance.dao.ExhibitionContactRepository;
import com.nammaexpo.persistance.model.ExhibitionContactEntity;
import com.nammaexpo.services.ContactsService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
            response = com.nammaexpo.payload.response.MessageResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = MessageResponse.class,
                    message = "CREATE_CONTACT_SUCCESS"),
            @ApiResponse(code = 400, response = MessageResponse.class,
                    message = "EMAIL_IN_USE"),
            @ApiResponse(code = 500, response = MessageResponse.class,
                    message = "CONTACT_REGISTRATION_FAILED")
    })
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true,
            allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @PostMapping("/contact")
    @PreAuthorize("hasAuthority('EXHIBITOR')")
    public ResponseEntity<MessageResponse> addContact(@RequestBody @Valid ContactsDTO contact) {

        Optional<ExhibitionContactEntity> optionalContacts = contactsRepo.findByEmail(contact.getEmail());

        if (optionalContacts.isPresent()) {
            throw ExpoException.error(MessageCode.EMAIL_IN_USE);
        }
        String createdBy = SecurityContextHolder.getContext().getAuthentication().getName();
        if (createdBy != null) {
            return contactsService.addNewContact(contact, createdBy);
        }
        throw ExpoException.error(MessageCode.CONTACT_REGISTRATION_FAILED);
    }

    @ApiOperation(value = "Get all contacts details",
            notes = "Returns all the contacts that has access for this exhibitor",
            response = ContactsDTO.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = ContactsDTO.class, responseContainer = "List",
                    message = "List of all contacts"),
            @ApiResponse(code = 204, response = MessageResponse.class,
                    message = "CONTACTS_NOT_FOUND")
    })
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true,
            allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @GetMapping("/contacts")
    @PreAuthorize("hasAuthority('EXHIBITOR')")
    public ResponseEntity<List<ContactsDTO>> getAllContacts() {
        List<ContactsDTO>  allContacts = contactsService.getAllContacts();
        if (allContacts.size() > 0)
            return new ResponseEntity<>(allContacts, HttpStatus.OK);
        else
            throw ExpoException.error(MessageCode.CONTACTS_NOT_FOUND);
    }

    @ApiOperation(value = "Get single contact details",
            notes = "Returns the contact details of the visitor based on his email id",
            response = ContactsDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = ContactsDTO.class,
                    message = "Visitor contact details"),
            @ApiResponse(code = 204, response = MessageResponse.class,
                    message = "CONTACTS_NOT_FOUND")
    })
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true,
            allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @RequestMapping( path = "/contact", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('EXHIBITOR')")
    public ResponseEntity<ContactsDTO> getContact(@RequestHeader String email) {
        ContactsDTO contactsDTO = contactsService.getContact(email);
        if (contactsDTO != null)
            return new ResponseEntity<>(contactsDTO, HttpStatus.OK);
        else
            throw ExpoException.error(MessageCode.CONTACTS_NOT_FOUND);
    }

    @ApiOperation(value = "Delete single contact details",
            notes = "Deletes the contact details of the visitor based on his email id",
            response = com.nammaexpo.payload.response.MessageResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = MessageResponse.class,
                    message = "CONTACT_DELETE_SUCCESS"),
            @ApiResponse(code = 204, response = MessageResponse.class,
                    message = "CONTACTS_NOT_FOUND")
    })
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true,
            allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @DeleteMapping("/contact")
    @PreAuthorize("hasAuthority('EXHIBITOR')")
    public ResponseEntity<MessageResponse> deleteContact(@RequestHeader String email) {

        Optional<ExhibitionContactEntity> contact = contactsRepo.findByEmail(email);
        if (contact.isPresent()) {
            contactsRepo.delete(contact.get());
            log.debug("Contact deleted");
            return new ResponseEntity<>(MessageResponse.builder()
                    .code(MessageCode.findName(19))
                    .message(MessageCode.findMessage(19)).build(), HttpStatus.ACCEPTED);
        }
        throw ExpoException.error(MessageCode.CONTACTS_NOT_FOUND);
    }

    @ApiOperation(value = "Update contact details",
            notes = "Update the contact details of the visitor based on his email id",
            response = MessageResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, response = MessageResponse.class,
                    message = "UPDATE_CONTACT_SUCCESS"),
            @ApiResponse(code = 400, response = MessageResponse.class,
                    message = "EMAIL_NOT_FOUND"),
            @ApiResponse(code = 500, response = MessageResponse.class,
                    message = "UPDATE_CONTACT_FAILED")
    })
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true,
            allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @PutMapping("/contact")
    @PreAuthorize("hasAuthority('EXHIBITOR')")
    public ResponseEntity<MessageResponse> updateContact(@RequestBody @Valid ContactsDTO contact, @RequestHeader String email) {

        Optional<ExhibitionContactEntity> visitorContact = contactsRepo.findByEmail(email);
        String updatedBy = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!visitorContact.isPresent()) {
            throw ExpoException.error(MessageCode.EMAIL_NOT_FOUND);
        }
        if (visitorContact.isPresent() && updatedBy != null) {
            return contactsService.updateContact(visitorContact.get(), contact, updatedBy);
        }
        throw ExpoException.error(MessageCode.UPDATE_CONTACT_FAILED);
    }
}

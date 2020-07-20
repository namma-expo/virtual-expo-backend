package com.nammaexpo.controllers;

import com.nammaexpo.expection.ExpoException;
import com.nammaexpo.models.enums.MessageCode;
import com.nammaexpo.payload.request.ContactsRequest;
import com.nammaexpo.payload.response.MessageResponse;
import com.nammaexpo.persistance.dao.ExhibitionContactRepository;
import com.nammaexpo.persistance.dao.ExhibitionDetailsRepository;
import com.nammaexpo.persistance.dao.UserRepository;
import com.nammaexpo.persistance.model.ExhibitionContactEntity;
import com.nammaexpo.persistance.model.ExhibitionDetailsEntity;
import com.nammaexpo.persistance.model.UserEntity;
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
import java.util.stream.Collectors;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Api(value = "contacts")
public class ContactsController {

    private UserRepository userRepository;

    private ExhibitionContactRepository contactsRepo;

    private ExhibitionDetailsRepository exhibitionDetails;

    @Autowired
    public ContactsController(UserRepository userRepository, ExhibitionContactRepository contactsRepo,
                              ExhibitionDetailsRepository exhibitionDetails) {
        this.userRepository = userRepository;
        this.contactsRepo = contactsRepo;
        this.exhibitionDetails = exhibitionDetails;
    }

    @ApiOperation(value = "Add new contact details",
            notes = "Exhibitor can collect visitors contact details",
            response = MessageResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = MessageResponse.class,
                    message = "CREATE_CONTACT_SUCCESS"),
            @ApiResponse(code = 400, response = MessageResponse.class,
                    message = "EMAIL_IN_USE"),
            @ApiResponse(code = 500, response = MessageResponse.class,
                    message = "CONTACT_REGISTRATION_FAILED")
    })
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true,
            paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @PostMapping("/contacts")
    @PreAuthorize("hasAuthority('EXHIBITOR')")
    public ResponseEntity<MessageResponse> addContact(@RequestBody @Valid ContactsRequest contact) {

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity = userRepository.findByEmail(userName)
                .orElseThrow(() -> ExpoException.error(
                        MessageCode.ACCESS_DENIED)
                );

        ExhibitionDetailsEntity exhibitionDetail = exhibitionDetails.findByExhibitorId(userEntity.getId())
                .orElseThrow(() ->
                        ExpoException.error(MessageCode.TRANSACTION_NOT_FOUND)
                );

        Optional<ExhibitionContactEntity> optionalContacts = contactsRepo.findByEmailAndExhibitionDetails(
                contact.getEmail(), exhibitionDetail);

        if (optionalContacts.isPresent()) {
            throw ExpoException.error(MessageCode.EMAIL_IN_USE);
        }

        contactsRepo.save(ExhibitionContactEntity.builder()
                .email(contact.getEmail())
                .name(contact.getName())
                .occupation(contact.getOccupation())
                .company(contact.getCompany())
                .phone1(contact.getPhone1())
                .phone2(contact.getPhone2())
                .notes(contact.getNotes())
                .exhibitionDetails(exhibitionDetail)
                .createdBy(userEntity.getId()).build());

        return ResponseEntity.ok(MessageResponse.builder()
                .messageCode(MessageCode.CREATE_CONTACT_SUCCESS)
                .build());
    }

    @ApiOperation(value = "Get all contacts details",
            notes = "Returns all the contacts that has access for this exhibitor",
            response = ContactsRequest.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = ContactsRequest.class, responseContainer = "List",
                    message = "List of all contacts"),
            @ApiResponse(code = 204, response = MessageResponse.class,
                    message = "CONTACTS_NOT_FOUND")
    })
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true,
            paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @GetMapping("/contacts/all")
    @PreAuthorize("hasAuthority('EXHIBITOR')")
    public List<ContactsRequest> getAllContacts() {

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity = userRepository.findByEmail(userName)
                .orElseThrow(() -> ExpoException.error(
                        MessageCode.ACCESS_DENIED)
                );

        ExhibitionDetailsEntity exhibitionDetail = exhibitionDetails.findByExhibitorId(userEntity.getId())
                .orElseThrow(() ->
                        ExpoException.error(MessageCode.TRANSACTION_NOT_FOUND)
                );

        List<ExhibitionContactEntity> contactsList = contactsRepo.findAllByExhibitionDetails(exhibitionDetail);

        return contactsList.stream()
                .map(contacts -> ContactsRequest.builder()
                        .email(contacts.getEmail())
                        .name(contacts.getName())
                        .company(contacts.getCompany())
                        .notes(contacts.getNotes())
                        .occupation(contacts.getOccupation())
                        .phone1(contacts.getPhone1())
                        .phone2(contacts.getPhone2())
                        .build())
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "Get single contact details",
            notes = "Returns the contact details of the visitor based on his email id",
            response = ContactsRequest.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = ContactsRequest.class,
                    message = "Visitor contact details"),
            @ApiResponse(code = 204, response = MessageResponse.class,
                    message = "CONTACTS_NOT_FOUND")
    })
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true,
            paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @GetMapping(path = "/contacts")
    @PreAuthorize("hasAuthority('EXHIBITOR')")
    public ResponseEntity<ContactsRequest> getContact(@RequestHeader(name = "email") String email) {

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity = userRepository.findByEmail(userName)
                .orElseThrow(() -> ExpoException.error(
                        MessageCode.ACCESS_DENIED)
                );

        ExhibitionDetailsEntity exhibitionDetail = exhibitionDetails.findByExhibitorId(userEntity.getId())
                .orElseThrow(() ->
                        ExpoException.error(MessageCode.TRANSACTION_NOT_FOUND)
                );
        ExhibitionContactEntity contactEntity = contactsRepo.findByEmailAndExhibitionDetails(email, exhibitionDetail)
                .orElseThrow(() -> ExpoException.error(
                        MessageCode.CONTACTS_NOT_FOUND
                ));
        ContactsRequest contactsRequest = ContactsRequest.builder()
                .email(contactEntity.getEmail())
                .name(contactEntity.getName())
                .company(contactEntity.getCompany())
                .notes(contactEntity.getNotes())
                .occupation(contactEntity.getOccupation())
                .phone1(contactEntity.getPhone1())
                .phone2(contactEntity.getPhone2())
                .build();

        return new ResponseEntity<>(contactsRequest, HttpStatus.OK);
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
            paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @DeleteMapping("/contacts")
    @PreAuthorize("hasAuthority('EXHIBITOR')")
    public ResponseEntity<MessageResponse> deleteContact(@RequestHeader String email) {

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity = userRepository.findByEmail(userName)
                .orElseThrow(() -> ExpoException.error(
                        MessageCode.ACCESS_DENIED)
                );

        ExhibitionDetailsEntity exhibitionDetail = exhibitionDetails.findByExhibitorId(userEntity.getId())
                .orElseThrow(() ->
                        ExpoException.error(MessageCode.TRANSACTION_NOT_FOUND)
                );
        ExhibitionContactEntity contact = contactsRepo.findByEmailAndExhibitionDetails(email, exhibitionDetail)
                .orElseThrow(() -> ExpoException.error(
                        MessageCode.CONTACTS_NOT_FOUND)
                );

        contactsRepo.delete(contact);
        return new ResponseEntity<>(MessageResponse.builder()
                .messageCode(MessageCode.CONTACT_DELETE_SUCCESS).build(), HttpStatus.ACCEPTED);
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
            paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @PutMapping("/contacts")
    @PreAuthorize("hasAuthority('EXHIBITOR')")
    public ResponseEntity<MessageResponse> updateContact(
            @RequestBody @Valid ContactsRequest contact,
            @RequestHeader String email) {

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity = userRepository.findByEmail(userName)
                .orElseThrow(() -> ExpoException.error(
                        MessageCode.ACCESS_DENIED)
                );

        ExhibitionDetailsEntity exhibitionDetail = exhibitionDetails.findByExhibitorId(userEntity.getId())
                .orElseThrow(() ->
                        ExpoException.error(MessageCode.TRANSACTION_NOT_FOUND)
                );
        ExhibitionContactEntity visitorContact = contactsRepo.findByEmailAndExhibitionDetails(email, exhibitionDetail)
                .orElseThrow(() -> ExpoException.error(
                        MessageCode.EMAIL_NOT_FOUND)
                );

        visitorContact.setName(contact.getName());
        visitorContact.setOccupation(contact.getOccupation());
        visitorContact.setCompany(contact.getCompany());
        visitorContact.setPhone1(contact.getPhone1());
        visitorContact.setPhone2(contact.getPhone2());
        visitorContact.setNotes(contact.getNotes());
        visitorContact.setExhibitionDetails(exhibitionDetail);
        visitorContact.setUpdatedBy(userEntity.getId());
        contactsRepo.save(visitorContact);

        return new ResponseEntity<>(MessageResponse.builder()
                .messageCode(MessageCode.UPDATE_CONTACT_SUCCESS)
                .build(), HttpStatus.ACCEPTED);
    }

}

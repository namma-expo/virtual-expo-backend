package com.nammaexpo.controllers;

import com.nammaexpo.models.enums.MessageCode;
import com.nammaexpo.expection.ExpoException;
import com.nammaexpo.payload.response.MessageResponse;
import com.nammaexpo.payload.request.ContactsRequest;
import com.nammaexpo.persistance.dao.ExhibitionContactRepository;
import com.nammaexpo.persistance.dao.ExhibitionModeratorsRepository;
import com.nammaexpo.persistance.dao.UserRepository;
import com.nammaexpo.persistance.model.ExhibitionContactEntity;
import com.nammaexpo.persistance.model.ExhibitionDetailsEntity;
import com.nammaexpo.persistance.model.ExhibitionModeratorEntity;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Api(value = "contacts")
public class ContactsController {

    private UserRepository userRepository;

    private ExhibitionModeratorsRepository moderatorsRepository;

    ExhibitionContactRepository contactsRepo;

    @Autowired
    public ContactsController(UserRepository userRepository, ExhibitionModeratorsRepository moderatorsRepository,
                              ExhibitionContactRepository contactsRepo) {
        this.userRepository = userRepository;
        this.moderatorsRepository = moderatorsRepository;
        this.contactsRepo = contactsRepo;
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
            allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @PostMapping("/contact")
    @PreAuthorize("hasAuthority('EXHIBITOR')")
    public ResponseEntity<MessageResponse> addContact(@RequestBody @Valid ContactsRequest contact) {

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity = userRepository.findByEmail(userName)
                .orElseThrow(() -> ExpoException.error(
                        MessageCode.ACCESS_DENIED)
                );

        ExhibitionModeratorEntity moderatorEntity = moderatorsRepository.findByIdAndIsActive(userEntity.getId(), Boolean.TRUE)
                .orElseThrow(() -> ExpoException.error(
                        MessageCode.EXHIBITOR_ACCESS_DENIED)
                );
        ExhibitionDetailsEntity exhibitionDetails = moderatorEntity.getExhibitionDetails();

        Optional<ExhibitionContactEntity> optionalContacts = contactsRepo.findByEmailAndExhibitionDetails(contact.getEmail(), exhibitionDetails);

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
                .exhibitionDetails(exhibitionDetails)
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
            allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @GetMapping("/contacts")
    @PreAuthorize("hasAuthority('EXHIBITOR')")
    public ResponseEntity<List<ContactsRequest>> getAllContacts() {

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity = userRepository.findByEmail(userName)
                .orElseThrow(() -> ExpoException.error(
                        MessageCode.ACCESS_DENIED)
                );

        ExhibitionModeratorEntity moderatorEntity = moderatorsRepository.findByIdAndIsActive(userEntity.getId(), Boolean.TRUE)
                .orElseThrow(() -> ExpoException.error(
                        MessageCode.EXHIBITOR_ACCESS_DENIED)
                );
        ExhibitionDetailsEntity exhibitionDetails = moderatorEntity.getExhibitionDetails();

        List<ExhibitionContactEntity> contactsList = contactsRepo.findAllByExhibitionDetails(exhibitionDetails);

        List<ContactsRequest> allContacts = new ArrayList<>();
        if (!contactsList.isEmpty()) {
            for (ExhibitionContactEntity contactEntity :
                    contactsList) {
                allContacts.add(ContactsRequest.builder()
                        .email(contactEntity.getEmail())
                        .name(contactEntity.getName())
                        .company(contactEntity.getCompany())
                        .notes(contactEntity.getNotes())
                        .occupation(contactEntity.getOccupation())
                        .phone1(contactEntity.getPhone1())
                        .phone2(contactEntity.getPhone2())
                        .build());
            }
        } else
            throw ExpoException.error(MessageCode.CONTACTS_NOT_FOUND);

        return new ResponseEntity<>(allContacts, HttpStatus.OK);
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
            allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @GetMapping( path = "/contact")
    @PreAuthorize("hasAuthority('EXHIBITOR')")
    public ResponseEntity<ContactsRequest> getContact(@RequestHeader String email) {

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity = userRepository.findByEmail(userName)
                .orElseThrow(() -> ExpoException.error(
                        MessageCode.ACCESS_DENIED)
                );

        ExhibitionModeratorEntity moderatorEntity = moderatorsRepository.findByIdAndIsActive(userEntity.getId(), Boolean.TRUE)
                .orElseThrow(() -> ExpoException.error(
                        MessageCode.EXHIBITOR_ACCESS_DENIED)
                );
        ExhibitionDetailsEntity exhibitionDetails = moderatorEntity.getExhibitionDetails();

        ExhibitionContactEntity contactEntity = contactsRepo.findByEmailAndExhibitionDetails(email, exhibitionDetails)
                .orElseThrow(() ->  ExpoException.error(
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
            allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @DeleteMapping("/contact")
    @PreAuthorize("hasAuthority('EXHIBITOR')")
    public ResponseEntity<MessageResponse> deleteContact(@RequestHeader String email) {

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity = userRepository.findByEmail(userName)
                .orElseThrow(() -> ExpoException.error(
                        MessageCode.ACCESS_DENIED)
                );

        ExhibitionModeratorEntity moderatorEntity = moderatorsRepository.findByIdAndIsActive(userEntity.getId(), Boolean.TRUE)
                .orElseThrow(() -> ExpoException.error(
                        MessageCode.EXHIBITOR_ACCESS_DENIED)
                );
        ExhibitionDetailsEntity exhibitionDetails = moderatorEntity.getExhibitionDetails();

        ExhibitionContactEntity contact = contactsRepo.findByEmailAndExhibitionDetails(email, exhibitionDetails)
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
            allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @PutMapping("/contact")
    @PreAuthorize("hasAuthority('EXHIBITOR')")
    public ResponseEntity<MessageResponse> updateContact(@RequestBody @Valid ContactsRequest contact, @RequestHeader String email) {

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity = userRepository.findByEmail(userName)
                .orElseThrow(() -> ExpoException.error(
                        MessageCode.ACCESS_DENIED)
                );

        ExhibitionModeratorEntity moderatorEntity = moderatorsRepository.findByIdAndIsActive(userEntity.getId(), Boolean.TRUE)
                .orElseThrow(() -> ExpoException.error(
                        MessageCode.EXHIBITOR_ACCESS_DENIED)
                );
        ExhibitionDetailsEntity exhibitionDetails = moderatorEntity.getExhibitionDetails();

        ExhibitionContactEntity visitorContact = contactsRepo.findByEmailAndExhibitionDetails(email, exhibitionDetails)
                .orElseThrow(() -> ExpoException.error(
                        MessageCode.EMAIL_NOT_FOUND)
                );

        visitorContact.setEmail(contact.getEmail());
        visitorContact.setName(contact.getName());
        visitorContact.setOccupation(contact.getOccupation());
        visitorContact.setCompany(contact.getCompany());
        visitorContact.setPhone1(contact.getPhone1());
        visitorContact.setPhone2(contact.getPhone2());
        visitorContact.setNotes(contact.getNotes());
        visitorContact.setExhibitionDetails(exhibitionDetails);
        visitorContact.setUpdatedBy(userEntity.getId());
        contactsRepo.save(visitorContact);

        return new ResponseEntity<>(MessageResponse.builder()
                .messageCode(MessageCode.UPDATE_CONTACT_SUCCESS)
                .build(),HttpStatus.ACCEPTED);
    }

}

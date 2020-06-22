package com.nammaexpo.services.impl;

import com.nammaexpo.models.enums.MessageCode;
import com.nammaexpo.payload.request.ContactsDTO;
import com.nammaexpo.payload.response.MessageResponse;
import com.nammaexpo.persistance.dao.ExhibitionContactRepository;
import com.nammaexpo.persistance.model.ExhibitionContactEntity;
import com.nammaexpo.services.ContactsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ContactsServiceImpl implements ContactsService {

    @Autowired
    ExhibitionContactRepository contactsRepo;

    @Override
    public ResponseEntity<MessageResponse> addNewContact(ContactsDTO contact, String createdBy) {

        int createrId = 0;
        int exhibitionId = 0;

        contactsRepo.save(ExhibitionContactEntity.builder()
                .email(contact.getEmail())
                .name(contact.getName())
                .occupation(contact.getOccupation())
                .company(contact.getCompany())
                .phone1(contact.getPhone1())
                .phone2(contact.getPhone2())
                .notes(contact.getNotes())
                .exhibitionId(exhibitionId)
                .createdBy(createrId).build());

        log.debug("Added new contact");

        return ResponseEntity.ok(MessageResponse.builder()
                .messageCode(MessageCode.CREATE_CONTACT_SUCCESS)
                .build());
    }

    @Override
    public ResponseEntity<MessageResponse> updateContact(ExhibitionContactEntity contactEntity, @Valid ContactsDTO contact, String updatedBy) {

        int exhibitionId = 0;
        int updaterId = 0;

        contactEntity.setEmail(contact.getEmail());
        contactEntity.setName(contact.getName());
        contactEntity.setOccupation(contact.getOccupation());
        contactEntity.setCompany(contact.getCompany());
        contactEntity.setPhone1(contact.getPhone1());
        contactEntity.setPhone2(contact.getPhone2());
        contactEntity.setNotes(contact.getNotes());
        contactEntity.setExhibitionId(exhibitionId);
        contactEntity.setUpdatedBy(updaterId);
        contactsRepo.save(contactEntity);

        return new ResponseEntity<>(MessageResponse.builder()
                .messageCode(MessageCode.UPDATE_CONTACT_SUCCESS)
                .build(),HttpStatus.ACCEPTED);
    }

    @Override
    public List<ContactsDTO> getAllContacts() {

        List<ExhibitionContactEntity> contactsList = contactsRepo.findAll();
        List<ContactsDTO> contactsDTOS = new ArrayList<>();
        if (!contactsList.isEmpty()) {
            for (ExhibitionContactEntity contactEntity :
                    contactsList) {
                contactsDTOS.add(ContactsDTO.builder()
                        .email(contactEntity.getEmail())
                        .name(contactEntity.getName())
                        .company(contactEntity.getCompany())
                        .notes(contactEntity.getNotes())
                        .occupation(contactEntity.getOccupation())
                        .phone1(contactEntity.getPhone1())
                        .phone2(contactEntity.getPhone2())
                        .build());
            }
        }
        return contactsDTOS;
    }

    @Override
    public ContactsDTO getContact(String email) {

        Optional<ExhibitionContactEntity> contact = contactsRepo.findByEmail(email);
        ContactsDTO contactsDTO = null;
        if (contact.isPresent()) {

        ExhibitionContactEntity contactEntity = contact.get();
            contactsDTO = ContactsDTO.builder()
                    .email(contactEntity.getEmail())
                    .name(contactEntity.getName())
                    .company(contactEntity.getCompany())
                    .notes(contactEntity.getNotes())
                    .occupation(contactEntity.getOccupation())
                    .phone1(contactEntity.getPhone1())
                    .phone2(contactEntity.getPhone2())
                    .build();
        }
        return contactsDTO;
    }
}

package com.nammaexpo.persistance.dao;

import com.nammaexpo.persistance.model.Contacts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactsRepo extends JpaRepository<Contacts, Integer> {
    Optional<Contacts> findByUserEmail(String userEmail);
    List<Contacts> findAll();
}

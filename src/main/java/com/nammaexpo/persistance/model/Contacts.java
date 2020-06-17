package com.nammaexpo.persistance.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
public class Contacts {

    public Contacts(){

    }

    @Id
    @GeneratedValue
    private int userId;

    @Column(
            name = "email",
            unique = true
    )
    private String userEmail;

    @Column(
            name = "userName",
            nullable = false
    )
    private String userName;

    @Column(
            name = "occupation"
    )
    private String occupation;

    @Column(
            name = "company"
    )
    private String companyName;

    @Column(
            name = "address1"
    )
    private String address1;

    @Column(
            name = "address2"
    )
    private String address2;

    @Column(
            name = "country",
            nullable = false
    )
    private String country;

    @Column(
            name = "state",
            nullable = false
    )
    private String state;

    @Column(
            name = "city",
            nullable = false
    )
    private String city;

    @Column(
            name = "phone1"
    )
    private String phone1;

    @Column(
            name = "phone2"
    )
    private String phone2;

    @Column (
            name = "visitDate",
            updatable = false,
            nullable = false,
            columnDefinition = "DATETIME(3) DEFAULT CURRENT_TIMESTAMP(3)"
    )
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss z", timezone = "IST")
    private Date visitDate;

    @Column(
            name = "clientReq"
    )
    private String clientReq;

    @Column(
            name = "remarks"
    )
    private String notes;

    @Column(
            name = "createdBy",
            nullable = false,
            columnDefinition = "VARCHAR(40) DEFAULT ''"
    )
    private String createdBy;

    @Column (
            name = "createdOn",
            updatable = false,
            nullable = false,
            columnDefinition = "DATETIME(3) DEFAULT CURRENT_TIMESTAMP(3)"
    )
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss z", timezone = "IST")
    private Date createdOn;

    @Column(
            name = "modifiedBy",
            columnDefinition = "VARCHAR(40) DEFAULT ''"
    )
    private String modifiedBy;

    @Column (
            name = "modifiedOn",
            columnDefinition = "DATETIME(3) DEFAULT CURRENT_TIMESTAMP(3)"
    )
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss z", timezone = "IST")
    private Date modifiedOn;

}

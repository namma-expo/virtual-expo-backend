package com.nammaexpo.persistance.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "exhibition_contacts"
)
public class ExhibitionContactEntity {
    @Id
    @GeneratedValue
    private int id;

    @Column(
            name = "name",
            nullable = false
    )
    private String name;

    @Column(
            name = "email"
    )
    private String email;

    @Column(
            name = "notes",
            columnDefinition = "TEXT"
    )
    private String notes;

    @Column(
            name = "company"
    )
    private String company;

    @Column(
            name = "occupation"
    )
    private String occupation;

    @Column(
            name = "phone1"
    )
    private String phone1;

    @Column(
            name = "phone2"
    )
    private String phone2;

    @Column(
            name = "exhibition_id",
            nullable = false
    )
    private int exhibitionId;

    @Column(
            name = "created_by",
            nullable = false
    )
    private int createdBy;

    @Column(
            name = "created_at",
            updatable = false,
            nullable = false,
            columnDefinition = "DATETIME(3) DEFAULT CURRENT_TIMESTAMP(3)"
    )
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss z", timezone = "IST")
    private Date createdAt;

    @Column(
            name = "updated_by"
    )
    private int updatedBy;

    @Column(
            name = "updated_at",
            columnDefinition = "DATETIME(3) DEFAULT CURRENT_TIMESTAMP(3)"
    )
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss z", timezone = "IST")
    private Date updatedAt;

    @Builder
    public ExhibitionContactEntity(String name, String email,
                                   String notes, String company, String occupation, String phone1,
                                   String phone2, int exhibitionId, int createdBy, int updatedBy) {
        this.name = name;
        this.email = email;
        this.notes = notes;
        this.company = company;
        this.occupation = occupation;
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.exhibitionId = exhibitionId;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }
}

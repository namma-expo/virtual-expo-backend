package com.nammaexpo.persistance.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "exhibition_moderators",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "exhibition_id"}),
        })
public class ExhibitionModeratorEntity {
    @Id
    @GeneratedValue
    private int id;

    @Column(
            name = "user_id",
            nullable = false
    )
    private int userId;

    @Column(
            name = "is_active",
            nullable = false,
            columnDefinition = "BOOLEAN DEFAULT 1"
    )
    private boolean isActive;

    @Column(
            name = "exhibition_id",
            nullable = false,
            updatable = false
    )
    private int exhibitionId;

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
            name = "created_by",
            nullable = false
    )
    private int createdBy;

}

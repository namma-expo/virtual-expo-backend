package com.nammaexpo.persistance.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(
            name = "is_active",
            nullable = false,
            columnDefinition = "BOOLEAN DEFAULT 1"
    )
    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "exhibition_id")
    private ExhibitionDetailsEntity exhibitionDetails;

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

    @Builder
    public ExhibitionModeratorEntity(UserEntity user, boolean isActive,
                                     ExhibitionDetailsEntity exhibitionDetails, int createdBy) {
        this.user = user;
        this.isActive = isActive;
        this.exhibitionDetails = exhibitionDetails;
        this.createdBy = createdBy;
    }
}

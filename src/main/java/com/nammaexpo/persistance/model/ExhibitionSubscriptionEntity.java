package com.nammaexpo.persistance.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nammaexpo.models.enums.SubscriptionPlan;
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
        name = "exhibition_subscription",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"plan_id", "exhibition_id"})
        }
)
public class ExhibitionSubscriptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(
            name = "plan_id",
            nullable = false
    )
    @Enumerated(EnumType.STRING)
    private SubscriptionPlan planId;


    @ManyToOne
    @JoinColumn(name = "exhibition_id")
    private ExhibitionDetailsEntity exhibition;

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
            name = "updated_at",
            nullable = false,
            columnDefinition = "DATETIME(3) DEFAULT CURRENT_TIMESTAMP(3)"
    )
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss z", timezone = "IST")
    private Date updatedAt;

    @Column(
            name = "deleted_at",
            columnDefinition = "DATETIME(3) DEFAULT NULL"
    )
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss z", timezone = "IST")
    private Date deletedAt;

    @Builder
    public ExhibitionSubscriptionEntity(SubscriptionPlan planId, ExhibitionDetailsEntity exhibition, int createdBy) {
        this.planId = planId;
        this.exhibition = exhibition;
        this.createdBy = createdBy;
    }
}

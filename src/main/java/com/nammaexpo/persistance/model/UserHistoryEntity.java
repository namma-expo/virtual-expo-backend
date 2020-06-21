package com.nammaexpo.persistance.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nammaexpo.models.enums.UserAction;
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
        name = "user_history"
)
public class UserHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(
            name = "user_id",
            nullable = false,
            updatable = false
    )
    private int userId;

    @Column(
            name = "action",
            nullable = false,
            updatable = false
    )
    @Enumerated(EnumType.STRING)
    private UserAction action;

    // NOTE: Nullable is true because of we might store other user actions in the future
    @Column(
            name = "exhibition_id",
            updatable = false
    )
    private String exhibitionId;

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


    @Builder
    public UserHistoryEntity(int userId, UserAction action, String exhibitionId) {
        this.userId = userId;
        this.action = action;
        this.exhibitionId = exhibitionId;
    }
}

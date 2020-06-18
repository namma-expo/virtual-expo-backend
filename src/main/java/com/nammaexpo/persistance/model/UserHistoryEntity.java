package com.nammaexpo.persistance.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nammaexpo.models.enums.UserAction;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
    name = "user_history"
)
public class UserHistoryEntity {
  @Id
  @GeneratedValue
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

}

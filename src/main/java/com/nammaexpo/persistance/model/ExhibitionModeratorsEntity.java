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
import javax.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
    name = "exhibition_moderators",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "exhibition_id"}),
})
public class ExhibitionModeratorsEntity {
  @Id
  @GeneratedValue
  private int id;

  @Column(
      name = "user_id",
      nullable = false
  )
  private String userId;

  @Column(
      name = "is_active",
      nullable = false
  )
  @Enumerated(EnumType.STRING)
  private UserAction action;

  @Column(
      name = "exhibition_id",
      nullable = false,
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

  @Column(
      name = "created_by",
      nullable = false
  )
  private String createdBy;

}

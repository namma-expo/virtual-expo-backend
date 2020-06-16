package com.nammaexpo.persistance.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
      name = "name"
  )
  private String name;

  @Column(
      name = "phone_number"
  )
  private String phoneNumber;

  @Column(
      name = "email"
  )
  private String email;

  @Column(
      name = "notes",
      columnDefinition = "text"
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
      name = "updated_at",
      nullable = false,
      columnDefinition = "DATETIME(3) DEFAULT CURRENT_TIMESTAMP(3)"
  )
  @UpdateTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss z", timezone = "IST")
  private Date updatedAt;
}

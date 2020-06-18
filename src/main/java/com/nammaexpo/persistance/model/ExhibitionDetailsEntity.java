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
import javax.persistence.UniqueConstraint;
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
    name = "exhibition_details",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "identity"),
})
public class ExhibitionDetailsEntity {

  @Id
  @GeneratedValue
  private int id;

  @Column(
      name = "name"
  )
  private String name;

  @Column(
      name = "logo"
  )
  private String logo;

  @Column(
      name = "exhibitor_id",
      nullable = false,
      unique = true
  )
  private int exhibitorId;

  @Column(
      name = "identity",
      unique = true,
      updatable = false,
      nullable = false
  )
  private String identity;

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
      name = "published_at",
      columnDefinition = "DATETIME(3) DEFAULT NULL"
  )
  @Temporal(TemporalType.TIMESTAMP)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss z", timezone = "IST")
  private Date publishedAt;

  @Column(
      name = "approved_at",
      columnDefinition = "DATETIME(3) DEFAULT NULL"
  )
  @Temporal(TemporalType.TIMESTAMP)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss z", timezone = "IST")
  private Date approvedAt;





}

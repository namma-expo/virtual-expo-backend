package com.nammaexpo.persistance.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nammaexpo.models.enums.Role;
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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = "email")
})
public class UserEntity {

  @Id
  @GeneratedValue
  private int userId;

  @Column(
      name = "name",
      nullable = false
  )
  private String name;

  @Column(
      name = "password",
      nullable = false
  )
  private String password;

  @Column(
      name = "email",
      unique = true,
      nullable = false
  )
  private String email;

  @Column(
      name = "identity",
      unique = true,
      nullable = false,
      updatable = false
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
      name = "role",
      nullable = false,
      length = 20
  )
  @Enumerated(EnumType.STRING)
  private Role role;

  @Builder
  public UserEntity(String name, String password, String email, String identity, Role role) {

    this.name = name;
    this.password = password;
    this.email = email;
    this.identity = identity;
    this.role = role;
  }
}
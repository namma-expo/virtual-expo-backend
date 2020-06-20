package com.nammaexpo.persistance.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nammaexpo.models.enums.Role;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
@Table(
    name = "users"
)
public class UserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(
      name = "name",
      nullable = false
  )
  private String name;

  @Column(
      name = "email",
      unique = true,
      nullable = false
  )
  private String email;

  @Column(
      name = "password",
      nullable = false
  )
  private String password;

  @Column(
      name = "identity",
      unique = true,
      nullable = false,
      updatable = false,
      length = 36
  )
  private String identity;

  @Column(
      name = "role",
      nullable = false,
      length = 25
  )
  @Enumerated(EnumType.STRING)
  private Role role;

  @OneToOne(
      mappedBy = "user",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.EAGER
  )
  private UserProfileEntity profile;

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

  @Builder
  public UserEntity(String name, String password, String email, String identity, Role role,
      UserProfileEntity profile) {
    this.name = name;
    this.password = password;
    this.email = email;
    this.identity = identity;
    this.role = role;

    profile.setUser(this);
    this.profile = profile;
  }
}
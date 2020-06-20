package com.nammaexpo.persistance.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nammaexpo.models.layout.Layout;
import com.nammaexpo.utils.SerDe;
import java.io.IOException;
import java.util.Date;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
@Table(
    name = "exhibition_details",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "identity"),
    })
public class ExhibitionDetailsEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(
      name = "name"
  )
  private String name;

  @Column(
      name = "logo"
  )
  private String logo;

  @OneToOne(cascade = CascadeType.DETACH)
  @JoinColumn(name = "exhibitor_id", referencedColumnName = "id")
  private UserEntity exhibitor;

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

  @OneToMany(mappedBy = "exhibitionDetails")
  private Set<ExhibitionModeratorEntity> exhibitionModerators;

  @OneToOne(
      mappedBy = "exhibitionDetails",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.EAGER
  )
  private PageEntity page;

  public Layout getPageDetails() {

    if (page != null && page.getContent() !=null) {
      try {
        return SerDe.mapper().readValue(this.page.getContent(), Layout.class);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  @Builder
  public ExhibitionDetailsEntity(String name, String logo,
      UserEntity exhibitor, String identity) {

    this.name = name;
    this.logo = logo;
    this.exhibitor = exhibitor;
    this.identity = identity;
  }
}

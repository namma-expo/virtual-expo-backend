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
        name = "file_metadata"
)
public class FileMetadataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(
            name = "file_id",
            unique = true,
            updatable = false,
            nullable = false
    )
    private String fileId;

    @Column(
            name = "file_name"
    )
    private String fileName;

    @Column(
            name = "url"
    )
    private String url;

    @Column(
            name = "file_size"
    )
    private long fileSize;

    @Column(
            name = "content_type"
    )
    private String contentType;

    @Column(
            name = "uploaded_by",
            nullable = false
    )
    private int uploadedBy;

    @Column(
            name = "uploaded_at",
            updatable = false,
            nullable = false,
            columnDefinition = "DATETIME(3) DEFAULT CURRENT_TIMESTAMP(3)"
    )
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss z", timezone = "IST")
    private Date uploadedAt;

    @Builder
    public FileMetadataEntity(String fileId, String fileName, String url, long fileSize, String contentType,
                              int uploadedBy) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.url = url;
        this.fileSize = fileSize;
        this.contentType = contentType;
        this.uploadedBy = uploadedBy;
    }
}

package com.shivachi.pacs.demo.app.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@EqualsAndHashCode(of = {"id"})
@Entity
@Table(name = "DICOM_IMAGE")
public class DICOMImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, unique = true)
    private Long id;

    @Column(name = "PATIENT_NAME")
    private String patientName;

    @Column(name = "STUDY_ID")
    private String studyId;

    @Column(name = "SERIES_ID")
    private String seriesId;

    @Column(name = "INSTANCE_ID")
    private String instanceId;

    @Basic
    @CreationTimestamp
    @JsonFormat(pattern = "dd-MMM-yyyy HH:mm:ss")
    @Column(name = "CREATION_DATE", nullable = false)
    private Timestamp creationDate;

    @Basic
    @UpdateTimestamp
    @JsonFormat(pattern = "dd-MMM-yyyy HH:mm:ss")
    @Column(name = "UPDATE_DATE", nullable = false)
    private Timestamp updateDate;
}

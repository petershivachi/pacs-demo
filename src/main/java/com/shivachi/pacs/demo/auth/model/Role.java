package com.shivachi.pacs.demo.auth.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.shivachi.pacs.demo.auth.data.role.AccessRight;
import com.shivachi.pacs.demo.auth.converter.AccessRightConverter;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.List;

@ToString
@Data
@DynamicUpdate
@Entity
@Table(name = "CONFIG_ROLE", uniqueConstraints = {
        @UniqueConstraint(name = "CONFIG_ROLE_ID", columnNames = {"ID"}),
})
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, updatable = false)
    private Long id;

    @Basic
    @Column(name = "NAME", nullable = false)
    private String name;

    @Basic
    @Column(name = "STATUS", nullable = false)
    private Integer status;

    @Basic
    @Convert(converter = AccessRightConverter.class)
    @Column(name = "ACCESS_RIGHTS", nullable = false, columnDefinition = "text")
    private List<AccessRight> accessRights;

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

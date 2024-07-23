package com.shivachi.pacs.demo.auth.model;

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
@EqualsAndHashCode(of = {"id"})
@DynamicUpdate
@Entity
@Table(name = "CONFIG_USER", uniqueConstraints = {
        @UniqueConstraint(name = "CONFIG_USER_ID", columnNames = {"ID"}),
        @UniqueConstraint(name = "CONFIG_USER_EMAIL", columnNames = {"EMAIL"}),
        @UniqueConstraint(name = "CONFIG_USER_STAFF_NO", columnNames = {"STAFF_NO"}),
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, updatable = false)
    private Long id;

    @Basic
    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Basic
    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;

    @Basic
    @Column(name = "LAST_NAME")
    private String lastName;

    @Basic
    @Column(name = "STAFF_NO")
    private String staffNo;

    @Basic
    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Basic
    @Column(name = "STATUS", nullable = false)
    private String status;

    @Basic
    @Column(name = "FIRST_LOGIN", nullable = false)
    private Integer firstLogin;

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

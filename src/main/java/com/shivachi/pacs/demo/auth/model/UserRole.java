package com.shivachi.pacs.demo.auth.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"ID"})
@DynamicUpdate
@Entity
@Table(name = "USER_ROLE_CONFIG", uniqueConstraints = {
        @UniqueConstraint(name = "USER_ROLE_CONFIG_ID", columnNames = {"ID"})
})
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "USER",
            referencedColumnName = "ID",
            nullable = false,
            updatable = false,
            foreignKey = @ForeignKey(name = "USER_ROLE_USER"))
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ROLE",
            referencedColumnName = "ID",
            nullable = false,
            updatable = false,
            foreignKey = @ForeignKey(name = "USER_ROLE_ROLE"))
    private Role role;

    @Column(name = "STATUS")
    private Integer status;

    @CreationTimestamp
    @JsonFormat(pattern = "dd-MMM-yyyy HH:mm:ss")
    @Column(name = "CREATION_DATE", nullable = false)
    private Timestamp creation_date;

    @UpdateTimestamp
    @JsonFormat(pattern = "dd-MMM-yyyy HH:mm:ss")
    @Column(name = "UPDATE_DATE", nullable = false)
    private Timestamp update_date;
}

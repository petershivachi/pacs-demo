package com.shivachi.pacs.demo.auth.data.user;

import com.shivachi.pacs.demo.auth.data.role.RoleData;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserData {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String staffNo;
    private String status;
    private Integer firstLogin;
    private Timestamp creationDate;
    private Timestamp updateDate;
    private List<RoleData> roles = null;
}

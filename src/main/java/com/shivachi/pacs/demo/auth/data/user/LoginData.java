package com.shivachi.pacs.demo.auth.data.user;

import com.shivachi.pacs.demo.auth.data.role.RoleData;
import lombok.*;

import java.util.List;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginData {
    @Builder.Default
    private String shortCode = null;

    @Builder.Default
    private String email = null;

    @Builder.Default
    private Integer firstLogin = null;

    @Builder.Default
    private String firstName = null;

    @Builder.Default
    private String lastName = null;

    @Builder.Default
    private List<RoleData> roles = null;

    @Builder.Default
    private String token = null;
}

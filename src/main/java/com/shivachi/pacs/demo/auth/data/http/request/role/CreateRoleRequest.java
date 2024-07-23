package com.shivachi.pacs.demo.auth.data.http.request.role;

import com.shivachi.pacs.demo.auth.data.role.AccessRight;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateRoleRequest {
    private String name;
    private List<AccessRight> accessRights;
}

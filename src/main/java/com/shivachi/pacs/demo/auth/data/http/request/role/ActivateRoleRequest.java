package com.shivachi.pacs.demo.auth.data.http.request.role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivateRoleRequest {
    private String name;
    private Integer status;
}

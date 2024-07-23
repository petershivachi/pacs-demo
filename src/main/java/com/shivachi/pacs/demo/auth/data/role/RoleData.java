package com.shivachi.pacs.demo.auth.data.role;

import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleData {
    @Builder.Default
    private Long id = null;

    @Builder.Default
    private String name = null;

    @Builder.Default
    private Integer status = null;

    @Builder.Default
    private Timestamp creationDate = null;

    @Builder.Default
    private Timestamp updateDate = null;

    @Builder.Default
    private List<RoleAccessRight> accessRights = null;
}

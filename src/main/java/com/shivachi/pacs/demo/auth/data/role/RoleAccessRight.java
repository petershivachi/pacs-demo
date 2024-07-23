package com.shivachi.pacs.demo.auth.data.role;

import lombok.*;

@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleAccessRight {
    @Builder.Default
    private String name = null;

    @Builder.Default
    private AccessRight accessRight = null;

    @Builder.Default
    private String category = null;

    @Builder.Default
    private String subCategory = null;
}

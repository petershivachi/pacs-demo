package com.shivachi.pacs.demo.auth.config.securityconfig;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class RoleSecurityConfig implements HttpSecurityConfig{
    @Override
    public Consumer<ServerHttpSecurity> configuration() {
        return (http) -> {
            http.authorizeExchange(exchanges -> exchanges
                    .pathMatchers(HttpMethod.POST, "/api/v1/access-management/roles/create").hasAnyAuthority("CREATE_ROLE")
                    .pathMatchers(HttpMethod.PUT, "/api/v1/access-management/roles/update").hasAnyAuthority("UPDATE_ROLE")
                    .pathMatchers(HttpMethod.PUT, "/api/v1/access-management/roles/change-status").hasAnyAuthority("UPDATE_ROLE_STATUS")
                    .pathMatchers(HttpMethod.GET, "/api/v1/access-management/roles/role-details").hasAnyAuthority("VIEW_ROLE_DETAILS")
                    .pathMatchers(HttpMethod.GET, "/api/v1/access-management/roles/list").hasAnyAuthority("VIEW_ROLES")
                    .pathMatchers(HttpMethod.GET, "/api/v1/access-management/roles/access-rights").hasAnyAuthority("VIEW_ACCESS_RIGHTS")
            );
        };
    }
}

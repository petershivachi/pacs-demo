package com.shivachi.pacs.demo.auth.config.securityconfig;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class UserSecurityConfig implements HttpSecurityConfig {
    @Override
    public Consumer<ServerHttpSecurity> configuration() {
        return (http) -> {
            http.authorizeExchange(exchanges -> exchanges
                            .pathMatchers(HttpMethod.POST, "/api/v1/access-management/users/create").hasAnyAuthority("CREATE_USER")
                            .pathMatchers(HttpMethod.PUT, "/api/v1/access-management/users/update/{email}").hasAnyAuthority("UPDATE_USER")
                            .pathMatchers(HttpMethod.PUT, "/api/v1/access-management/users/lock").hasAnyAuthority("UPDATE_USER")
                            .pathMatchers(HttpMethod.PUT, "/api/v1/access-management/users/activate").hasAnyAuthority("UPDATE_USER")
                            .pathMatchers(HttpMethod.PUT, "/api/v1/access-management/users/delete").hasAnyAuthority("DELETE_USER")
                            .pathMatchers(HttpMethod.PUT, "/api/v1/access-management/users/update-password").hasAnyAuthority("UPDATE_USER_PASSWORD")
                            .pathMatchers(HttpMethod.GET, "/api/v1/access-management/users/list").hasAnyAuthority("VIEW_USERS")
                            .pathMatchers(HttpMethod.GET, "/api/v1/access-management/users/user-template").hasAnyAuthority("DOWNLOAD_USER_TEMPLATE")
                            .pathMatchers(HttpMethod.POST, "/api/v1/access-management/users/customer_bulk_upload").hasAnyAuthority("USER_BULK_UPLOAD")
                            .pathMatchers(HttpMethod.POST, "/api/v1/access-management/users/remove/{userCode}").hasAnyAuthority("REMOVE_USER")
                    );
        };
    }
}

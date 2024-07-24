package com.shivachi.pacs.demo.auth.config.securityconfig;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class ImageSecurityConfig implements HttpSecurityConfig {
    @Override
    public Consumer<ServerHttpSecurity> configuration() {
        return (http) -> {
            http.authorizeExchange(exchanges -> exchanges
                    .pathMatchers(HttpMethod.GET, "/api/v1/dicom-images/fetch/{instanceId}").hasAnyAuthority("VIEW_IMAGE")
                    .pathMatchers(HttpMethod.PUT, "/api/v1/dicom-images/save/{instanceId}").hasAnyAuthority("SAVE_IMAGE")
            );
        };
    }
}

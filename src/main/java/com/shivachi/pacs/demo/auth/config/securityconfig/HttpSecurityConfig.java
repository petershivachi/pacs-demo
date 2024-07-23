package com.shivachi.pacs.demo.auth.config.securityconfig;

import org.springframework.security.config.web.server.ServerHttpSecurity;

import java.util.function.Consumer;

public interface HttpSecurityConfig {
    Consumer<ServerHttpSecurity> configuration();
}

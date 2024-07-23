package com.shivachi.pacs.demo.auth.config;

import com.shivachi.pacs.demo.auth.config.securityconfig.HttpSecurityConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.savedrequest.NoOpServerRequestCache;

import java.util.List;

@Log
@Configuration
@RequiredArgsConstructor
@EnableWebFluxSecurity
public class HttpConfigurer {
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    @Bean
    @Primary
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, final List<HttpSecurityConfig> httpConfigurations) {

        http
//                .exceptionHandling()
//                .authenticationEntryPoint((swe, e) -> {
//                    swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//                    throw new AccessDeniedException(String.format("%s Unauthorized access denied", HttpStatus.UNAUTHORIZED.value()));
//                })
//                .accessDeniedHandler((swe, e) -> {
//                    swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//                    throw new AccessDeniedException(String.format("%s Unauthorized access denied", HttpStatus.UNAUTHORIZED.value()));
//                })
//                .and()
//                .requestCache().requestCache(NoOpServerRequestCache.getInstance())
//                .and()
//                .csrf().disable()
//                .formLogin().disable()
//                .logout().disable()
//                .httpBasic().disable()
                .securityContextRepository(securityContextRepository);

        httpConfigurations.forEach(config -> config.configuration().accept(http));
        http
                .authenticationManager(authenticationManager)
                .authorizeExchange(exchanges -> exchanges
                .pathMatchers(HttpMethod.GET, "/swagger-*/**", "/v2/api-docs/**", "/v3/api-docs/**").permitAll()
                .pathMatchers(HttpMethod.POST, "/api/v1/auth/**").permitAll()
                .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .anyExchange()
                .authenticated());

        return http.build();
    }

    @Bean
    @Primary
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

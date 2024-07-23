package com.shivachi.pacs.demo.auth.config;

import com.shivachi.pacs.demo.auth.integ.UserService;
import com.shivachi.pacs.demo.auth.model.Role;
import com.shivachi.pacs.demo.utilities.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.logging.Level;

@Component
@RequiredArgsConstructor
@Log
public class AuthenticationManager implements ReactiveAuthenticationManager {
    private final UserService userService;
    private final JWTUtil jwtUtil;


    @Override
    public Mono<Authentication> authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication != null && authentication.getPrincipal() != null) {
            log.log(Level.FINE, String.format("Http validate auth [ principal=%s, credentials=%s, data=%s ]", authentication.getPrincipal(), authentication.getCredentials(), authentication));

            String authToken = authentication.getPrincipal().toString();

            String email = jwtUtil.getEmailFromToken(authToken);

            List<Role> roles = this.userService.validateUser(email);

            if (roles != null && !roles.isEmpty()) {
                return Mono.just(new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), roles.stream().map(Role::getAccessRights).toList().stream().flatMap(Collection::stream).toList().stream().map(s -> new SimpleGrantedAuthority(s.name())).distinct().toList()));
            } else {
                return Mono.just(authentication);
            }
        } else {
            log.log(Level.WARNING, String.format("Http validate auth no authenticate [ %s ]", authentication));
            return Mono.just(new UsernamePasswordAuthenticationToken("", ""));
        }
    }
}

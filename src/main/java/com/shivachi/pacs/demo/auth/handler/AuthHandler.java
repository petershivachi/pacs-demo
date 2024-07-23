package com.shivachi.pacs.demo.auth.handler;

import com.shivachi.pacs.demo.auth.data.http.request.user.UserLoginRequest;
import com.shivachi.pacs.demo.auth.data.http.response.LoginResponse;
import com.shivachi.pacs.demo.auth.integ.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Api(tags = "User Authentication")
public class AuthHandler {
    private final UserService userService;


    @RequestMapping(
            path = "/login",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation("Login")
    public Mono<ResponseEntity<LoginResponse>> login (@RequestBody UserLoginRequest request){
        return Mono.just(ResponseEntity.ok().body(userService.authenticateUser(request.getEmail(), request.getPassword())));
    }
}

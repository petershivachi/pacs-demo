package com.shivachi.pacs.demo.auth.handler;


import com.shivachi.pacs.demo.auth.data.http.EntityResponse;
import com.shivachi.pacs.demo.auth.data.http.request.user.*;
import com.shivachi.pacs.demo.auth.data.http.response.UsersResponse;
import com.shivachi.pacs.demo.auth.integ.UserService;
import com.shivachi.pacs.demo.auth.data.Constant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/access-management/users")
@Api(tags = "User Management")
public class UserHandler {
    private final UserService userService;

    @RequestMapping(
            path = "/create",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation("Create User")
    public Mono<ResponseEntity<Mono<EntityResponse>>> createUser (@RequestBody CreateUserRequest request){
        return Mono.just(ResponseEntity.ok().body(userService.createUser(request)));
    }

    @RequestMapping(
            path = "/update/{email}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation("Update User")
    public Mono<ResponseEntity<EntityResponse>> updateUser (@PathVariable String email, @RequestBody UpdateUserRequest request){
        return Mono.just(ResponseEntity.ok().body(userService.updateUser(email, request.getFirstName(), request.getLastName())));
    }

    @RequestMapping(
            path = "/lock",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation("Lock User")
    public Mono<ResponseEntity<EntityResponse>> lockUser (@RequestBody UpdateStatusRequest request){
        return Mono.just(ResponseEntity.ok().body(userService.updateUserStatus(request.getEmail(), Constant.DISABLED.name())));
    }

    @RequestMapping(
            path = "/activate",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation("Activate User")
    public Mono<ResponseEntity<EntityResponse>> activateUser (@RequestBody UpdateStatusRequest request){
        return Mono.just(ResponseEntity.ok().body(userService.updateUserStatus(request.getEmail(), Constant.ACTIVE.name())));
    }

    @RequestMapping(
            path = "/delete",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation("Delete User")
    public Mono<ResponseEntity<EntityResponse>> deleteUser (@RequestBody UpdateStatusRequest request){
        return Mono.just(ResponseEntity.ok().body(userService.updateUserStatus(request.getEmail(), Constant.DELETED.name())));
    }

    @RequestMapping(
            path = "/update-password",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation("Update User Password")
    public Mono<ResponseEntity<EntityResponse>> updateUserPassword (@RequestBody UpdatePasswordRequest request){
        return Mono.just(ResponseEntity.ok().body(userService.updateUserPassword(request.getEmail(), request.getOldPassword(), request.getNewPassword())));
    }

    @RequestMapping(
            path = "/update-role",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation("Update User Role")
    public Mono<ResponseEntity<EntityResponse>> updatePassword (@RequestBody UpdateUserRoleRequest request){
        return Mono.just(ResponseEntity.ok().body(userService.updateUserRole(request.getEmail(), request.getRoleId())));
    }

    @PreAuthorize(value = "hasAnyAuthority('VIEW_USERS')")
    @RequestMapping(
            path = "/list",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation("Lists Users, Allowed Status: 'ALL' , 'ACTIVE', 'DELETED', 'DISABLED'")
    public Mono<ResponseEntity<UsersResponse>> fetchUsers (@RequestParam("status") String status){
        AtomicReference<UsersResponse> response = new AtomicReference<>();
        if(Objects.equals(status, "ALL")){
            response.set(userService.getAllUsers());
        }else {
            response.set(userService.getAllUsersByStatus(status));
        }
        return Mono.just(ResponseEntity.ok().body(response.get()));
    }

}

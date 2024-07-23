package com.shivachi.pacs.demo.auth.handler;

import com.shivachi.pacs.demo.auth.data.http.request.role.ActivateRoleRequest;
import com.shivachi.pacs.demo.auth.data.role.RoleData;
import com.shivachi.pacs.demo.auth.data.http.request.role.CreateRoleRequest;
import com.shivachi.pacs.demo.auth.data.http.EntityResponse;
import com.shivachi.pacs.demo.auth.data.http.response.RoleResponse;
import com.shivachi.pacs.demo.auth.data.role.RoleAccessRight;
import com.shivachi.pacs.demo.auth.integ.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/access-management/roles")
@Api(tags = "Role Management")
public class RoleHandler {
    private final RoleService roleService;

    @RequestMapping(
            path = "/access-rights",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation("Fetch Access Rights")
    public Mono<ResponseEntity<List<RoleAccessRight>>> getAccessRights(){
        return Mono.just(ResponseEntity.ok().body(roleService.accessRights()));
    }

    @RequestMapping(
            path = "/create",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation("Maintain Role")
    public Mono<ResponseEntity<EntityResponse>> createRole (@RequestBody CreateRoleRequest request){
        return Mono.just(ResponseEntity.ok().body(roleService.createRole(request.getName(), request.getAccessRights())));
    }

    @RequestMapping(
            path = "/update",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation("Update Role")
    public Mono<ResponseEntity<EntityResponse>> updateRole (@RequestBody CreateRoleRequest request){
        return Mono.just(ResponseEntity.ok().body(roleService.updateRole(request)));
    }

    @RequestMapping(
            path = "/change-status",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation("Activate or Deactivate Role")
    public Mono<ResponseEntity<EntityResponse>> activateDeactivateRole (@RequestBody ActivateRoleRequest request){
        return Mono.just(ResponseEntity.ok().body(roleService.activateDeactivateRole(request)));
    }

    @RequestMapping(
            path = "/role-details",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation("Role Details")
    public Mono<ResponseEntity<RoleData>> findRoleByName (@RequestParam("name") String name){
        return Mono.just(ResponseEntity.ok().body(roleService.getRoleByName(name)));
    }

    @RequestMapping(
            path = "/list",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiOperation("Lists Roles, Allowed status: '2 - All Roles', '1 - Active Roles', '-1 - Inactive roles'")
    public Mono<ResponseEntity<RoleResponse>> fetchRoles (@RequestParam("status") Integer status){
        AtomicReference<RoleResponse> response = new AtomicReference<>();
        if(status == 2){
            response.set(roleService.getRoles());
        }else {
            response.set(roleService.getRolesByStatus(status));
        }
        return Mono.just(ResponseEntity.ok().body(response.get()));
    }
}

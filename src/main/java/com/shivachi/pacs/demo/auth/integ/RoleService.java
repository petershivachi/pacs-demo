package com.shivachi.pacs.demo.auth.integ;

import com.shivachi.pacs.demo.auth.data.http.request.role.ActivateRoleRequest;
import com.shivachi.pacs.demo.auth.data.role.RoleData;
import com.shivachi.pacs.demo.auth.data.http.request.role.CreateRoleRequest;
import com.shivachi.pacs.demo.auth.data.http.response.RoleResponse;
import com.shivachi.pacs.demo.auth.data.role.AccessRight;
import com.shivachi.pacs.demo.auth.data.role.RoleAccessRight;
import com.shivachi.pacs.demo.auth.data.http.EntityResponse;
import com.shivachi.pacs.demo.auth.model.Role;
import com.shivachi.pacs.demo.auth.repo.RoleRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    public List<RoleAccessRight> accessRights(){
        return Arrays.stream(AccessRight.values())
                .map(s -> RoleAccessRight.builder().name(s.getName()).category(s.getCategory()).subCategory(s.getSubCategory()).accessRight(s).build())
                .collect(Collectors.toList());
    }

    public EntityResponse createRole(@NonNull String name, List<AccessRight> accessRights){
        AtomicReference<EntityResponse> response = new AtomicReference<>();

        roleRepository.findByName(name).ifPresentOrElse(role -> {
            response.set(EntityResponse.builder().resultCode(HttpStatus.BAD_REQUEST.value()).returnMessage(String.format("Role with name %s already exists", name)).build());
            log.warn(String.format("Role with name %s already exists", name));
        }, () -> {
            Role roleConfig = new Role();
            roleConfig.setName(name);
            roleConfig.setAccessRights(accessRights);
            roleConfig.setStatus(1);

            roleRepository.save(roleConfig);

            response.set(EntityResponse.builder().resultCode(HttpStatus.OK.value()).returnMessage("Role created successfully").build());

            log.info("Role created successfully");
        });

        return response.get();
    }

    public EntityResponse activateDeactivateRole(ActivateRoleRequest request){
        AtomicReference<EntityResponse> response = new AtomicReference<>();
        roleRepository.findByName(request.getName()).ifPresentOrElse(role -> {
            role.setStatus(request.getStatus());

            roleRepository.save(role);

            response.set(EntityResponse.builder().resultCode(HttpStatus.OK.value()).returnMessage(String.format("Status for role %s updated successfully", request.getName())).build());
            log.warn(String.format("Status for role %s updated successfully", request.getName()));
        }, () -> {
            response.set(EntityResponse.builder().resultCode(HttpStatus.BAD_REQUEST.value()).returnMessage(String.format("Role with name %s not found", request.getName())).build());
            log.warn(String.format("Role with name %s not found", request.getName()));
        });

        return response.get();
    }

    public EntityResponse updateRole(CreateRoleRequest request){
        AtomicReference<EntityResponse> response = new AtomicReference<>();

        roleRepository.findByName(request.getName()).ifPresentOrElse(role -> {
            if(role.getStatus().compareTo(1) == 0){
                role.setAccessRights(request.getAccessRights());
                roleRepository.save(role);
                response.set(EntityResponse.builder().resultCode(HttpStatus.OK.value()).returnMessage("Role updated successfully").build());
                log.warn("Role updated successfully");
            }else {
                response.set(EntityResponse.builder().resultCode(HttpStatus.BAD_REQUEST.value()).returnMessage(String.format("Role %s is inactive", request.getName())).build());
                log.warn(String.format("Role %s is inactive", request.getName()));
            }
        }, () -> {
            response.set(EntityResponse.builder().resultCode(HttpStatus.BAD_REQUEST.value()).returnMessage(String.format("Role %s not found", request.getName())).build());
            log.warn(String.format("Role %s not found", request.getName()));
        });
        return response.get();
    }

    public RoleResponse getRoles(){
        AtomicReference<RoleResponse> response = new AtomicReference<>();
        List<Role> roles = roleRepository.findAll();
        return getRoleResponse(response, roles);
    }

    private RoleResponse getRoleResponse(AtomicReference<RoleResponse> response, List<Role> roles) {
        List<RoleData> roleData = new ArrayList<>();

        roles.forEach(role -> {
            RoleData myRole = RoleData.builder()
                    .id(role.getId())
                    .name(role.getName())
                    .status(role.getStatus())
                    .creationDate(role.getCreationDate())
                    .updateDate(role.getCreationDate())
                    .build();

            List<RoleAccessRight> accessRights = new ArrayList<>();

            if(role.getAccessRights() != null && !role.getAccessRights().isEmpty()){
                role.getAccessRights().forEach(accessRight -> {
                    log.info("----- Access Right Name ----- {}", accessRight.getName());
                    accessRights.add(RoleAccessRight.builder().name(accessRight.getName()).category(accessRight.getCategory()).subCategory(accessRight.getSubCategory()).accessRight(accessRight).build());
                });
            }

            myRole.setAccessRights(accessRights);

           roleData.add(myRole);
        });

        response.set(RoleResponse.builder().resultCode(HttpStatus.OK.value()).returnMessage("Records Found").data(roleData).build());

        return response.get();
    }

    public RoleResponse getRolesByStatus(Integer status){
        AtomicReference<RoleResponse> response = new AtomicReference<>();
        List<Role> roles = roleRepository.findByStatus(status);
        return getRoleResponse(response, roles);
    }

    public RoleData getRoleByName(String name){
        AtomicReference<RoleData> response = new AtomicReference<>();

        roleRepository.findByName(name).ifPresentOrElse(myRole -> {
            RoleData roleData = RoleData.builder()
                    .id(myRole.getId())
                    .name(myRole.getName())
                    .status(myRole.getStatus())
                    .creationDate(myRole.getCreationDate())
                    .updateDate(myRole.getCreationDate())
                    .build();

            List<RoleAccessRight> accessRights = new ArrayList<>();

            if(myRole.getAccessRights() != null && !myRole.getAccessRights().isEmpty()){
                myRole.getAccessRights().forEach(accessRight -> {
                    accessRights.add(RoleAccessRight.builder().name(accessRight.getName()).category(accessRight.getCategory()).subCategory(accessRight.getCategory()).accessRight(accessRight).build());
                });
            }

            roleData.setAccessRights(accessRights);

            response.set(roleData);
        }, () -> {});

        return response.get();
    }


    public void initRoles() {
        List<Role> currentRoles = roleRepository.findAll();
        System.out.println("Printing roles");
        System.out.println(Arrays.deepToString(currentRoles.toArray()));
        System.out.println("After Printing roles");

        List<AccessRight> accessRights = Arrays.asList(AccessRight.values());

        log.info(String.format("Access Rights %s : ", accessRights));

        if (roleRepository.findAll().isEmpty()) {
            AtomicReference<Role> roleConfig = new AtomicReference<>(new Role());
            roleConfig.get().setName("ROLE_SUPERUSER");
            roleConfig.get().setAccessRights(accessRights);
            roleConfig.get().setStatus(1);
            roleConfig.set(roleRepository.save(roleConfig.get()));
        }
    }


}

package com.shivachi.pacs.demo.auth.integ;

import com.shivachi.pacs.demo.auth.data.http.response.LoginResponse;
import com.shivachi.pacs.demo.auth.data.http.response.UsersResponse;
import com.shivachi.pacs.demo.auth.data.role.RoleData;
import com.shivachi.pacs.demo.auth.model.UserRole;
import com.shivachi.pacs.demo.auth.repo.UserRoleRepository;
import com.shivachi.pacs.demo.auth.utils.PasswordGenerator;
import com.shivachi.pacs.demo.auth.data.Constant;
import com.shivachi.pacs.demo.auth.data.http.EntityResponse;
import com.shivachi.pacs.demo.auth.data.http.request.user.CreateUserRequest;
import com.shivachi.pacs.demo.auth.data.role.RoleAccessRight;
import com.shivachi.pacs.demo.auth.data.user.LoginData;
import com.shivachi.pacs.demo.auth.data.user.UserData;
import com.shivachi.pacs.demo.auth.model.Role;
import com.shivachi.pacs.demo.auth.model.User;
import com.shivachi.pacs.demo.auth.repo.RoleRepository;
import com.shivachi.pacs.demo.auth.repo.UserRepository;
import com.shivachi.pacs.demo.auth.utils.PasswordUtil;
import com.shivachi.pacs.demo.utilities.jwt.JWTUtil;
import com.shivachi.pacs.demo.utilities.mailutil.MailService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    @Value("${organisation.superUserEmail}")
    private String superUserEmail;
    @Value("${organisation.superUserFirstName}")
    private String superUserFirstName;
    @Value("${organisation.superUserLastName}")
    private String superUserLastName;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordUtil passwordUtil;
    private final MailService mailService;
    private final UserRoleRepository userRoleRepository;
    private final JWTUtil jwtUtil;
    private final RoleService roleService;

    public List<Role> validateUser(@NonNull String email) {
        List<Role> roles = new ArrayList<>();

        this.userRepository.findByEmail(email.trim()).ifPresent(user -> {
            if (Objects.equals(user.getStatus(), Constant.ACTIVE.name())) {
                roles.addAll(this.userRoles(user, true));
            }
        });

        return roles;
    }

    public List<Role> validateUser(@NonNull String email, @NonNull String password) {
        List<Role> roles = new ArrayList<>();

        this.userRepository.findByEmail(email.trim().toLowerCase()).ifPresent(user -> {
            if (Objects.equals(user.getStatus(), "Active")) {
                roles.addAll(this.userRoles(user, true));
            }
        });

        return roles;
    }

    public Optional<User> getLoggedInUser(String email) {
        return this.userRepository.findByEmail(email);
    }

    public List<Role> userRoles(@org.springframework.lang.NonNull User user, boolean activeOnly) {
        if (activeOnly) {
            return this.userRoleRepository.findAllByUser(user).stream().map(UserRole::getRole).collect(Collectors.toList());
        } else {
            return this.userRoleRepository.findAllByUserAndStatus(user, 1).stream().map(UserRole::getRole).collect(Collectors.toList());
        }
    }

    public LoginResponse authenticateUser(@NonNull String email, @NonNull String password) {
        AtomicReference<LoginResponse> response = new AtomicReference<>();

        userRepository.findByEmail(email.trim()).ifPresentOrElse(user -> {
            if (Objects.equals(user.getStatus(), Constant.ACTIVE.name())) {
                userRoleRepository.findByUser(user).ifPresentOrElse(userRole -> {
                    if (userRole.getRole().getStatus().compareTo(1) != 0) {
                        response.set(LoginResponse.builder().resultCode(HttpStatus.BAD_REQUEST.value()).returnMessage("User role is deactivated").build());
                    } else {
                        if (passwordUtil.matches(password.trim(), user.getPassword())) {

                            UserData userData = getUserDetails(user.getEmail());

                            String token = jwtUtil.generateJwtToken(userData.getEmail());

                            LoginData authResponse = LoginData.builder()
                                    .token(token)
                                    .firstName(user.getFirstName())
                                    .lastName(user.getLastName())
                                    .email(user.getEmail())
                                    .firstLogin(user.getFirstLogin())
                                    .roles(userData.getRoles())
                                    .build();
                            response.set(LoginResponse.builder().resultCode(HttpStatus.OK.value()).returnMessage("Login success").user(authResponse).build());
                        } else {

                            response.set(LoginResponse.builder().resultCode(HttpStatus.BAD_REQUEST.value()).returnMessage("Email or password provided is incorrect").build());
                        }
                    }
                }, () -> {
                });

            } else {
                response.set(LoginResponse.builder().resultCode(HttpStatus.BAD_REQUEST.value()).returnMessage("Account is " + email +
                        " inactive. Please contact your system admin.").build());
            }
        }, () -> {
            response.set(LoginResponse.builder().resultCode(HttpStatus.BAD_REQUEST.value()).returnMessage("Email or password provided is incorrect").build());
        });

        return response.get();
    }

    public Mono<EntityResponse> createUser(CreateUserRequest body) {
        AtomicReference<EntityResponse> response = new AtomicReference<>();
        User userEmail = userRepository.findByEmail(body.getEmail()).orElse(null);
        User userShortCode = userRepository.findByStaffNo(body.getStaffNo()).orElse(null);
        Role userRole = roleRepository.findById(body.getRole()).orElse(null);

        if(userEmail != null) {
            response.set(EntityResponse.builder().resultCode(HttpStatus.BAD_REQUEST.value()).returnMessage(String.format("User with shortcode %s already exists", body.getStaffNo())).build());
            log.warn(String.format("User with shortcode %s already exists", body.getStaffNo()));
            return Mono.just(response.get());
        }

        if (userShortCode != null) {
            response.set(EntityResponse.builder().resultCode(HttpStatus.BAD_REQUEST.value()).returnMessage(String.format("User with shortcode %s already exists", body.getStaffNo())).build());
            log.warn(String.format("User with shortcode %s already exists", body.getStaffNo()));
            return Mono.just(response.get());
        }

        if(userRole == null){
            response.set(EntityResponse.builder().resultCode(HttpStatus.BAD_REQUEST.value()).returnMessage("Selected role not found").build());
            return Mono.just(response.get());
        } else if (userRole.getStatus().compareTo(1) != 0) {
            response.set(EntityResponse.builder().resultCode(HttpStatus.BAD_REQUEST.value()).returnMessage("Selected role is inactive").build());
            return Mono.just(response.get());
        }

        User user = new User();
        user.setEmail(body.getEmail());
        user.setFirstName(body.getFirstName());
        user.setLastName(body.getLastName());
        user.setStaffNo(body.getStaffNo());
        user.setStatus(Constant.ACTIVE.name());
        user.setFirstLogin(1);
        String password = PasswordGenerator.generatePassword();

        log.info("Generated user password {}", password);
        user.setPassword(passwordUtil.encode(password));

        String mailBody = "Welcome aboard! We are thrilled to have you join the Kanjeru Springs community. "
                + "Here are your login credentials to access our platform:\n\n"
                + "Email: " + body.getEmail() + "\n"
                + "Password: " + password + "\n\n"
                + "Please keep this information secure and do not share it with anyone.\n\n"
                +" You will be required to change your password upon your first login.\n\n"
                + "Thank you for choosing kanjeru Springs!\n\n";

        if(mailService.sendEmail("Digital M-Mita Password", body.getFirstName(), mailBody, body.getEmail())){
            response.set(EntityResponse.builder().resultCode(HttpStatus.OK.value()).returnMessage("User created successfully").build());
        }else{
            response.set(EntityResponse.builder().resultCode(HttpStatus.BAD_REQUEST.value()).returnMessage("SMTP Error occurred while sending password credentials").build());
        }

        return Mono.just(response.get());

    }


    public UserData getUserDetails(@NonNull String email) {
        AtomicReference<UserData> response = new AtomicReference<>();

        this.userRepository.findByEmail(email).ifPresentOrElse(user -> {
            UserData data = UserData.builder()
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .email(user.getEmail())
                    .status(user.getStatus())
                    .creationDate(user.getCreationDate())
                    .updateDate(user.getUpdateDate())
                    .firstLogin(user.getFirstLogin())
                    .build();

            List<RoleData> roles = new ArrayList<>();

            List<UserRole> userRoles = this.userRoleRepository.findAllByUser(user);

            if (userRoles != null && !userRoles.isEmpty()) {
                userRoles.forEach(userRole -> {
                    userRoles(roles, userRole);

                });
                data.setRoles(roles);
            }

            response.set(data);
        }, () -> {

        });

        return response.get();
    }

    public User getUserDetails(@NonNull Long userId) {
        AtomicReference<User> response = new AtomicReference<>();

        this.userRepository.findById(userId).ifPresentOrElse(response::set, () -> {
            response.set(null);
        });

        return response.get();
    }

    public User getUser(@NonNull String customerCode) {
        AtomicReference<User> response = new AtomicReference<>();

        this.userRepository.findByStaffNo(customerCode).ifPresentOrElse(response::set, () -> {
            response.set(null);
        });

        return response.get();
    }

    public EntityResponse updateUser(@NonNull String email, @NonNull String firstName, @NonNull String lastName) {
        AtomicReference<EntityResponse> res = new AtomicReference<>();

        userRepository.findByEmail(email).ifPresentOrElse(myUser -> {
            AtomicReference<User> user = new AtomicReference<>(myUser);
            user.get().setFirstName(firstName.trim());
            user.get().setLastName(lastName.trim());

            user.set(this.userRepository.save(user.get()));


            res.set(EntityResponse.builder().returnMessage("User details updated successfully !").resultCode(HttpStatus.OK.value()).build());
        }, () -> {
            res.set(EntityResponse.builder().returnMessage("User not found !").resultCode(HttpStatus.BAD_REQUEST.value()).build());
        });

        return res.get();
    }

    public EntityResponse updateUserStatus(@NonNull String email, @NonNull String status) {
        AtomicReference<EntityResponse> response = new AtomicReference<>();

        this.userRepository.findByEmail(email).ifPresentOrElse(userData -> {
            AtomicReference<User> user = new AtomicReference<>(userData);
            user.get().setStatus(status);

            user.set(this.userRepository.save(user.get()));

            response.set(EntityResponse.builder().returnMessage("User status updated successfully !").resultCode(HttpStatus.OK.value()).build());
        }, () -> {
            /* todo:: User not found  */
            response.set(EntityResponse.builder().returnMessage("User not found").resultCode(HttpStatus.BAD_REQUEST.value()).build());
        });

        return response.get();
    }

    public EntityResponse updateUserPassword(@NonNull String email, @NonNull String previousPassword,
                                             @NonNull String password) {
        AtomicReference<EntityResponse> response = new AtomicReference<>();

        this.userRepository.findByEmail(email).ifPresentOrElse(userData -> {
            if (Objects.equals(userData.getStatus(), Constant.ACTIVE.name())) {
                if (passwordUtil.matches(previousPassword, userData.getPassword())) {
                    String encodedPassword = passwordUtil.encode(password);
                    if (encodedPassword.equals(userData.getPassword())) {
                        response.set(EntityResponse.builder().returnMessage("Password too similar to your previous password, please try another password !").resultCode(HttpStatus.BAD_REQUEST.value()).build());
                    } else {
                        userData.setPassword(encodedPassword);

                        userData.setFirstLogin(0);
                        userRepository.save(userData);
                        response.set(EntityResponse.builder().returnMessage("Password updated successfully !").resultCode(HttpStatus.OK.value()).build());
                    }
                } else {
                    response.set(EntityResponse.builder().returnMessage("The previous  password you provided is " +
                            "incorrect !").resultCode(HttpStatus.BAD_REQUEST.value()).build());
                }

            } else {
                response.set(EntityResponse.builder().returnMessage(String.format("Account with the email %s is not " +
                        "active ", email)).resultCode(HttpStatus.BAD_REQUEST.value()).build());

            }
        }, () -> {
            /* todo:: User not found  */
            response.set(EntityResponse.builder().returnMessage(String.format("Account with the email %s not found ",
                    email)).resultCode(HttpStatus.BAD_REQUEST.value()).build());
        });

        return response.get();
    }

    public EntityResponse updateUserRole(@NonNull String email, @NonNull Long roleId) throws RuntimeException {
        EntityResponse response = new EntityResponse();

        roleRepository.findById(roleId).ifPresentOrElse((roleConfig -> {
            if (roleConfig.getStatus() == 0) {
                response.setReturnMessage("You cannot assign a locked role");
                response.setResultCode(HttpStatus.BAD_REQUEST.value());
                return;
            }
            userRoleRepository.findByUser_Email(email).ifPresentOrElse((userRole -> {
                System.out.println("Previous role was " + userRole.getRole().getName() + " Current is " + roleConfig.getName());


                log.info("------User Id ----- {}", userRole.getUser().getId());
                log.info("------Role Id ----- {}", roleId);

                userRoleRepository.updateUserRole(roleId, userRole.getUser().getId());
                response.setReturnMessage("User role updated successfully");
                response.setResultCode(HttpStatus.OK.value());
            }), () -> {
                response.setReturnMessage("User not present");
                response.setResultCode(HttpStatus.BAD_REQUEST.value());
            });
        }), () -> {
            response.setReturnMessage("Role not found");
            response.setResultCode(HttpStatus.BAD_REQUEST.value());
        });

        return response;
    }

    public UsersResponse getAllUsers() {
        AtomicReference<UsersResponse> response = new AtomicReference<>();

        List<UserData> usersResponse = new ArrayList<>();
        List<User> users = this.userRepository.findAll();

        return getUsersResponse(response, usersResponse, users);
    }

    public UsersResponse getAllUsersByStatus(String status) {
        AtomicReference<UsersResponse> response = new AtomicReference<>();

        List<UserData> usersResponse = new ArrayList<>();
        List<User> users = this.userRepository.findAllByStatus(status);

        return getUsersResponse(response, usersResponse, users);
    }


    private UsersResponse getUsersResponse(AtomicReference<UsersResponse> response, List<UserData> usersResponse, List<User> users) {
        if (!users.isEmpty()) {
            users.forEach(user -> {
                UserData userData = UserData.builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .status(user.getStatus())
                        .staffNo(user.getStaffNo())
                        .firstLogin(user.getFirstLogin())
                        .creationDate(user.getCreationDate())
                        .updateDate(user.getUpdateDate())
                        .build();

                List<RoleData> roles = new ArrayList<>();

                List<UserRole> userRoles = this.userRoleRepository.findAllByUser(user);
                if (userRoles != null && !userRoles.isEmpty()) {
                    userRoles.forEach(userRole -> {
                        userRoles(roles, userRole);
                        userData.setRoles(roles);
                    });
                }

                usersResponse.add(userData);
            });

            response.set(UsersResponse.builder().resultCode(HttpStatus.OK.value()).returnMessage("Users Found").data(usersResponse).build());
        }

        return response.get();
    }

    private void userRoles(List<RoleData> roles, UserRole userRole) {
        RoleData userRoleData = RoleData.builder()
                .id(userRole.getRole().getId())
                .name(userRole.getRole().getName())
                .status(userRole.getRole().getStatus())
                .creationDate(userRole.getRole().getCreationDate())
                .updateDate(userRole.getRole().getUpdateDate())
                .build();

        List<RoleAccessRight> accessRights = new ArrayList<>();
        if (userRole.getRole().getStatus() != null && !userRole.getRole().getAccessRights().isEmpty()) {
            userRole.getRole().getAccessRights().forEach(accessRight -> {
                accessRights.add(RoleAccessRight.builder().name(accessRight.getName()).category(accessRight.getCategory()).subCategory(accessRight.getSubCategory()).accessRight(accessRight).build());
            });
        }

        userRoleData.setAccessRights(accessRights);

        roles.add(userRoleData);
    }

    @Bean
    private void initMaker() {

        if (userRepository.findAll().isEmpty()) {

            roleService.initRoles();

            AtomicReference<User> user = new AtomicReference<>(new User());

            roleRepository.findByName("ROLE_SUPERUSER").ifPresentOrElse(role -> {
                        user.get().setEmail(superUserEmail.trim());
                        user.get().setFirstName(superUserFirstName.trim());
                        user.get().setLastName(superUserLastName.trim());
                        user.get().setStatus(Constant.ACTIVE.name());
                        user.get().setFirstLogin(1);
                        user.get().setStaffNo("0000");
                        user.get().setPassword(passwordUtil.encode("12345678"));


                        user.set(userRepository.save(user.get()));

                        log.info(String.format("User created [ %s ]", user.get()));

                        log.info(String.format("Role Details [ %s ]", role));

                        UserRole usr = new UserRole();
                        usr.setRole(role);
                        usr.setUser(user.get());
                        usr.setCreation_date(new Timestamp(new Date().getTime()));
                        usr.setStatus(1);
                        usr.setUpdate_date(new Timestamp(new Date().getTime()));

                        userRoleRepository.save(usr);

                    }, () -> log.info("Provided role not found")
            );
        }

    }

}

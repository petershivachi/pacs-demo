package com.shivachi.pacs.demo.auth.repo;


import com.shivachi.pacs.demo.auth.model.UserRole;
import com.shivachi.pacs.demo.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    List<UserRole> findAllByUser(@NonNull User u);

    Optional<UserRole> findByUser(@NonNull User u);

    List<UserRole> findAllByUserAndStatus(@NonNull User user,  Integer s);

    Optional<UserRole> findByUser_Email(String email);

    @Query(nativeQuery = true, value = "Update USER_ROLE_CONFIG set ROLE =:role_id where USER=:user")
    void updateUserRole(Long role_id, Long user);
}

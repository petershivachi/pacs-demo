package com.shivachi.pacs.demo.auth.repo;

import com.shivachi.pacs.demo.auth.model.Role;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findByStatus(@NonNull Integer status);

    @Query(nativeQuery = true, value = "select * from CONFIG_ROLE where name = :name limit 1")
    Optional<Role> findByName(@NonNull String name);
}

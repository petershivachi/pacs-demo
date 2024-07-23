package com.shivachi.pacs.demo.auth.repo;

import com.shivachi.pacs.demo.auth.model.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(@NonNull String email);

    Optional<User> findByStaffNo(@NonNull String staffNo);

    List<User> findAllByStatus(@NonNull String status);
}

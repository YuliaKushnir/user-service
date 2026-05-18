package org.example.userservice.repository;

import org.example.userservice.data.User;
import org.example.userservice.data.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user for the given email
     */
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Boolean existsByKeycloakId(String userId);

    Optional<User> getUserById(String id);

    List<User> findAllByRole(Role role);

    Optional<User> getUserByKeycloakId(String key);
}

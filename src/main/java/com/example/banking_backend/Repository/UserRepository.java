package com.example.banking_backend.Repository;

import com.example.banking_backend.Model.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserDTO , Long> {
    Optional<UserDTO> findByUsername(String username);

    boolean existsByUsername(String username);
}

package com.example.banking_backend.Service;

import com.example.banking_backend.Exception.ResourceNotFoundException;
import com.example.banking_backend.Model.UserDTO;
import com.example.banking_backend.Repositary.UserRepositary;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AuthService {
    @Autowired
    private UserRepositary userRepositary;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDTO signup(UserDTO userDTO) {
        String username = userDTO.getUsername() == null ? "" : userDTO.getUsername().trim();
        if (username.length() < 3 || username.length() > 30) {
            throw new IllegalArgumentException("Username must be between 3 and 30 characters");
        }
        if (username.contains(" ")) {
            throw new IllegalArgumentException("Username must not contain spaces");
        }
        if (userDTO.getPassword() == null || userDTO.getPassword().length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters");
        }
        if (userRepositary.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }

        String role = userDTO.getRole() == null ? "USER" : userDTO.getRole().toUpperCase();
        if (!role.equals("USER") && !role.equals("ADMIN")) {
            throw new IllegalArgumentException("Role must be USER or ADMIN");
        }

        userDTO.setUsername(username);
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userDTO.setRole(role);

        UserDTO savedUser = userRepositary.save(userDTO);
        savedUser.setUserId(savedUser.getId());
        return userRepositary.save(savedUser);
    }

    public UserDTO getUserByUsername(String username) {
        return userRepositary.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this username: " + username));
    }
}

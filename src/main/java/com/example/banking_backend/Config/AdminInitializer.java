package com.example.banking_backend.Config;

import com.example.banking_backend.Model.UserDTO;
import com.example.banking_backend.Repositary.UserRepositary;
import com.example.banking_backend.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements CommandLineRunner {
    @Autowired
    private UserRepositary userRepositary;

    @Autowired
    private AuthService authService;

    @Value("${admin.username:admin}")
    private String adminUsername;

    @Value("${admin.password:admin123}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        if (userRepositary.existsByUsername(adminUsername)) {
            return;
        }
        UserDTO admin = new UserDTO();
        admin.setName("Admin");
        admin.setUsername(adminUsername);
        admin.setPassword(adminPassword);
        admin.setRole("ADMIN");
        authService.signup(admin);
        System.out.println("Admin user created with username: " + adminUsername);
    }
}

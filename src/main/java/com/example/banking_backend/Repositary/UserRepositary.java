package com.example.banking_backend.Repositary;

import com.example.banking_backend.Model.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepositary extends JpaRepository<UserDTO , Long> {

}

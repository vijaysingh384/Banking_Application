package com.example.banking_backend.Service;

import com.example.banking_backend.Model.UserDTO;
import com.example.banking_backend.Repositary.UserRepositary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepositary userRepositary;

    public UserDTO register(UserDTO userDTO) {
        return userRepositary.save(userDTO);
    }


    public List<UserDTO> getAllusers() {
        return userRepositary.findAll();
    }

    public UserDTO getUserById(Long id) {
        return userRepositary.findById(id).orElse(null);
    }
}

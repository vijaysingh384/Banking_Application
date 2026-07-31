package com.example.banking_backend.Controller;

import com.example.banking_backend.Model.UserDTO;
import com.example.banking_backend.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public UserDTO register(@RequestBody UserDTO userDTO){
        return userService.register(userDTO);
    }

    @GetMapping("/users")
    public List<UserDTO> getAllusers(){
        return userService.getAllusers();
    }

    @GetMapping("/users/{id}")
    public UserDTO getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }

    public UserDTO createAccount(@RequestBody UserDTO userDTO){
        return
    }
}

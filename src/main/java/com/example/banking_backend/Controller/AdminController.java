package com.example.banking_backend.Controller;

import com.example.banking_backend.Model.*;
import com.example.banking_backend.Service.AuthService;
import com.example.banking_backend.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @GetMapping("/users")
    public ResponseEntity<ResponseDTO> getAllusers(){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setStatuscode(HttpStatus.OK.value());
        responseDTO.setError(false);
        responseDTO.setMessage("User fetch Successfully");
        responseDTO.setDtos(userService.getAllusers());
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<ResponseDTO> createUser(@RequestBody UserDTO userDTO){
        ResponseDTO responseDTO = new ResponseDTO();
        UserDTO savedUser = authService.signup(userDTO);
        responseDTO.setStatuscode(HttpStatus.CREATED.value());
        responseDTO.setError(false);
        responseDTO.setMessage("User created successfully");
        responseDTO.setDtos(List.of(savedUser));
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/accounts")
    public ResponseEntity<ResponseDTO> getAllAccount(){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setStatuscode(HttpStatus.OK.value());
        responseDTO.setError(false);
        responseDTO.setMessage("Account fetch successfully");
        responseDTO.setAccountdtos(userService.getAllAccount());
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PostMapping("/accounts")
    public ResponseEntity<ResponseDTO> createAccount(@RequestBody AccountDTO accountDTO){
        ResponseDTO responseDTO = new ResponseDTO();
        AccountDTO account = userService.createAccount(accountDTO);
        responseDTO.setStatuscode(HttpStatus.CREATED.value());
        responseDTO.setError(false);
        responseDTO.setMessage("Account created successfully");
        responseDTO.setAccountdtos(List.of(account));
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/loans")
    public ResponseEntity<ResponseDTO> getAllLoans(){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setStatuscode(HttpStatus.OK.value());
        responseDTO.setError(false);
        responseDTO.setMessage("Loans fetch successfully");
        responseDTO.setLoandtos(userService.getAllLoans());
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PostMapping("/loans/{id}/approve")
    public ResponseEntity<ResponseDTO> approveLoan(@PathVariable long id){
        ResponseDTO responseDTO = new ResponseDTO();
        LoanDTO loan = userService.approveLoan(id);
        responseDTO.setStatuscode(HttpStatus.OK.value());
        responseDTO.setError(false);
        responseDTO.setMessage("Loan approved successfully");
        responseDTO.setLoandtos(List.of(loan));
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PostMapping("/loans/{id}/reject")
    public ResponseEntity<ResponseDTO> rejectLoan(@PathVariable long id){
        ResponseDTO responseDTO = new ResponseDTO();
        LoanDTO loan = userService.rejectLoan(id);
        responseDTO.setStatuscode(HttpStatus.OK.value());
        responseDTO.setError(false);
        responseDTO.setMessage("Loan rejected successfully");
        responseDTO.setLoandtos(List.of(loan));
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}

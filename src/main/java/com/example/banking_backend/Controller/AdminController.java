package com.example.banking_backend.Controller;

import com.example.banking_backend.Model.*;
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

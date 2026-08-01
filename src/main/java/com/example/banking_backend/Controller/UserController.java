package com.example.banking_backend.Controller;

import com.example.banking_backend.Model.*;
import com.example.banking_backend.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private ResponseDTO responseDTO;

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> register(@RequestBody UserDTO userDTO){
        UserDTO SavedUser = userService.register(userDTO);
        responseDTO.setStatuscode(HttpStatus.OK.value());
        responseDTO.setError(false);
        responseDTO.setMessage("User created successfully");
        ArrayList<UserDTO> dtos = new ArrayList<>();
        dtos.add(SavedUser);
        responseDTO.setDtos(dtos);
        return new ResponseEntity<ResponseDTO>(responseDTO , HttpStatus.CREATED);
    }

    @GetMapping("/users")
    public ResponseEntity<ResponseDTO> getAllusers(){
        List<UserDTO> getusers = userService.getAllusers();
        responseDTO.setStatuscode(HttpStatus.OK.value());
        responseDTO.setError(false);
        responseDTO.setMessage("User fetch Successfully");
        responseDTO.setDtos(getusers);
        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<ResponseDTO> getUserById(@PathVariable Long id) {

        UserDTO user = userService.getUserById(id);

        responseDTO.setStatuscode(HttpStatus.OK.value());
        responseDTO.setError(false);
        responseDTO.setMessage("User fetched successfully");

        ArrayList<UserDTO> users = new ArrayList<>();
        users.add(user);
        responseDTO.setDtos(users);


        return new ResponseEntity<>(responseDTO, HttpStatus.OK);

    }

    @PostMapping("/account")
    public ResponseEntity<ResponseDTO> createAccount(@RequestBody AccountDTO accountDTO){
        AccountDTO UserAccount = userService.createAccount(accountDTO);
        responseDTO.setStatuscode(HttpStatus.OK.value());
        responseDTO.setError(false);
        responseDTO.setMessage("Account created successfully");
        ArrayList<AccountDTO> accountDTOS = new ArrayList<>();
        accountDTOS.add(UserAccount);
        responseDTO.setAccountdtos(accountDTOS);
        return new ResponseEntity<ResponseDTO>(responseDTO , HttpStatus.CREATED);
    }

    @GetMapping("/accounts")
    public ResponseEntity<ResponseDTO> getAllAccount(){
        List<AccountDTO> getAccounts = userService.getAllAccount();
        responseDTO.setStatuscode(HttpStatus.OK.value());
        responseDTO.setError(false);
        responseDTO.setMessage("Account fetch successfully");
        responseDTO.setAccountdtos(getAccounts);
        return new ResponseEntity<ResponseDTO>(responseDTO,HttpStatus.OK);


    }

    @GetMapping("/account/{id}")
    public ResponseEntity<ResponseDTO> getAccountById(@PathVariable Long id) {

        AccountDTO account = userService.getAccountById(id);

        responseDTO.setStatuscode(HttpStatus.OK.value());
        responseDTO.setError(false);
        responseDTO.setMessage("Account fetched successfully");

        ArrayList<AccountDTO> accounts = new ArrayList<>();
        accounts.add(account);
        responseDTO.setAccountdtos(accounts);

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PostMapping("/depositMoney")
    public ResponseEntity<ResponseDTO> depositMoney(@RequestParam(value = "accountId") long accountId, @RequestParam("amount") double amount){
        AccountDTO depositamount = userService.depositMoney(accountId , amount);
        responseDTO.setStatuscode(HttpStatus.OK.value());
        responseDTO.setError(false);
        responseDTO.setMessage("Amount deposited successfully");
        responseDTO.setAccountdtos(List.of(depositamount));
        return new  ResponseEntity<>(responseDTO , HttpStatus.OK);
    }

    @PostMapping("/withdrawMoney")
    public ResponseEntity<ResponseDTO> withdrawMoney(@RequestParam(value = "accountId") long accountId, @RequestParam("amount") double amount){
        AccountDTO withdrawamount = userService.withdrawMoney(accountId , amount);
        responseDTO.setStatuscode(HttpStatus.OK.value());
        responseDTO.setError(false);
        responseDTO.setMessage("Amount withdraw successfully");
        responseDTO.setAccountdtos(List.of(withdrawamount));

        return new ResponseEntity<>(responseDTO , HttpStatus.OK);

    }

    @PostMapping("/loan")
    public ResponseEntity<ResponseDTO> getLoan(@RequestParam(value = "accountId") long accountId, @RequestParam("loanamount") double loanamount){
        LoanDTO getaloan = userService.getLoan(accountId , loanamount);
        responseDTO.setStatuscode(HttpStatus.OK.value());
        responseDTO.setError(false);
        responseDTO.setMessage("Loan sencened successfully");
        responseDTO.setLoandtos(List.of(getaloan));
        return new ResponseEntity<>(responseDTO , HttpStatus.OK);
    }

    public ResponseEntity<ResponseDTO> getAllLoan(){
        List<LoanDTO> loan = userService.getAllLoan();
        responseDTO.setStatuscode(HttpStatus.OK.value());
        responseDTO.setError(false);
        responseDTO.setMessage("All loan details fetch");
        responseDTO.setLoandtos(loan);
        return new ResponseEntity<>(responseDTO , HttpStatus.OK);

    }




}

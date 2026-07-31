package com.example.banking_backend.Controller;

import com.example.banking_backend.Model.AccountDTO;
import com.example.banking_backend.Model.LoanDTO;
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
        return  userService.getAllusers();
    }

    @GetMapping("/users/{id}")
    public UserDTO getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }

    @PostMapping("/account")
    public AccountDTO createAccount(@RequestBody AccountDTO accountDTO){
        return userService.createAccount(accountDTO);
    }

    @GetMapping("/accounts")
    public List<AccountDTO> getAllAccount(){
        return userService.getAllAccount();
    }

    @GetMapping("/account/{id}")
    public AccountDTO getAccountById(@PathVariable Long id){
        return userService.getAccountById(id);
    }

    @PostMapping("/depositMoney")
    public AccountDTO depositMoney(@RequestParam(value = "accountId") long accountId, @RequestParam("amount") double amount){
        return userService.depositMoney(accountId , amount);
    }

    @PostMapping("/withdrawMoney")
    public AccountDTO withdrawMoney(@RequestParam(value = "accountId") long accountId, @RequestParam("amount") double amount){
        return userService.withdrawMoney(accountId , amount);

    }

//    public LoanDTO getLoan(@RequestParam(value = "accountId") long accountId, @RequestParam("amount") double loanamount){
//        return userService.getLoan(accountId , loanamount);
//    }


}

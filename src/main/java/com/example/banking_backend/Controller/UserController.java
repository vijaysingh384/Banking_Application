package com.example.banking_backend.Controller;

import com.example.banking_backend.Model.*;
import com.example.banking_backend.Service.AuthService;
import com.example.banking_backend.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @GetMapping("/profile")
    public ResponseEntity<ResponseDTO> getProfile(){
        ResponseDTO responseDTO = new ResponseDTO();
        UserDTO user = userService.getCurrentUser();
        responseDTO.setStatuscode(HttpStatus.OK.value());
        responseDTO.setError(false);
        responseDTO.setMessage("Profile fetched successfully");
        responseDTO.setDtos(List.of(user));
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/my/accounts")
    public ResponseEntity<ResponseDTO> getMyAccounts(){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setStatuscode(HttpStatus.OK.value());
        responseDTO.setError(false);
        responseDTO.setMessage("Accounts fetched successfully");
        responseDTO.setAccountdtos(userService.getMyAccounts());
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/my/loans")
    public ResponseEntity<ResponseDTO> getMyLoans(){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setStatuscode(HttpStatus.OK.value());
        responseDTO.setError(false);
        responseDTO.setMessage("Loans fetched successfully");
        responseDTO.setLoandtos(userService.getMyLoans());
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PostMapping("/applyLoan")
    public ResponseEntity<ResponseDTO> applyLoan(@RequestParam("amount") BigDecimal amount){
        ResponseDTO responseDTO = new ResponseDTO();
        LoanDTO loan = userService.applyLoan(amount);
        responseDTO.setStatuscode(HttpStatus.CREATED.value());
        responseDTO.setError(false);
        responseDTO.setMessage("Loan applied successfully");
        responseDTO.setLoandtos(List.of(loan));
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PostMapping("/adduser")
    public ResponseEntity<ResponseDTO> register(@RequestBody UserDTO userDTO){
        ResponseDTO responseDTO = new ResponseDTO();
        userDTO.setRole("USER");
        UserDTO SavedUser = authService.signup(userDTO);
        responseDTO.setStatuscode(HttpStatus.OK.value());
        responseDTO.setError(false);
        responseDTO.setMessage("User created successfully");
        ArrayList<UserDTO> dtos = new ArrayList<>();
        dtos.add(SavedUser);
        responseDTO.setDtos(dtos);
        return new ResponseEntity<>(responseDTO , HttpStatus.CREATED);
    }

    @GetMapping("/users")
    public ResponseEntity<ResponseDTO> getAllusers(){
        ResponseDTO responseDTO = new ResponseDTO();
        List<UserDTO> getusers = userService.getAllusers();
        responseDTO.setStatuscode(HttpStatus.OK.value());
        responseDTO.setError(false);
        responseDTO.setMessage("User fetch Successfully");
        responseDTO.setDtos(getusers);
        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<ResponseDTO> getUserById(@PathVariable Long id) {
        ResponseDTO responseDTO = new ResponseDTO();
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
        ResponseDTO responseDTO = new ResponseDTO();
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
        ResponseDTO responseDTO = new ResponseDTO();
        List<AccountDTO> getAccounts = userService.getAllAccount();
        responseDTO.setStatuscode(HttpStatus.OK.value());
        responseDTO.setError(false);
        responseDTO.setMessage("Account fetch successfully");
        responseDTO.setAccountdtos(getAccounts);
        return new ResponseEntity<ResponseDTO>(responseDTO,HttpStatus.OK);


    }

    @GetMapping("/account/{id}")
    public ResponseEntity<ResponseDTO> getAccountById(@PathVariable Long id) {
        ResponseDTO responseDTO = new ResponseDTO();
        AccountDTO account = userService.getAccountById(id);

        responseDTO.setStatuscode(HttpStatus.OK.value());
        responseDTO.setError(false);
        responseDTO.setMessage("Account fetched successfully");

        ArrayList<AccountDTO> accounts = new ArrayList<>();
        accounts.add(account);
        responseDTO.setAccountdtos(accounts);

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PostMapping("/transferAmount")
    public ResponseEntity<ResponseDTO> TransferFund(@RequestParam long fromAccountId , @RequestParam long toAccountId , @RequestParam BigDecimal amount){
        ResponseDTO responseDTO = new ResponseDTO();
        userService.TransferFund(fromAccountId , toAccountId , amount);
        responseDTO.setStatuscode(HttpStatus.OK.value());
        responseDTO.setError(false);
        responseDTO.setMessage("Money transferred successfully.");
        return new ResponseEntity<>(responseDTO , HttpStatus.OK);
    }

    @PostMapping("/depositMoney")
    public ResponseEntity<ResponseDTO> depositMoney(@RequestParam(value = "accountId") long accountId, @RequestParam("amount") BigDecimal amount){
        ResponseDTO responseDTO = new ResponseDTO();
        AccountDTO depositamount = userService.depositMoney(accountId , amount);
        responseDTO.setStatuscode(HttpStatus.OK.value());
        responseDTO.setError(false);
        responseDTO.setMessage("Amount deposited successfully");
        responseDTO.setAccountdtos(List.of(depositamount));
        return new  ResponseEntity<>(responseDTO , HttpStatus.OK);
    }

    @PostMapping("/withdrawMoney")
    public ResponseEntity<ResponseDTO> withdrawMoney(@RequestParam(value = "accountId") long accountId, @RequestParam("amount") BigDecimal amount){
        ResponseDTO responseDTO = new ResponseDTO();
        AccountDTO withdrawamount = userService.withdrawMoney(accountId , amount);
        responseDTO.setStatuscode(HttpStatus.OK.value());
        responseDTO.setError(false);
        responseDTO.setMessage("Amount withdraw successfully");
        responseDTO.setAccountdtos(List.of(withdrawamount));

        return new ResponseEntity<>(responseDTO , HttpStatus.OK);

    }


    @PostMapping("/loan")
    public ResponseEntity<ResponseDTO> getLoan(@RequestParam(value = "accountId") long accountId, @RequestParam("loanamount") BigDecimal loanamount){
        ResponseDTO responseDTO = new ResponseDTO();
        LoanDTO getaloan = userService.getLoan(accountId , loanamount);
        responseDTO.setStatuscode(HttpStatus.OK.value());
        responseDTO.setError(false);
        responseDTO.setMessage("Loan sencened successfully");
        responseDTO.setLoandtos(List.of(getaloan));
        return new ResponseEntity<>(responseDTO , HttpStatus.OK);
    }

    public ResponseEntity<ResponseDTO> getAllLoan(){
        ResponseDTO responseDTO = new ResponseDTO();
        List<LoanDTO> loan = userService.getAllLoan();
        responseDTO.setStatuscode(HttpStatus.OK.value());
        responseDTO.setError(false);
        responseDTO.setMessage("All loan details fetch");
        responseDTO.setLoandtos(loan);
        return new ResponseEntity<>(responseDTO , HttpStatus.OK);

    }

    @GetMapping("/loans")
    public ResponseEntity<ResponseDTO> getAllLoans(){
        ResponseDTO responseDTO = new ResponseDTO();
        List<LoanDTO> getLoans = userService.getAllLoans();
        responseDTO.setStatuscode(HttpStatus.OK.value());
        responseDTO.setError(false);
        responseDTO.setMessage("Loans fetch successfully");
        responseDTO.setLoandtos(getLoans);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);


    }








}

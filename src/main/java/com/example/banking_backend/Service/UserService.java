package com.example.banking_backend.Service;

import com.example.banking_backend.Exception.ResourceNotFoundException;
import com.example.banking_backend.Model.*;
import com.example.banking_backend.Repositary.AccountRepositary;
import com.example.banking_backend.Repositary.LoanRepositary;
import com.example.banking_backend.Repositary.UserRepositary;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepositary userRepositary;

    @Autowired
    private AccountRepositary accountRepositary;

    @Autowired
    private LoanRepositary loanRepositary;


    public UserDTO register(UserDTO userDTO) {
        return userRepositary.save(userDTO);
    }


    public List<UserDTO> getAllusers() {
        return userRepositary.findAll();
    }

    public UserDTO getUserById(Long id) {
        return userRepositary.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found for this ID: " + id));
    }

    public AccountDTO createAccount(AccountDTO accountDTO) {
       List<AccountDTO> account = accountRepositary.findAll();
       return  accountRepositary.save(accountDTO);

    }

    public List<AccountDTO> getAllAccount() {
        return accountRepositary.findAll();
    }

    public AccountDTO getAccountById(Long id) {
        return accountRepositary.findById(id).orElseThrow(() -> new ResourceNotFoundException("No Account found for this ID: " + id));
    }



    public AccountDTO depositMoney(long accountId, double amount) {
        AccountDTO account = accountRepositary.findById(accountId).orElseThrow(() -> new ResourceNotFoundException("Account not for for this id " + accountId));
        double initialBalance = account.getBalance();
        account.setBalance(initialBalance + amount);
        return accountRepositary.save(account);

    }


    public AccountDTO withdrawMoney(long accountId, double amount) {
        AccountDTO account = accountRepositary.findById(accountId).orElseThrow(() -> new ResourceNotFoundException("Account not for for this id " + accountId));
        double initialBalance = account.getBalance();
        if (amount > initialBalance) {
            System.out.println("Withdrawal amount exceeded");
            return null;
        }
        account.setBalance(account.getBalance() - amount);
        return accountRepositary.save(account);
    }

    public LoanDTO getLoan(long accountId, double loanamount) {
        LoanDTO loan = loanRepositary.findById(accountId).orElseThrow(() -> new ResourceNotFoundException("No Loan found for this ID: " + accountId));
        loan.setSanctionAmount(loan.getSanctionAmount() + loanamount);
//        LoanDTO loan = new LoanDTO();
//        loan.setId(accountId);
//        loan.setUserId(accountId);
//        loan.setSanctionAmount(loanamount);
        return loanRepositary.save(loan);



    }

    public List<LoanDTO> getAllLoan() {
        return loanRepositary.findAll();
    }

    public List<LoanDTO> getAllLoans() {
        return loanRepositary.findAll();
    }


    public void  TransferFund(Long fromAccountId , Long toAccountId, double amount) throws Exception {
        AccountDTO sender = accountRepositary.findById(fromAccountId).orElseThrow(() -> new ResourceNotFoundException("Sender account not found"));
        AccountDTO reciver = accountRepositary.findById(toAccountId).orElseThrow(() -> new ResourceNotFoundException("Reciver account not found"));
        if(fromAccountId.equals(toAccountId)){
            throw new Exception("cannot transfer to same account");

        }
        if(amount <=0){
            throw new Exception("Amount should greater than zero");
        }

        if(sender.getBalance() < amount){
            throw new Exception("Insufficient balance");
        }

        sender.setBalance(sender.getBalance() - amount);
        reciver.setBalance(reciver.getBalance() + amount);
        accountRepositary.save(sender);
        accountRepositary.save(reciver);

    }
}

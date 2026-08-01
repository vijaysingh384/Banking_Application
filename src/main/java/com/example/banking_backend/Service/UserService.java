package com.example.banking_backend.Service;

import com.example.banking_backend.Exception.ResourceNotFoundException;
import com.example.banking_backend.Model.AccountDTO;
import com.example.banking_backend.Model.LoanDTO;
import com.example.banking_backend.Model.UserDTO;
import com.example.banking_backend.Repositary.AccountRepositary;
import com.example.banking_backend.Repositary.LoanRepositary;
import com.example.banking_backend.Repositary.UserRepositary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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
}

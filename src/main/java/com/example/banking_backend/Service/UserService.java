package com.example.banking_backend.Service;

import com.example.banking_backend.Exception.ResourceNotFoundException;
import com.example.banking_backend.Model.*;
import com.example.banking_backend.Repositary.AccountRepositary;
import com.example.banking_backend.Repositary.LoanRepositary;
import com.example.banking_backend.Repositary.UserRepositary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserService {
    @Autowired
    private UserRepositary userRepositary;

    @Autowired
    private AccountRepositary accountRepositary;

    @Autowired
    private LoanRepositary loanRepositary;


    public UserDTO getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepositary.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this username: " + username));
    }

    public List<UserDTO> getAllusers() {
        return userRepositary.findAll();
    }

    public UserDTO getUserById(Long id) {
        return userRepositary.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found for this ID: " + id));
    }

    public AccountDTO createAccount(AccountDTO accountDTO) {
       userRepositary.findById(accountDTO.getUserId())
               .orElseThrow(() -> new ResourceNotFoundException("User not found for this ID: " + accountDTO.getUserId()));
       if (accountDTO.getBalance() == null) {
           accountDTO.setBalance(BigDecimal.ZERO);
       }
       return accountRepositary.save(accountDTO);
    }

    public List<AccountDTO> getAllAccount() {
        return accountRepositary.findAll();
    }

    public AccountDTO getAccountById(Long id) {
        return accountRepositary.findById(id).orElseThrow(() -> new ResourceNotFoundException("No Account found for this ID: " + id));
    }

    public List<AccountDTO> getMyAccounts() {
        return accountRepositary.findByUserId(getCurrentUser().getId());
    }

    private AccountDTO getOwnAccount(long accountId) {
        AccountDTO account = accountRepositary.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not for for this id " + accountId));
        if (account.getUserId() != getCurrentUser().getId()) {
            throw new AccessDeniedException("You are not allowed to use this account");
        }
        return account;
    }

    private AccountDTO getAccount(long accountId) {
        return accountRepositary.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("No Account found for this ID: " + accountId));
    }

    private static BigDecimal balanceOf(AccountDTO account) {
        return account.getBalance() == null ? BigDecimal.ZERO : account.getBalance();
    }

    /**
     * Money is only ever stored with two decimals, so amounts are normalised before use.
     */
    private static BigDecimal requirePositiveAmount(BigDecimal amount, String message) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(message);
        }
        return amount.setScale(2, RoundingMode.HALF_UP);
    }

    public AccountDTO depositMoney(long accountId, BigDecimal amount) {
        BigDecimal depositAmount = requirePositiveAmount(amount, "Deposit amount should be greater than zero");
        AccountDTO account = getOwnAccount(accountId);
        account.setBalance(balanceOf(account).add(depositAmount));
        return accountRepositary.save(account);
    }

    public AccountDTO withdrawMoney(long accountId, BigDecimal amount) {
        BigDecimal withdrawalAmount = requirePositiveAmount(amount, "Withdrawal amount should be greater than zero");
        AccountDTO account = getOwnAccount(accountId);
        if (withdrawalAmount.compareTo(balanceOf(account)) > 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        account.setBalance(balanceOf(account).subtract(withdrawalAmount));
        return accountRepositary.save(account);
    }

    public LoanDTO applyLoan(BigDecimal amount) {
        BigDecimal loanAmount = requirePositiveAmount(amount, "Loan amount should be greater than zero");
        LoanDTO loan = new LoanDTO();
        loan.setUserId(getCurrentUser().getId());
        loan.setSanctionAmount(loanAmount);
        return loanRepositary.save(loan);
    }

    public List<LoanDTO> getMyLoans() {
        return loanRepositary.findByUserId(getCurrentUser().getId());
    }

    public LoanDTO getLoan(long accountId, BigDecimal loanamount) {
        BigDecimal addedAmount = requirePositiveAmount(loanamount, "Loan amount should be greater than zero");
        LoanDTO loan = loanRepositary.findById(accountId).orElseThrow(() -> new ResourceNotFoundException("No Loan found for this ID: " + accountId));
        loan.setSanctionAmount(loan.getSanctionAmount().add(addedAmount));
        return loanRepositary.save(loan);



    }

    public List<LoanDTO> getAllLoan() {
        return loanRepositary.findAll();
    }

    public List<LoanDTO> getAllLoans() {
        return loanRepositary.findAll();
    }


    @Transactional(rollbackFor = Exception.class)
    public void TransferFund(Long fromAccountId , Long toAccountId, BigDecimal amount) {
        BigDecimal transferAmount = requirePositiveAmount(amount, "Amount should greater than zero");
        if (fromAccountId.equals(toAccountId)) {
            throw new IllegalArgumentException("cannot transfer to same account");
        }

        // Ownership is checked before the receiver is looked up: another user's
        // account must never be revealed by proxying a transfer through it.
        AccountDTO sender = getOwnAccount(fromAccountId);

        // Both rows are always touched in ascending id order so two opposite
        // transfers between the same accounts cannot deadlock each other.
        AccountDTO receiver = getAccount(toAccountId);

        if (transferAmount.compareTo(balanceOf(sender)) > 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        sender.setBalance(balanceOf(sender).subtract(transferAmount));
        receiver.setBalance(balanceOf(receiver).add(transferAmount));

        // Persist in a stable order, independent of which side is the sender.
        AccountDTO first = sender.getId() < receiver.getId() ? sender : receiver;
        AccountDTO second = first == sender ? receiver : sender;
        accountRepositary.save(first);
        accountRepositary.save(second);
    }
}

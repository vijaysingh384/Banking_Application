package com.example.banking_backend.Service;

import com.example.banking_backend.Exception.ResourceNotFoundException;
import com.example.banking_backend.Model.*;
import com.example.banking_backend.Repositary.AccountRepositary;
import com.example.banking_backend.Repositary.LoanRepositary;
import com.example.banking_backend.Repositary.TransactionRepositary;
import com.example.banking_backend.Repositary.UserRepositary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
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

    @Autowired
    private TransactionRepositary transactionRepositary;


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
       // Account id, version and balance are owned by the server, never by the request body.
       accountDTO.setId(0);
       accountDTO.setVersion(0);
       accountDTO.setBalance(BigDecimal.ZERO);
       return accountRepositary.save(accountDTO);
    }

    public AccountDTO openMyAccount() {
        AccountDTO account = new AccountDTO();
        account.setUserId(getCurrentUser().getId());
        account.setBalance(BigDecimal.ZERO);
        return accountRepositary.save(account);
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

    private TransactionDTO recordTransaction(AccountDTO account, TransactionType type, BigDecimal amount,
                                             Long counterpartyAccountId, LocalDateTime timestamp) {
        TransactionDTO transaction = new TransactionDTO();
        transaction.setAccountId(account.getId());
        transaction.setType(type);
        transaction.setAmount(amount);
        transaction.setBalanceAfter(balanceOf(account));
        transaction.setTimestamp(timestamp);
        transaction.setCounterpartyAccountId(counterpartyAccountId);
        return transactionRepositary.save(transaction);
    }

    public List<TransactionDTO> getMyTransactions(long accountId) {
        AccountDTO account = getOwnAccount(accountId);
        return transactionRepositary.findByAccountIdOrderByTimestampDescIdDesc(account.getId());
    }

    public AccountDTO depositMoney(long accountId, BigDecimal amount) {
        BigDecimal depositAmount = requirePositiveAmount(amount, "Deposit amount should be greater than zero");
        AccountDTO account = getOwnAccount(accountId);
        account.setBalance(balanceOf(account).add(depositAmount));
        AccountDTO savedAccount = accountRepositary.save(account);
        recordTransaction(savedAccount, TransactionType.DEPOSIT, depositAmount, null, LocalDateTime.now());
        return savedAccount;
    }

    public AccountDTO withdrawMoney(long accountId, BigDecimal amount) {
        BigDecimal withdrawalAmount = requirePositiveAmount(amount, "Withdrawal amount should be greater than zero");
        AccountDTO account = getOwnAccount(accountId);
        if (withdrawalAmount.compareTo(balanceOf(account)) > 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        account.setBalance(balanceOf(account).subtract(withdrawalAmount));
        AccountDTO savedAccount = accountRepositary.save(account);
        recordTransaction(savedAccount, TransactionType.WITHDRAW, withdrawalAmount, null, LocalDateTime.now());
        return savedAccount;
    }

    public LoanDTO applyLoan(long accountId, BigDecimal amount) {
        BigDecimal loanAmount = requirePositiveAmount(amount, "Loan amount should be greater than zero");
        AccountDTO account = getOwnAccount(accountId);
        LoanDTO loan = new LoanDTO();
        loan.setUserId(getCurrentUser().getId());
        loan.setAccountId(account.getId());
        loan.setSanctionAmount(loanAmount);
        loan.setStatus(LoanStatus.PENDING);
        return loanRepositary.save(loan);
    }

    public List<LoanDTO> getMyLoans() {
        return loanRepositary.findByUserId(getCurrentUser().getId());
    }

    public LoanDTO approveLoan(long loanId) {
        LoanDTO loan = getPendingLoan(loanId);
        AccountDTO account = getAccount(loan.getAccountId());

        loan.setStatus(LoanStatus.APPROVED);
        account.setBalance(balanceOf(account).add(loan.getSanctionAmount()));
        accountRepositary.save(account);
        recordTransaction(account, TransactionType.DEPOSIT, loan.getSanctionAmount(), null, LocalDateTime.now());
        return loanRepositary.save(loan);
    }

    public LoanDTO rejectLoan(long loanId) {
        LoanDTO loan = getPendingLoan(loanId);
        loan.setStatus(LoanStatus.REJECTED);
        return loanRepositary.save(loan);
    }

    private LoanDTO getPendingLoan(long loanId) {
        LoanDTO loan = loanRepositary.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("No Loan found for this ID: " + loanId));
        if (loan.getStatus() != LoanStatus.PENDING) {
            throw new IllegalArgumentException("Only a pending loan can be decided, this loan is " + loan.getStatus());
        }
        return loan;
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

        // Both rows and their history entries commit or roll back together.
        LocalDateTime timestamp = LocalDateTime.now();
        recordTransaction(sender, TransactionType.TRANSFER_OUT, transferAmount, receiver.getId(), timestamp);
        recordTransaction(receiver, TransactionType.TRANSFER_IN, transferAmount, sender.getId(), timestamp);
    }
}

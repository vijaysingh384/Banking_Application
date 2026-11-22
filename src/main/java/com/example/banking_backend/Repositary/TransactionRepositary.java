package com.example.banking_backend.Repositary;

import com.example.banking_backend.Model.TransactionDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepositary extends JpaRepository<TransactionDTO, Long> {
    List<TransactionDTO> findByAccountIdOrderByTimestampDescIdDesc(long accountId);
}

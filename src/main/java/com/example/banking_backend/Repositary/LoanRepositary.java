package com.example.banking_backend.Repositary;

import com.example.banking_backend.Model.LoanDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepositary extends JpaRepository<LoanDTO , Long> {
    List<LoanDTO> findByUserId(long userId);
}

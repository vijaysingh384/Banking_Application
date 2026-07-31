package com.example.banking_backend.Repositary;

import com.example.banking_backend.Model.LoanDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepositary extends JpaRepository<LoanDTO , Long> {
}

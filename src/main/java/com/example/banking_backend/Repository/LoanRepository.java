package com.example.banking_backend.Repository;

import com.example.banking_backend.Model.LoanDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<LoanDTO , Long> {
    List<LoanDTO> findByUserId(long userId);
}

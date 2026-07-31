package com.example.banking_backend.Repositary;

import com.example.banking_backend.Model.AccountDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

@Repository
public interface AccountRepositary extends JpaRepository<AccountDTO , Long> {
}

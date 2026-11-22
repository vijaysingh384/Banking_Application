package com.example.banking_backend.Repository;

import com.example.banking_backend.Model.AccountDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<AccountDTO , Long> {
    List<AccountDTO> findByUserId(long userId);
}

package com.example.banking_backend.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table
public class AccountDTO {
    @Id
    private long id;
    private long userId;
    private double balance;
}

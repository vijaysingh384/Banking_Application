package com.example.banking_backend.Model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table
public class AccountDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long userId;
    private double balance;
}

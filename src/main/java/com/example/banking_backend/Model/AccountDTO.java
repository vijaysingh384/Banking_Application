package com.example.banking_backend.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table
public class AccountDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long userId;

    @Column(precision = 19, scale = 2)
    private BigDecimal balance;

    @Version
    private long version;
}

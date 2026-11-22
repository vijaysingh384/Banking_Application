package com.example.banking_backend.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Table
@Entity
public class LoanDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long userId;

    @Column(precision = 19, scale = 2)
    private BigDecimal sanctionAmount;

    // The account the money is credited to once an admin approves the loan.
    private long accountId;

    @Enumerated(EnumType.STRING)
    private LoanStatus status;

}

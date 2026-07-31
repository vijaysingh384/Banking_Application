package com.example.banking_backend.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Table
@Entity
public class LoanDTO {
    @Id
    private long id;
    private long userId;
    private double sanctionAmount;

}

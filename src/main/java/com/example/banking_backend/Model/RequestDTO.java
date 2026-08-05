package com.example.banking_backend.Model;

import lombok.Data;

@Data
public class RequestDTO
{
    private long fromAccountId;
    private long toAccountId;
    private double amount;
}

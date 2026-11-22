package com.example.banking_backend.Model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDTO {
    private String message;
    private boolean error;
    private int Statuscode;
    private List<UserDTO> dtos;
    private List<AccountDTO> accountdtos;
    private List<LoanDTO> loandtos;
    private List<TransactionDTO> transactiondtos;
}

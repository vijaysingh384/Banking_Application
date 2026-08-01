package com.example.banking_backend.Model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDTO {
    private String message;
    private boolean error;
    private int Statuscode;
    private List<UserDTO> dtos;
    private List<AccountDTO> accountdtos;
    private List<AmountDTO> amountdtos;
    private List<LoanDTO> loandtos;
}

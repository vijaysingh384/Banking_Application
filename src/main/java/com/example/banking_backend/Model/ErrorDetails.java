package com.example.banking_backend.Model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDetails {
    private int status;
    private String msg;
    private LocalDateTime dateTime;
    private String path;
    private Map<String, String> fieldErrors;
}

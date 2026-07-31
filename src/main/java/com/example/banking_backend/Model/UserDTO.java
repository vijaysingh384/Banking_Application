package com.example.banking_backend.Model;
import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table
public class UserDTO {
    @Id
    private Long id;
    private String name;












}

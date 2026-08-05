package com.example.banking_backend.Model;
import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table
public class UserDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long userId;
    private String name;












}

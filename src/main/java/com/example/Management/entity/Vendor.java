package com.example.Management.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Vendor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String gstNumber;
    private String address;
    private String state;
    private String contactNumber;
    private String email;
    private String panNumber;
    private String bankName;
    private String accountNumber;
    private String ifscCode;
}

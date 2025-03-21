package com.example.Management.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

    @Id
    private Long employeeId;
    private String employeeName;
    private String address;
    private Long phone;
    private String email;
    private String role;

}

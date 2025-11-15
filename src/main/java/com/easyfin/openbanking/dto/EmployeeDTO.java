package com.easyfin.openbanking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String fullName;
    private String position;
    private BigDecimal monthlySalary;
    private LocalDate hireDate;
    private String email;
    private String phone;
    private Boolean isActive;
}


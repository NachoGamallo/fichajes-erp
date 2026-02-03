package com.empresa.fichajes.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table (name = "employees")
public class Employee {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    private String position;
    private String password;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

}

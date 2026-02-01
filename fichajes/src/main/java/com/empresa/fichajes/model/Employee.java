package com.empresa.fichajes.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter @Setter
public class Employee {

    @Id
    @GeneratedValue
    private UUID id;

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String role;
    private boolean active;

    @ManyToOne
    private Department department;
    @ManyToOne
    private WorkSchedule workSchedule;

}

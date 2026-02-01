package com.empresa.fichajes.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter @Setter
public class Department {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    private String description;

}

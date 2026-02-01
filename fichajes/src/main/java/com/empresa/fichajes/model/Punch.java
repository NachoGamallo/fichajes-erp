package com.empresa.fichajes.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Getter @Setter
public class Punch {

    @Id
    @GeneratedValue
    private UUID id;


    @ManyToOne
    private Employee employee;

    private LocalDate punchDate;
    private LocalTime punchTime;

    @Enumerated(EnumType.STRING)
    private PunchType type;

    private boolean outOfSchedule;

}

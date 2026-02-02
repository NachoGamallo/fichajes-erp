package com.empresa.fichajes.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "punch")
@Getter @Setter
@NoArgsConstructor

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

    public Punch(Employee employee, PunchType type, boolean outOfSchedule) {
        this.employee = employee;
        this.type = type;
        this.outOfSchedule = outOfSchedule;
        this.punchDate = LocalDate.now();
        this.punchTime = LocalTime.now();
    }

}

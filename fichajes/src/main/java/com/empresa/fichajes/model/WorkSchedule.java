package com.empresa.fichajes.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalTime;
import java.util.UUID;

@Entity
@Getter @Setter
public class WorkSchedule {

    @Id
    @GeneratedValue
    private UUID id;

    private LocalTime startTime;
    private LocalTime endTime;
    private String workingDays;


}

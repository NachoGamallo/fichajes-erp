package com.empresa.fichajes.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "time_records")
public class TimeRecord {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "employee_id")
    private Employee employee;
    private String actionType; //Clock_in , Clock_out, Break_in, Break_out
    private LocalDateTime timestamp;

}

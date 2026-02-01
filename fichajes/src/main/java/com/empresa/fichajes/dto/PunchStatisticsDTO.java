package com.empresa.fichajes.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PunchStatisticsDTO {

    private double averageWorkedHours;
    private double averageBreakHours;
    private double averageOvertimeHours;

    private long totalDaysWorked;
    private long totalDaysAbsent;
    private long totalDaysOutOfSchedule;

}

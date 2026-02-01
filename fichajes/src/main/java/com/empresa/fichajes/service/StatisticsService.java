package com.empresa.fichajes.service;

import com.empresa.fichajes.dto.PunchStatisticsDTO;
import com.empresa.fichajes.model.Employee;
import com.empresa.fichajes.model.Punch;
import com.empresa.fichajes.model.PunchType;
import com.empresa.fichajes.repository.PunchRepository;
import com.empresa.fichajes.util.DateUtil;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

public class StatisticsService {

    private final PunchRepository repo;
    public StatisticsService(PunchRepository repo){this.repo = repo;}

    public PunchStatisticsDTO calculateStatistics(Employee employee){

        PunchStatisticsDTO dto = new PunchStatisticsDTO();

        LocalDate end = LocalDate.now();
        LocalDate start = end.minusMonths(2);

        List<Punch> punches = repo.findPunchByEmployeeAndPunchDateBetween(employee, start, end);

        long totalDaysWorked = punches.stream().map(Punch::isOutOfSchedule).distinct().count();
        long totalDaysOutOfSchedule = punches.stream().filter(Punch::isOutOfSchedule).map(Punch::getPunchDate).distinct().count();
        long totalDaysAbsent = DateUtil.countAbsentDays(employee.getWorkSchedule(), start, end , punches);
        Duration totalWorked = Duration.ZERO;
        Duration totalBreak = Duration.ZERO;
        Duration totalOvertime = Duration.ZERO;

        for (LocalDate day : punches.stream().map(Punch::getPunchDate).distinct().toList()){

            Punch checkIn = punches.stream().filter(p -> p.getType() == PunchType.CHECK_IN).findFirst().orElse(null);
            Punch checkOut = punches.stream().filter(p -> p.getType() == PunchType.CHECK_OUT).findFirst().orElse(null);
            Punch breakStart = punches.stream().filter(p -> p.getType() == PunchType.BREAK_START).findFirst().orElse(null);
            Punch breakEnd = punches.stream().filter(p -> p.getType() == PunchType.BREAK_END).findFirst().orElse(null);

            if (checkIn != null && checkOut != null){

                Duration breakDuration = Duration.ZERO;
                if (breakStart != null && breakEnd != null) breakDuration = Duration.between(breakStart.getPunchTime(),breakEnd.getPunchTime());
                Duration worked = Duration.between(checkIn.getPunchTime(), checkOut.getPunchTime());

                totalWorked = totalWorked.plus(worked);
                totalBreak = totalBreak.plus(breakDuration);

                Duration overtime = worked.minusHours(6);
                if (!overtime.isNegative()) totalOvertime = totalOvertime.plus(overtime);

            }
        }

        dto.setTotalDaysWorked(totalDaysWorked);
        dto.setTotalDaysOutOfSchedule(totalDaysOutOfSchedule);
        dto.setTotalDaysAbsent(totalDaysAbsent);

        dto.setAverageWorkedHours(totalDaysWorked > 0 ? totalWorked.toMinutes() / 60.0 / totalDaysWorked : 0);
        dto.setAverageBreakHours(totalDaysWorked > 0 ? totalBreak.toMinutes() / 60.0 / totalDaysWorked : 0);
        dto.setAverageOvertimeHours(totalDaysWorked > 0 ? totalOvertime.toMinutes() / 60.0 / totalDaysWorked : 0);

        return dto;

    }

}

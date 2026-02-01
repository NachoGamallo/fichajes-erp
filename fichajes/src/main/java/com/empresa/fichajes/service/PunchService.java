package com.empresa.fichajes.service;

import com.empresa.fichajes.model.Employee;
import com.empresa.fichajes.model.Punch;
import com.empresa.fichajes.model.PunchType;
import com.empresa.fichajes.repository.PunchRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class PunchService {

    private final PunchRepository repo;

    public PunchService(PunchRepository repo){this.repo = repo;}

    public void punch(Employee e , PunchType type){

        LocalTime now = LocalTime.now();
        boolean outOfSchedule = now.isBefore(e.getWorkSchedule().getStartTime()) || now.isAfter(e.getWorkSchedule().getEndTime());

        Punch p = new Punch();
        p.setEmployee(e);
        p.setType(type);
        p.setPunchDate(LocalDate.now());
        p.setPunchTime(now);
        p.setOutOfSchedule(outOfSchedule);

        repo.save(p);
    }

    public List<Punch> getDailyPunches( Employee emp , LocalDate date ){
        return repo.findPunchByEmployeeAndPunchDate(emp,date);
    }

    public List<Punch> getRangePunches( Employee emp, LocalDate start, LocalDate end ){
        return repo.findPunchByEmployeeAndPunchDateBetween(emp, start, end);
    }

}

package com.empresa.fichajes.service;

import com.empresa.fichajes.exception.BusinessException;
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

    public Punch registerIn(Employee employee) {

        LocalDate today = LocalDate.now();

        if (repo.existsByEmployeeAndPunchDateAndType(
                employee, today, PunchType.IN)) {
            throw new BusinessException("Employee already punched IN today");
        }

        return repo.save(new Punch(employee, PunchType.IN, false));

    }

    public Punch registerOut(Employee employee){

        LocalDate today = LocalDate.now();

        if (!repo.existsByEmployeeAndPunchDateAndType(employee,today,PunchType.IN)) throw new BusinessException("Cannot punch OUT without punched IN");
        return repo.save(new Punch(employee, PunchType.OUT, false));

    }

    public List<Punch> getDailyPunches( Employee emp , LocalDate date ){
        return repo.findPunchByEmployeeAndPunchDate(emp,date);
    }

    public List<Punch> getRangePunches( Employee emp, LocalDate start, LocalDate end ){
        return repo.findPunchByEmployeeAndPunchDateBetween(emp, start, end);
    }

}

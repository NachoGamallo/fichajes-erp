package com.empresa.fichajes.repository;

import com.empresa.fichajes.model.Employee;
import com.empresa.fichajes.model.Punch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface PunchRepository extends JpaRepository<Punch, UUID> {

    List<Punch> findPunchByEmployeeAndPunchDate(Employee employee, LocalDate punchDate);

    List<Punch> findPunchByEmployeeAndPunchDateBetween(Employee employee, LocalDate punchDateAfter, LocalDate punchDateBefore);

    List<Punch> findPunchByEmployeeAndPunchDateMonth(Employee employee, int punchDateMonth);

}

package com.empresa.fichajes.repository;

import com.empresa.fichajes.model.Employee;
import com.empresa.fichajes.model.Punch;
import com.empresa.fichajes.model.PunchType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PunchRepository extends JpaRepository<Punch, UUID> {

    List<Punch> findPunchByEmployeeAndPunchDate(Employee employee, LocalDate punchDate);

    List<Punch> findPunchByEmployeeAndPunchDateBetween(Employee employee, LocalDate start, LocalDate end);

    boolean existsByEmployeeAndPunchDateAndType(Employee employee, LocalDate punchDate, PunchType type);

    Optional<Punch> findTopByEmployeeAndPunchDateOrderByPunchDateDesc(Employee employee, LocalDate punchDate);

}

package com.empresa.fichajes.repository;

import com.empresa.fichajes.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    Employee findEmployeeByEmail(String email);

    Employee findEmployeesById(UUID id);
}

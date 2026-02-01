package com.empresa.fichajes.service;

import com.empresa.fichajes.model.Employee;
import com.empresa.fichajes.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    private EmployeeRepository employeeRepo;

    public Employee findByEmail(String email){

        return employeeRepo.findEmployeeByEmail(email);

    }

}

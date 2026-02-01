package com.empresa.fichajes.service;

import com.empresa.fichajes.model.Employee;
import com.empresa.fichajes.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {

    private final EmployeeRepository repo;

    public AuthService(EmployeeRepository repo) {this.repo = repo;}

    public Employee login(String email , String password){

        Employee e = repo.findEmployeeByEmail(email);
        if (e != null && e.getPassword().equals(password)){

            return e;

        }
        return null;

    }

}

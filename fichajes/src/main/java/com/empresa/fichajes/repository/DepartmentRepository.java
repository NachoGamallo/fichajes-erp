package com.empresa.fichajes.repository;

import com.empresa.fichajes.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DepartmentRepository extends JpaRepository<Department, UUID> {}

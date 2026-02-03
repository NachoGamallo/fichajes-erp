package com.empresa.fichajes.repository;

import com.empresa.fichajes.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department,Long> {

}

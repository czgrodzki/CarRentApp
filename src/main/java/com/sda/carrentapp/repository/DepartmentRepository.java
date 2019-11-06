package com.sda.carrentapp.repository;

import com.sda.carrentapp.entity.Department;
import com.sda.carrentapp.entity.EntityStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> getDepartmentById(Long id);

    List<Department> findAllByEntityStatus(EntityStatus entityStatus);
}

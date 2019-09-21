package com.sda.carrentapp.repository;

import com.sda.carrentapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<User, Long> {
    @Query("select e from users e where e.role = 'MANAGER' or e.role = 'EMPLOYEE'")
    List<User> getAllEmployees();
}

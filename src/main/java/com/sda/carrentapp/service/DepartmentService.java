package com.sda.carrentapp.service;

import com.sda.carrentapp.entity.Car;
import com.sda.carrentapp.entity.Department;
import com.sda.carrentapp.entity.EntityStatus;
import com.sda.carrentapp.entity.User;
import com.sda.carrentapp.exception.DepartmentNotFoundException;
import com.sda.carrentapp.repository.DepartmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor

@Service
public class DepartmentService {
    private DepartmentRepository departmentRepository;

    public List<Department> getDepartments() {
        return departmentRepository.findAllByEntityStatus(EntityStatus.ACTIVE);
    }

    public Department getDepartmentById(Long id) throws DepartmentNotFoundException {
        return departmentRepository.getDepartmentById(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with id: " + id));
    }

    public void saveDepartment(Department department) {
        department.setEntityStatus(EntityStatus.ACTIVE);
        departmentRepository.save(department);
    }

    public void deleteDepartment(Long id) throws DepartmentNotFoundException {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with id: " + id));
        department.setEntityStatus(EntityStatus.ARCHIVED);
        departmentRepository.save(department);
    }

    public List<User> getEmployeesForDepartment(Long id) throws DepartmentNotFoundException {
        return getDepartmentById(id).getUsers()
                .stream()
                .filter(e -> e.getEntityStatus().equals(EntityStatus.ACTIVE))
                .sorted(Comparator.comparing(User::getId))
                .collect(Collectors.toList());
    }

    public List<Car> getCarsForDepartment(Long id) throws DepartmentNotFoundException {
        return getDepartmentById(id).getCars()
                .stream()
                .sorted(Comparator.comparing(Car::getId))
                .collect(Collectors.toList());
    }

}
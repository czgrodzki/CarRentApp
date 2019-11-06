package com.sda.carrentapp.service;

import com.sda.carrentapp.entity.EntityStatus;
import com.sda.carrentapp.entity.User;
import com.sda.carrentapp.entity.dto.UserDTO;
import com.sda.carrentapp.entity.mapper.UserMapper;
import com.sda.carrentapp.exception.UserNotFoundException;
import com.sda.carrentapp.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final BCryptPasswordEncoder encoder;

    public List<User> getEmployees() {
        return employeeRepository.getAllEmployees();
    }

    public User getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Employee with this id doesn't exist."));
    }

    public void delete(Long id) throws UserNotFoundException {
        User employee = employeeRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Employee with this id doesn't exist."));
        employee.setEntityStatus(EntityStatus.ARCHIVED);
        employeeRepository.save(employee);
    }

    public void saveEmployee(UserDTO employeeDto) {
        employeeDto.setPassword(encoder.encode(employeeDto.getPassword()));
        User employee = UserMapper.map(employeeDto);
        employee.setEntityStatus(EntityStatus.ACTIVE);
        employeeRepository.save(employee);
    }


}

package com.sda.carrentapp.controller;

import com.sda.carrentapp.common.Message;
import com.sda.carrentapp.entity.EntityStatus;
import com.sda.carrentapp.entity.Role;
import com.sda.carrentapp.entity.User;
import com.sda.carrentapp.entity.UserDTO;
import com.sda.carrentapp.exception.UserNotFoundException;
import com.sda.carrentapp.service.DepartmentService;
import com.sda.carrentapp.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private EmployeeService employeeService;
    private DepartmentService departmentService;

    @GetMapping
    public String getEmployees(Model model) {
        model.addAttribute("employees", employeeService.getEmployees());
        return "employees";
    }

    @GetMapping("/addEmployee")
    public String addEmployeeView(Model model) {
        User employee = new User();
        model.addAttribute("roles", Stream.of(Role.EMPLOYEE, Role.MANAGER).collect(Collectors.toList()));
        model.addAttribute("departments", departmentService.getDepartments());
        model.addAttribute("employee", employee);
        return "employee-form";
    }

    @PostMapping("/saveEmployee")
    public String saveEmployee(@ModelAttribute("employee") UserDTO employeeDto) {
        employeeService.saveEmployee(employeeDto);
        return "redirect:/departments/employees/" + employeeDto.getDepartment().getId();
    }

    @GetMapping("/editEmployee/{id}")
    public String updateEmployee(@PathVariable("id") Long id, Model model) {
        model.addAttribute("roles", Stream.of(Role.EMPLOYEE, Role.MANAGER).collect(Collectors.toList()));
        model.addAttribute("departments", departmentService.getDepartments());
        model.addAttribute("employee", employeeService.getEmployeeById(id));
        return "employee-form";
    }

    @PostMapping("/deleteEmployee/{id}")
    public String deleteEmployee(@PathVariable("id") Long id, Model model) throws UserNotFoundException {
        User employeeById = employeeService.getEmployeeById(id);
        List<User> employees = employeeService.getEmployees();
        long count = employees.stream()
                .filter(e -> e.getRole().equals(Role.MANAGER))
                .filter(e -> e.getDepartment().getId().equals(employeeById.getDepartment().getId()))
                .filter(e -> e.getEntityStatus().equals(EntityStatus.ACTIVE))
                .count();
        Long departmentId = employeeById.getDepartment().getId();

        if (employeeById.getRole().equals(Role.EMPLOYEE)) {
            employeeService.delete(id);
            return "redirect:/departments/employees/" + departmentId;
        }
        if (employeeById.getRole().equals(Role.MANAGER) && count >= 2) {
            employeeService.delete(id);
            return "redirect:/departments/employees/" + departmentId;
        } else {
            model.addAttribute("url", "/departments/employees/" + departmentId);
            model.addAttribute("departmentId", departmentId);
            model.addAttribute("message",
                    new Message("Warning", "Can't delete employee. You have to hire another manager to delete "
                            + employeeById.getFirstName() + " " + employeeById.getLastName()));
            return "message";
        }
    }
}


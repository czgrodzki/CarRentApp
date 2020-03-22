package com.sda.carrentapp.controller;

import com.sda.carrentapp.entity.Department;
import com.sda.carrentapp.entity.UserBooking;
import com.sda.carrentapp.exception.DepartmentNotFoundException;
import com.sda.carrentapp.service.DepartmentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor

@Controller
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService departmentService;
    private final UserBooking userBooking;

    @GetMapping
    public String getDepartments(Model model) {
        model.addAttribute("departments", departmentService.getDepartments());
        return "departments";
    }

    @GetMapping("/addDepartment")
    public String addDepartmentView(Model model) {
        model.addAttribute("department", new Department());
        return "department-form";
    }

    @PostMapping("/saveDepartment")
    public String saveDepartment(@ModelAttribute("department") Department department) {
        departmentService.saveDepartment(department);
        return "redirect:/departments";
    }

    @GetMapping("/updateDepartment/{id}")
    public String updateDepartmentView(@PathVariable("id") Long id, Model model) throws DepartmentNotFoundException {
        model.addAttribute("department", departmentService.getDepartmentById(id));
        return "department-form";
    }

    @PostMapping("/deleteDepartment/{id}")
    public String deleteDepartment(@PathVariable("id") Long id) throws DepartmentNotFoundException {
        departmentService.deleteDepartment(id);
        return "redirect:/departments";
    }

    @GetMapping("/employees/{id}")
    public String employeesForDepartmentView(@PathVariable("id") Long id, Model model) throws DepartmentNotFoundException {
        model.addAttribute("employees", departmentService.getEmployeesForDepartment(id));
        return "employees";
    }

    @GetMapping("/cars/{id}")
    public String carsForDepartmentView(@PathVariable("id") Long id, Model model) throws DepartmentNotFoundException {
        model.addAttribute("cars", departmentService.getCarsForDepartment(id));
        model.addAttribute("userBooking", userBooking);
        return "cars";
    }


}
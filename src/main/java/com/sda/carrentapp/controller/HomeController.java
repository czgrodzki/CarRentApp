package com.sda.carrentapp.controller;

import com.sda.carrentapp.service.CarService;
import com.sda.carrentapp.service.DepartmentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@AllArgsConstructor

@Controller
@RequestMapping("/")
public class HomeController {

    private final CarService carService;
    private final DepartmentService departmentService;

    @GetMapping
    public String showHomeView() {
        return "main/index";
    }

    @GetMapping("/fleet")
    public String showFleetView(Model model) {
        model.addAttribute("cars", carService.getActiveCars());
        return "main/fleet-list";
    }

    @GetMapping("/branches")
    public String showBranchesView(Model model) {
        model.addAttribute("departments", departmentService.getDepartments());
        return "main/branches-list";
    }
}

package com.sda.carrentapp.controller;

import com.sda.carrentapp.service.CarManager;
import com.sda.carrentapp.service.DepartmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    private CarManager carManager;
    private DepartmentService departmentService;

    public HomeController(CarManager carManager, DepartmentService departmentService) {
        this.carManager = carManager;
        this.departmentService = departmentService;
    }

    @GetMapping
    public String showHomeView() {
        return "main/index";
    }

    @GetMapping("/fleet")
    public String showFleetView(Model model) {
        model.addAttribute("cars", carManager.getActiveCars());
        return "main/fleet-list";
    }

    @GetMapping("/branches")
    public String showBranchesView(Model model) {
        model.addAttribute("departments", departmentService.getDepartments());
        return "main/branches-list";
    }
}

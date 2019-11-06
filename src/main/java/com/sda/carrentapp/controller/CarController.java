package com.sda.carrentapp.controller;

import com.sda.carrentapp.entity.Car;
import com.sda.carrentapp.entity.Status;
import com.sda.carrentapp.entity.UserBooking;
import com.sda.carrentapp.entity.dto.CarDto;
import com.sda.carrentapp.service.CarManager;
import com.sda.carrentapp.service.DepartmentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor

@Controller
@RequestMapping("/cars")
public class CarController {
    private final CarManager carManager;
    private final UserBooking userBooking;
    private final DepartmentService departmentService;


    @GetMapping
    public String getCars(Model model) {
        model.addAttribute("cars", carManager.getActiveCars());
        model.addAttribute("userBooking", userBooking);
        return "cars";
    }

    @GetMapping("/addCar")
    public String addCarView(Model model) {
        model.addAttribute("statuses",Stream.of(Status.values())
                .collect(Collectors.toList()));
        model.addAttribute("departments", departmentService.getDepartments());
        model.addAttribute("car", new Car());
        return "car-form";
    }

    @PostMapping("/saveCar")
    public String saveCar(@ModelAttribute("car") CarDto carDto) {
        carManager.saveCar(carDto);
        return "redirect:/cars";
    }

    @GetMapping("/editCar/{id}")
    public String editCarView(@PathVariable Long id, Model model) {
        model.addAttribute("statuses", Stream.of(Status.values()).collect(Collectors.toList()));
        model.addAttribute("departments", departmentService.getDepartments());
        model.addAttribute("car", carManager.getCarById(id));
        return "car-form";
    }

    @PostMapping("/deleteCar/{id}")
    public String deleteCar(@PathVariable("id") Long id) {
        carManager.deleteCar(id);
        return "redirect:/cars";
    }
}

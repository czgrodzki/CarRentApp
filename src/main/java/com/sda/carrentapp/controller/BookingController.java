package com.sda.carrentapp.controller;

import com.sda.carrentapp.common.Message;
import com.sda.carrentapp.entity.Car;
import com.sda.carrentapp.entity.Role;
import com.sda.carrentapp.entity.User;
import com.sda.carrentapp.entity.UserBooking;
import com.sda.carrentapp.exception.BookingNotFoundException;
import com.sda.carrentapp.exception.RentStartDateIsNullException;
import com.sda.carrentapp.exception.UserNotFoundException;
import com.sda.carrentapp.service.BookingService;
import com.sda.carrentapp.service.CarService;
import com.sda.carrentapp.service.DepartmentService;
import com.sda.carrentapp.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.Period;

@AllArgsConstructor

@Controller
public class BookingController {

    private final BookingService bookingService;
    private final CarService carService;
    private final DepartmentService departmentService;
    private final UserService userService;

    @GetMapping("/booking/allBookings")
    public String bookingsView(Model model) {
        model.addAttribute("booked", bookingService.getBookings());
        return "bookings";
    }


    @GetMapping("/booking/selectDateAndLocation")
    public String bookingView(Model model) {
        model.addAttribute("userBooking", new UserBooking());
        model.addAttribute("departments", departmentService.getDepartments());
        return "booking";
    }

    @PostMapping("/booking/selectCar")
    public String carView(@ModelAttribute("userBooking") UserBooking userBooking, Model model) {
        model.addAttribute("days", Period.between(userBooking.getStartDate(), userBooking.getEndDate()).getDays());
        model.addAttribute("cars", carService.getCarsByRentDepAndDateAndStatus(userBooking.getStartDate(), userBooking.getEndDate(), userBooking.getRentDepartment()));
        model.addAttribute("userBooking", userBooking);
        return "cars";
    }

    @PostMapping("/booking/selectCar/{id}")
    public String addCarToBooking(@PathVariable Long id, @ModelAttribute("userBooking") UserBooking userBooking, Model model) throws UserNotFoundException {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByUserName(userName);

        Car selectedCar = carService.getCarById(id);
        userBooking.setCar(selectedCar);
        bookingService.addBooking(userBooking);

        if (user.getRole().equals(Role.USER)) {
            bookingsViewModelAttributes(model);
            return "redirect:/userPanel/bookings";
        } else {
            return "redirect:/booking/allBookings";
        }
    }

    @PostMapping("/booking/delete/{id}")
    public String deleteBooking(@PathVariable Long id) throws BookingNotFoundException {
        bookingService.deleteBooking(id);
        return "redirect:/booking/allBookings";
    }

    @GetMapping("/booking/update/{id}")
    public String updateBooking(@PathVariable Long id, Model model) throws BookingNotFoundException {
        bookingService.deleteBooking(id);
        model.addAttribute("userBooking", bookingService.getBookingById(id));
        model.addAttribute("departments", departmentService.getDepartments());
        return "booking";
    }

    @PostMapping("/booking/activate/{id}")
    public String activateRent(@PathVariable Long id) throws BookingNotFoundException {
        bookingService.acceptRent(id);
        return "redirect:/booking/allBookings";
    }

    @PostMapping("/booking/return/{id}")
    public String returnCar(@PathVariable Long id, Model model) throws BookingNotFoundException {
        try {
            bookingService.returnCar(id);
        } catch (RentStartDateIsNullException e) {
            e.printStackTrace();
            model.addAttribute("url", "/booking/allBookings");
            model.addAttribute("message",
                    new Message("Warning", "Rent has not been started yet"));
            return "message";
        }
        return "redirect:/booking/allBookings";
    }

    private void bookingsViewModelAttributes(Model model) throws UserNotFoundException {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("booked", bookingService.getAllBookingsByUserName(userName));
        model.addAttribute("user", userService.getUserByUserName(userName));
        model.addAttribute("username", userName);
    }
}

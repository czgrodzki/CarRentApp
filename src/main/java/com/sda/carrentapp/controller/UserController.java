package com.sda.carrentapp.controller;

import com.sda.carrentapp.entity.Role;
import com.sda.carrentapp.entity.User;
import com.sda.carrentapp.entity.dto.UserDTO;
import com.sda.carrentapp.exception.UserNotFoundException;
import com.sda.carrentapp.service.BookingService;
import com.sda.carrentapp.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final BookingService bookingService;

    @GetMapping
    public String getUsers(Model model) {
        model.addAttribute("users", userService.getUsers());
        return "users";
    }

    @GetMapping("/addUser")
    public String addUserView(Model model) {
        model.addAttribute("user", new User());
        return "user-form";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("user") UserDTO userDTO, Model model) throws UserNotFoundException {
        User user = userService.getUserByUserName(userService.getLoggedInUser());
        userService.saveUser(userDTO);

        if (user.getRole().equals(Role.USER)) {
            bookingsViewModelAttributes(model);
            return "redirect:/userPanel/accountSettings";
        } else {
            return "redirect:/users";
        }
    }

    @GetMapping("/updateUser/{id}")
    public String updateUserView(@PathVariable("id") Long id, Model model) throws UserNotFoundException {
        model.addAttribute("user", userService.getUserById(id));
        return "user-form";
    }

    @PostMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") Long id) throws UserNotFoundException {
        userService.deleteUser(id);
        return "redirect:/users";
    }

    private void bookingsViewModelAttributes(Model model) throws UserNotFoundException {
        model.addAttribute("booked", bookingService.getAllBookingsByUserName(userService.getLoggedInUser()));
        model.addAttribute("user", userService.getUserByUserName(userService.getLoggedInUser()));
    }
}

package com.sda.carrentapp.controller;

import com.sda.carrentapp.entity.Role;
import com.sda.carrentapp.entity.dto.UserDTO;
import com.sda.carrentapp.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@AllArgsConstructor

@Controller
@RequestMapping("/register")
public class RegisterController {

    private final UserService userService;

    @GetMapping
    public String getRegistrations(Model model) {
        model.addAttribute("user", new UserDTO());
        return "register-form";
    }

    @PostMapping
    public String addUser(@ModelAttribute("user") @Valid UserDTO userDto, BindingResult bindResult) {

        if (userService.getOptionalUserByUserName(userDto.getUsername()).isPresent()) {
            bindResult.rejectValue("username", null, "Account with this user name already exist.");
        }

        if (bindResult.hasErrors())
            return "register-form";
        else {
            userDto.setRole(Role.USER);
            userService.saveUser(userDto);
            return "redirect:/login-form";
        }
    }
}

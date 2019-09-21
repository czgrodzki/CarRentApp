package com.sda.carrentapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/login-form")
    public String loginPage() {
        return "login-form";
    }
}

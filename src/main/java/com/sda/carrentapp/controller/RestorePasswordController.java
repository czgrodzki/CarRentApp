package com.sda.carrentapp.controller;

import com.sda.carrentapp.common.Message;
import com.sda.carrentapp.exception.EmailsAreNotEqualException;
import com.sda.carrentapp.exception.PasswordsDoNotMatchException;
import com.sda.carrentapp.exception.UserNotFoundException;
import com.sda.carrentapp.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@AllArgsConstructor
@Controller
@RequestMapping("/restorePassword")
public class RestorePasswordController {

    private final UserService userService;

    @GetMapping
    public String getRestorePasswordForm(){
        return "restore-password";
    }

    @PostMapping
    public String restorePassword(@RequestParam("userName") String userName,
                                  @RequestParam("email") String email,
                                  @RequestParam("password") String password,
                                  @RequestParam("confirmPassword") String confirmPassword,
                                  Model model){

        try {
            userService.restorePassword(userName ,email, password, confirmPassword);
        } catch (UserNotFoundException | PasswordsDoNotMatchException | EmailsAreNotEqualException e) {
            e.getMessage();
            e.printStackTrace();
            model.addAttribute("url", "/restorePassword");
            model.addAttribute("message",
                    new Message("Warning", e.getMessage()));
            return "message";
        }
        model.addAttribute("url", "/login-form");
        model.addAttribute("message",
                new Message("Success", "Password has been changed"));
        return "message";
    }
}

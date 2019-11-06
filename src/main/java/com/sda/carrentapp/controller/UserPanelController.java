package com.sda.carrentapp.controller;

import com.sda.carrentapp.common.Message;
import com.sda.carrentapp.entity.User;
import com.sda.carrentapp.exception.BookingNotFoundException;
import com.sda.carrentapp.exception.PasswordsDoNotMatchException;
import com.sda.carrentapp.exception.UserNotFoundException;
import com.sda.carrentapp.exception.WrongOldPasswordException;
import com.sda.carrentapp.service.BookingService;
import com.sda.carrentapp.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor

@Controller
@RequestMapping("/userPanel")
public class UserPanelController {

    private final UserService userService;
    private final BookingService bookingService;

    @RequestMapping("/accountSettings")
    public String showUserSettings(Model model) throws UserNotFoundException {
        model.addAttribute("user", userService.getUserByUserName(userService.getLoggedInUser()));
        model.addAttribute("username", userService.getLoggedInUser());
        return "user-form";
    }

    @RequestMapping("/bookings")
    public String showUserBookings(@ModelAttribute("user") User user, Model model) throws UserNotFoundException {
        bookingsViewModelAttributes(model);
        return "bookings";
    }

    @PostMapping("/booking/delete/{id}")
    public String deleteBooking(@PathVariable Long id, Model model) throws BookingNotFoundException, UserNotFoundException {
        bookingService.deleteBooking(id);
        bookingsViewModelAttributes(model);
        return "redirect:/userPanel/bookings";
    }

    private void bookingsViewModelAttributes(Model model) throws UserNotFoundException {
        model.addAttribute("booked", bookingService.getAllBookingsByUserName(userService.getLoggedInUser()));
        model.addAttribute("user", userService.getUserByUserName(userService.getLoggedInUser()));
        model.addAttribute("username", userService.getLoggedInUser());
    }

        @PostMapping("/changePassword")
    public String changePassword(@RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword, Model model){

        String userName = userService.getLoggedInUser();
        try {
            userService.changePassword(userName ,oldPassword, newPassword, confirmPassword);
        } catch (UserNotFoundException | PasswordsDoNotMatchException | WrongOldPasswordException e) {
            e.getMessage();
            e.printStackTrace();
            model.addAttribute("url", "/userPanel/changePassword");
            model.addAttribute("message",
                    new Message("Warning", e.getMessage()));
            return "message";
        }
            model.addAttribute("url", "/userPanel/changePassword");
            model.addAttribute("message",
                    new Message("Success", "Password has been changed"));
            return "message";
    }

    @GetMapping("/changePassword")
    public String returnSomething(){
        return "change-password";
    }

}

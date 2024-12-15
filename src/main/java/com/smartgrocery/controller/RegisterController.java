package com.smartgrocery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.smartgrocery.model.User;
import com.smartgrocery.service.UserService;

@Controller
public class RegisterController {

    @Autowired
    private UserService userService;

    // Show the registration form
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User()); // Pass an empty user object to bind the form
        return "register"; // Ensure register.html exists in src/main/resources/templates
    }

    // Handle registration form submission
    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {
        // In real scenarios, hash the password before saving
        userService.registerUser(user); // Call the service to save the user
        return "redirect:/login"; // Redirect to the login page after successful registration
    }
}

package com.smartgrocery.controller;

import com.smartgrocery.model.GroceryItem;
import com.smartgrocery.model.User;
import com.smartgrocery.service.GroceryService;
import com.smartgrocery.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private GroceryService groceryService;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String username, @RequestParam String password, Model model) {
        Optional<User> user = userService.findByUsername(username);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            List<GroceryItem> groceryItems = groceryService.getAllItems();
            model.addAttribute("groceryItems", groceryItems);
            model.addAttribute("user", user.get());
            return "groceryList";
        } else {
            model.addAttribute("error", "Invalid credentials");
            return "login";
        }
    }
}

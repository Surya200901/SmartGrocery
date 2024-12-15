package com.smartgrocery.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DefaultController {

    @GetMapping("/")
    public String defaultRedirect() {
        return "redirect:/register"; // Default entry point redirects to /register
    }
}

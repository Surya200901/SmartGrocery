package com.smartgrocery.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DefaultController {
	
	@GetMapping("/")
    public String showIndex() {
        return "index"; // Serve the index.html page
    }
	
	@GetMapping("/default-register")
	public String defaultRegisterRedirect() {
	    return "redirect:/register";
	}
    
}

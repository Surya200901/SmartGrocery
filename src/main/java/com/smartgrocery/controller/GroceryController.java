package com.smartgrocery.controller;

import com.smartgrocery.model.GroceryItem;
import com.smartgrocery.model.User;
import com.smartgrocery.service.GroceryService;
import com.smartgrocery.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class GroceryController {

    @Autowired
    private GroceryService groceryService;

    @Autowired
    private UserService userService;

    @GetMapping("/groceryList")
    public String showGroceryList(Model model, Principal principal) {
        String username = principal.getName();
        User user = userService.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("user", user);
        model.addAttribute("groceryItems", groceryService.getItemsByUser(user));
        model.addAttribute("newGroceryItem", new GroceryItem());

        return "groceryList";
    }

    @PostMapping("/addGroceryItem")
    public String addGroceryItem(@RequestParam("itemName") String itemName, @RequestParam("quantity") int quantity, Principal principal) {
        String username = principal.getName();
        User user = userService.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        GroceryItem item = new GroceryItem();
        item.setItemName(itemName);
        item.setQuantity(quantity);
        item.setUser(user);
        groceryService.addItem(item);

        return "redirect:/groceryList";
    }

    @PostMapping("/updateGroceryItem")
    public String updateGroceryItem(@RequestParam("id") Long id, @RequestParam("itemName") String itemName, @RequestParam("quantity") int quantity) {
        GroceryItem item = groceryService.getItemById(id);
        if (item != null) {
            item.setItemName(itemName);
            item.setQuantity(quantity);
            groceryService.updateItem(item);
        }
        return "redirect:/groceryList";
    }

    @GetMapping("/deleteGroceryItem/{id}")
    public String deleteGroceryItem(@PathVariable("id") Long id) {
        groceryService.deleteItem(id);
        return "redirect:/groceryList";
    }
    
    @GetMapping("/publicgroceryList")
    public String showGroceryList(Model model) {
        model.addAttribute("groceryItems", groceryService.getAllItems());
        return "groceryList";
    }
}

package com.smartgrocery.controller;

import com.smartgrocery.model.GroceryItem;
import com.smartgrocery.model.GroceryItemUpdateDTO;
import com.smartgrocery.model.User;
import com.smartgrocery.service.GroceryService;
import com.smartgrocery.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class GroceryController {

    @Autowired
    private GroceryService groceryService;

    @Autowired
    private UserService userService;

    // Display the user's grocery list
    @GetMapping("/groceryList")
    public String showGroceryList(Model model, Principal principal) {
        String username = principal.getName();
        User user = userService.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("user", user);
        model.addAttribute("groceryItems", groceryService.getItemsByUser(user));
        model.addAttribute("newGroceryItem", new GroceryItem());

        return "groceryList";
    }

    // Add a new grocery item
    @PostMapping("/addGroceryItem")
    public String addGroceryItem(@RequestParam("itemName") String itemName, 
                                 @RequestParam("quantity") String quantity, 
                                 Principal principal) {
        String username = principal.getName();
        User user = userService.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        // Extract numeric and text parts from quantity
        int numericValue = extractNumericPart(quantity);
        String unitValue = extractTextPart(quantity);

        GroceryItem item = new GroceryItem();
        item.setItemName(itemName);
        item.setQuantity(numericValue);
        item.setUnit(unitValue); // Save the unit part
        item.setUser(user);
        groceryService.addItem(item);

        return "redirect:/groceryList";
    }

    // Update an existing grocery item
    @PostMapping("/updateGroceryItem/{id}")
    public String updateGroceryItem(@PathVariable("id") Long id, 
                                    @RequestParam("itemName") String itemName, 
                                    @RequestParam("quantity") String quantity) {
        GroceryItem item = groceryService.getItemById(id);
        if (item != null) {
            int numericValue = extractNumericPart(quantity);
            String unitValue = extractTextPart(quantity);

            item.setItemName(itemName);
            item.setQuantity(numericValue);
            item.setUnit(unitValue);
            groceryService.updateItem(item);
        }
        return "redirect:/groceryList";
    }

    // Delete a grocery item
    @GetMapping("/deleteGroceryItem/{id}")
    public String deleteGroceryItem(@PathVariable("id") Long id) {
        groceryService.deleteItem(id);
        return "redirect:/groceryList";
    }

    // Publicly view the grocery list (without login)
    @GetMapping("/publicgroceryList")
    public String showPublicGroceryList(Model model) {
        model.addAttribute("groceryItems", groceryService.getAllItems());
        return "groceryList";
    }

    // Update an existing grocery item (AJAX endpoint)
    @PostMapping(value = "/updateGroceryItem/{id}", consumes = "application/json")
    @ResponseBody
    public String updateGroceryItem(@PathVariable("id") Long id, 
                                    @RequestBody GroceryItemUpdateDTO updatedItem) {
        GroceryItem existingItem = groceryService.getItemById(id);
        if (existingItem != null) {
            int numericValue = extractNumericPart(updatedItem.getQuantity());
            String unitValue = extractTextPart(updatedItem.getQuantity());

            existingItem.setItemName(updatedItem.getItemName());
            existingItem.setQuantity(numericValue);
            existingItem.setUnit(unitValue);
            groceryService.updateItem(existingItem);
            return "Item updated successfully";
        }
        return "Item not found";
    }

    // Utility method to extract the numeric part of a quantity string
    private int extractNumericPart(String quantity) {
        Pattern pattern = Pattern.compile("^(\\d+(?:\\.\\d+)?)");
        Matcher matcher = pattern.matcher(quantity);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1).replaceAll("[^0-9]", ""));  // Remove extra non-numeric characters
        }
        return 0;
    }
    
    // Utility method to extract the text part of a quantity string
    private String extractTextPart(String quantity) {
        Pattern pattern = Pattern.compile("\\d+(?:\\.\\d+)?\\s*([a-zA-Z\\s]*)$");
        Matcher matcher = pattern.matcher(quantity);
        if (matcher.find()) {
            return matcher.group(1).trim();  // Ensure the unit is trimmed but retains space before it
        }
        return "";
    }
}

package com.smartgrocery.service;

import com.smartgrocery.model.GroceryItem;
import com.smartgrocery.model.User;
import com.smartgrocery.repository.GroceryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroceryService {

    @Autowired
    private GroceryRepository groceryRepository;

    // Fetch all grocery items for a specific user
    public List<GroceryItem> getItemsByUser(User user) {
        return groceryRepository.findByUser(user);  // Make sure findByUser exists in your repository
    }

    // Get a grocery item by its ID
    public GroceryItem getItemById(Long id) {
        Optional<GroceryItem> item = groceryRepository.findById(id);
        return item.orElse(null);
    }

    // Add a new item
    public void addItem(GroceryItem item) {
        groceryRepository.save(item);
    }

    // Update an existing item
    public void updateItem(GroceryItem item) {
        groceryRepository.save(item);
    }

    // Delete an item
    public void deleteItem(Long id) {
        groceryRepository.deleteById(id);
    }
    
    public List<GroceryItem> getAllItems() {
        return groceryRepository.findAll();  // Retrieve all items from the repository
    }
}

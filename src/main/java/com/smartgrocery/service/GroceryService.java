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
    	item.updateStatus();
        groceryRepository.save(item);
    }

    // Update an existing item
    public void updateItem(GroceryItem item) {
    	item.updateStatus();
        groceryRepository.save(item);
    }

    // Delete an item
    public void deleteItem(Long id) {
        groceryRepository.deleteById(id);
    }

    // Fetch all grocery items
    public List<GroceryItem> getAllItems() {
        return groceryRepository.findAll();
    }
    
   /* 
    
 // Get items by status
    public List<GroceryItem> getLowStockItems(User user) {
        return groceryRepository.findByUserAndStatus(user, "Low Stock");
    }

    public List<GroceryItem> getOutOfStockItems(User user) {
        return groceryRepository.findByUserAndStatus(user, "Out of Stock");
    }

    public List<GroceryItem> getInStockItems(User user) {
        return groceryRepository.findByUserAndStatus(user, "In Stock");
    }
    */
//added items
 /*   
    public List<GroceryItem> getLowStockItems(User user) {
        List<GroceryItem> items = groceryRepository.findByUser(user);
        return items.stream()
                .filter(item -> item.getQuantity() != null && item.getQuantity() < 3 && item.getQuantity() > 0)
                .toList();
    }

    public List<GroceryItem> getOutOfStockItems(User user) {
        List<GroceryItem> items = groceryRepository.findByUser(user);
        return items.stream()
                .filter(item -> item.getQuantity() != null && item.getQuantity() == 0)
                .toList();
    }

    public List<GroceryItem> getInStockItems(User user) {
        List<GroceryItem> items = groceryRepository.findByUser(user);
        return items.stream()
                .filter(item -> item.getQuantity() != null && item.getQuantity() >= 3)
                .toList();
    }
*/
    
    public List<GroceryItem> getLowStockItems(User user) {
        List<GroceryItem> items = groceryRepository.findByUser(user);
        List<GroceryItem> lowStockItems = items.stream()
            .filter(item -> item.getQuantity() != null && item.getQuantity() < 3 && item.getQuantity() > 0)
            .toList();
        System.out.println("Low Stock Items: " + lowStockItems); // Debug
        return lowStockItems;
    }

    public List<GroceryItem> getOutOfStockItems(User user) {
        List<GroceryItem> items = groceryRepository.findByUser(user);
        List<GroceryItem> outOfStockItems = items.stream()
            .filter(item -> item.getQuantity() != null && item.getQuantity() == 0)
            .toList();
        System.out.println("Out of Stock Items: " + outOfStockItems); // Debug
        return outOfStockItems;
    }

    public List<GroceryItem> getInStockItems(User user) {
        List<GroceryItem> items = groceryRepository.findByUser(user);
        List<GroceryItem> inStockItems = items.stream()
            .filter(item -> item.getQuantity() != null && item.getQuantity() >= 3)
            .toList();
        System.out.println("In Stock Items: " + inStockItems); // Debug
        return inStockItems;
    }

}

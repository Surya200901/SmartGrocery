package com.smartgrocery.repository;

import com.smartgrocery.model.GroceryItem;
import com.smartgrocery.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroceryRepository extends JpaRepository<GroceryItem, Long> {
    List<GroceryItem> findByUser(User user);  // Method to fetch grocery items by user
}

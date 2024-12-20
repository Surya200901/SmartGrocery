package com.smartgrocery.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "grocery_items")
@Data
public class GroceryItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String itemName;

    private Integer quantity; // Numeric value of quantity

    private String unit; // Unit, e.g., "kg", "liters"
    
    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Getter for combined quantity with unit
    public String getQuantityWithUnit() {
        return quantity + (unit != null ? " " + unit : "");
    }
    
    @Override
    public String toString() {
        return "GroceryItem{" +
                "id=" + id +
                ", itemName='" + itemName + '\'' +
                ", quantity=" + quantity +
                ", unit='" + unit + '\'' +
                ", status='" + status + '\'' +
                // Excluding 'user' to avoid recursion
                '}';
    }
    
    public void updateStatus() {
        if (quantity == 0) {
            this.status = "Out of Stock";
        } else if (quantity < 3) {
            this.status = "Low Stock";
        } else {
            this.status = "In Stock";
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
       
}

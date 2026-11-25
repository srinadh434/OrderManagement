
package com.crio.order_management.service;

import com.crio.order_management.entity.GroceryItem;
import com.crio.order_management.repository.GroceryItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class GroceryItemService {

    private final GroceryItemRepository groceryItemRepository;

    public GroceryItemService(GroceryItemRepository groceryItemRepository) {
        this.groceryItemRepository = groceryItemRepository;
    }

    // Create grocery item
    public GroceryItem createGroceryItem(GroceryItem groceryItem) {
        return groceryItemRepository.save(groceryItem);
    }

    // Get all grocery items
    public List<GroceryItem> getAllGroceryItems() {
        return groceryItemRepository.findAll();
    }

    // Get grocery item by ID
    public GroceryItem getGroceryItemById(Long id) {
        return groceryItemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Grocery item not found with ID: " + id
                ));
    }

    // Update grocery item
    public GroceryItem updateGroceryItem(Long id, GroceryItem updatedGroceryItem) {
        GroceryItem existingGroceryItem = getGroceryItemById(id);

        existingGroceryItem.setName(updatedGroceryItem.getName());
        existingGroceryItem.setCategory(updatedGroceryItem.getCategory());
        existingGroceryItem.setPrice(updatedGroceryItem.getPrice());
        existingGroceryItem.setQuantity(updatedGroceryItem.getQuantity());

        return groceryItemRepository.save(existingGroceryItem);
    }

    // Delete grocery item
    public void deleteGroceryItem(Long id) {
        GroceryItem existingGroceryItem = getGroceryItemById(id);
        groceryItemRepository.delete(existingGroceryItem);
    }
}


package com.crio.order_management.controller;

import com.crio.order_management.entity.GroceryItem;
import com.crio.order_management.service.GroceryItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grocery-items")
public class GroceryItemController {

    private final GroceryItemService groceryItemService;

    public GroceryItemController(GroceryItemService groceryItemService) {
        this.groceryItemService = groceryItemService;
    }

    // Create grocery item
    @PostMapping
    public ResponseEntity<GroceryItem> createGroceryItem(
            @RequestBody GroceryItem groceryItem) {

        GroceryItem createdItem =
                groceryItemService.createGroceryItem(groceryItem);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdItem);
    }

    // Get all grocery items
    @GetMapping
    public ResponseEntity<List<GroceryItem>> getAllGroceryItems() {
        return ResponseEntity.ok(
                groceryItemService.getAllGroceryItems()
        );
    }

    // Get grocery item by ID
    @GetMapping("/{id}")
    public ResponseEntity<GroceryItem> getGroceryItemById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                groceryItemService.getGroceryItemById(id)
        );
    }

    // Update grocery item
    @PutMapping("/{id}")
    public ResponseEntity<GroceryItem> updateGroceryItem(
            @PathVariable Long id,
            @RequestBody GroceryItem groceryItem) {

        return ResponseEntity.ok(
                groceryItemService.updateGroceryItem(id, groceryItem)
        );
    }

    // Delete grocery item
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroceryItem(
            @PathVariable Long id) {

        groceryItemService.deleteGroceryItem(id);

        return ResponseEntity.noContent().build();
    }
}


package com.crio.order_management.service;

import com.crio.order_management.entity.GroceryItem;
import com.crio.order_management.repository.GroceryItemRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroceryItemServiceTest {

    @Mock
    private GroceryItemRepository groceryItemRepository;

    @InjectMocks
    private GroceryItemService groceryItemService;

    private GroceryItem createItem(
            String name, String category,
            String price, Integer quantity) {

        GroceryItem item = new GroceryItem();
        item.setName(name);
        item.setCategory(category);
        item.setPrice(new BigDecimal(price));
        item.setQuantity(quantity);

        return item;
    }

    @Test
    void shouldCreateGroceryItem() {
        GroceryItem item = createItem(
                "Rice", "Grains", "85.50", 20
        );

        when(groceryItemRepository.save(any(GroceryItem.class)))
                .thenReturn(item);

        GroceryItem result = groceryItemService.createGroceryItem(item);

        assertEquals("Rice", result.getName());
        assertEquals(0, new BigDecimal("85.50")
                .compareTo(result.getPrice()));

        verify(groceryItemRepository).save(item);
    }

    @Test
    void shouldReturnAllGroceryItems() {
        List<GroceryItem> items = List.of(
                createItem("Rice", "Grains", "85.50", 20),
                createItem("Milk", "Dairy", "30.00", 10)
        );

        when(groceryItemRepository.findAll()).thenReturn(items);

        List<GroceryItem> result =
                groceryItemService.getAllGroceryItems();

        assertEquals(2, result.size());
    }

    @Test
    void shouldReturnGroceryItemById() {
        GroceryItem item = createItem(
                "Rice", "Grains", "85.50", 20
        );

        when(groceryItemRepository.findById(1L))
                .thenReturn(Optional.of(item));

        GroceryItem result =
                groceryItemService.getGroceryItemById(1L);

        assertEquals("Rice", result.getName());
    }

    @Test
    void shouldThrow404WhenItemDoesNotExist() {
        when(groceryItemRepository.findById(999L))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> groceryItemService.getGroceryItemById(999L)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void shouldUpdateGroceryItem() {
        GroceryItem existing = createItem(
                "Rice", "Grains", "85.50", 20
        );

        GroceryItem updated = createItem(
                "Basmati Rice", "Grains", "100.00", 30
        );

        when(groceryItemRepository.findById(1L))
                .thenReturn(Optional.of(existing));

        when(groceryItemRepository.save(any(GroceryItem.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        GroceryItem result =
                groceryItemService.updateGroceryItem(1L, updated);

        assertEquals("Basmati Rice", result.getName());
        assertEquals(0, new BigDecimal("100.00")
                .compareTo(result.getPrice()));
        assertEquals(30, result.getQuantity());
    }

    @Test
    void shouldDeleteGroceryItem() {
        GroceryItem item = createItem(
                "Rice", "Grains", "85.50", 20
        );

        when(groceryItemRepository.findById(1L))
                .thenReturn(Optional.of(item));

        groceryItemService.deleteGroceryItem(1L);

        verify(groceryItemRepository).delete(item);
    }
}

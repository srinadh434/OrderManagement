
package com.crio.order_management.controller;

import com.crio.order_management.entity.GroceryItem;
import com.crio.order_management.service.GroceryItemService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class GroceryItemControllerTest {

    @Mock
    private GroceryItemService groceryItemService;

    @InjectMocks
    private GroceryItemController groceryItemController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(groceryItemController)
                .build();
    }

    private GroceryItem createItem() {
        GroceryItem item = new GroceryItem();
        item.setName("Basmati Rice");
        item.setCategory("Grains");
        item.setPrice(new BigDecimal("85.50"));
        item.setQuantity(20);
        return item;
    }

    @Test
    void shouldCreateGroceryItem() throws Exception {

        when(groceryItemService.createGroceryItem(any(GroceryItem.class)))
                .thenReturn(createItem());

        mockMvc.perform(post("/api/grocery-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "name": "Basmati Rice",
                            "category": "Grains",
                            "price": 85.50,
                            "quantity": 20
                        }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Basmati Rice"))
                .andExpect(jsonPath("$.category").value("Grains"))
                .andExpect(jsonPath("$.price").value(85.50))
                .andExpect(jsonPath("$.quantity").value(20));
    }

    @Test
    void shouldGetAllGroceryItems() throws Exception {

        when(groceryItemService.getAllGroceryItems())
                .thenReturn(List.of(createItem()));

        mockMvc.perform(get("/api/grocery-items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Basmati Rice"))
                .andExpect(jsonPath("$[0].price").value(85.50));
    }

    @Test
    void shouldGetGroceryItemById() throws Exception {

        when(groceryItemService.getGroceryItemById(1L))
                .thenReturn(createItem());

        mockMvc.perform(get("/api/grocery-items/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Basmati Rice"));
    }

    @Test
    void shouldUpdateGroceryItem() throws Exception {

        GroceryItem updatedItem = createItem();
        updatedItem.setName("Premium Basmati Rice");
        updatedItem.setPrice(new BigDecimal("100.00"));

        when(groceryItemService.updateGroceryItem(
                eq(1L), any(GroceryItem.class)))
                .thenReturn(updatedItem);

        mockMvc.perform(put("/api/grocery-items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "name": "Premium Basmati Rice",
                            "category": "Grains",
                            "price": 100.00,
                            "quantity": 25
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name")
                        .value("Premium Basmati Rice"))
                .andExpect(jsonPath("$.price").value(100.00));
    }

    @Test
    void shouldDeleteGroceryItem() throws Exception {

        mockMvc.perform(delete("/api/grocery-items/1"))
                .andExpect(status().isNoContent());

        verify(groceryItemService).deleteGroceryItem(1L);
    }
}

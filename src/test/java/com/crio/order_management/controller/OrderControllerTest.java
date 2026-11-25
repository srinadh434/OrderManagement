
package com.crio.order_management.controller;

import com.crio.order_management.dto.OrderRequest;
import com.crio.order_management.entity.Customer;
import com.crio.order_management.entity.GroceryItem;
import com.crio.order_management.entity.Order;
import com.crio.order_management.service.OrderService;

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
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(orderController)
                .build();
    }

    private Order createOrder() {

        Customer customer = new Customer(
                "Sree",
                "sree@example.com",
                "Hyderabad",
                "9876543210"
        );

        GroceryItem rice = new GroceryItem();
        rice.setName("Basmati Rice");
        rice.setCategory("Grains");
        rice.setPrice(new BigDecimal("85.50"));
        rice.setQuantity(20);

        Order order = new Order();
        order.setCustomer(customer);
        order.setGroceryItems(List.of(rice));
        order.setOrderDate(LocalDateTime.of(2026, 11, 26, 10, 0));
        order.setTotalPrice(new BigDecimal("85.50"));

        return order;
    }

    @Test
    void shouldCreateOrder() throws Exception {

        when(orderService.createOrder(any(OrderRequest.class)))
                .thenReturn(createOrder());

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "customerId": 1,
                            "groceryItemIds": [1]
                        }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customer.name").value("Sree"))
                .andExpect(jsonPath("$.groceryItems[0].name")
                        .value("Basmati Rice"))
                .andExpect(jsonPath("$.totalPrice").value(85.50));
    }

    @Test
    void shouldGetAllOrders() throws Exception {

        when(orderService.getAllOrders())
                .thenReturn(List.of(createOrder()));

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customer.name").value("Sree"))
                .andExpect(jsonPath("$[0].totalPrice").value(85.50));
    }

    @Test
    void shouldGetOrderById() throws Exception {

        when(orderService.getOrderById(1L))
                .thenReturn(createOrder());

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customer.name").value("Sree"))
                .andExpect(jsonPath("$.totalPrice").value(85.50));
    }

    @Test
    void shouldUpdateOrder() throws Exception {

        Order updatedOrder = createOrder();
        updatedOrder.setTotalPrice(new BigDecimal("115.50"));

        when(orderService.updateOrder(
                eq(1L), any(OrderRequest.class)))
                .thenReturn(updatedOrder);

        mockMvc.perform(put("/api/orders/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "customerId": 1,
                            "groceryItemIds": [1, 2]
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPrice").value(115.50));
    }

    @Test
    void shouldDeleteOrder() throws Exception {

        mockMvc.perform(delete("/api/orders/1"))
                .andExpect(status().isNoContent());

        verify(orderService).deleteOrder(1L);
    }
}

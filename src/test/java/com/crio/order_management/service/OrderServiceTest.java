
package com.crio.order_management.service;

import com.crio.order_management.dto.OrderRequest;
import com.crio.order_management.entity.Customer;
import com.crio.order_management.entity.GroceryItem;
import com.crio.order_management.entity.Order;
import com.crio.order_management.repository.CustomerRepository;
import com.crio.order_management.repository.GroceryItemRepository;
import com.crio.order_management.repository.OrderRepository;

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
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private GroceryItemRepository groceryItemRepository;

    @InjectMocks
    private OrderService orderService;

    private GroceryItem createItem(
            String name, String price) {

        GroceryItem item = new GroceryItem();
        item.setName(name);
        item.setPrice(new BigDecimal(price));

        return item;
    }

    @Test
    void shouldCreateOrderAndCalculateTotalPrice() {

        Customer customer = new Customer(
                "Sree", "sree@example.com",
                "Hyderabad", "9876543210"
        );

        GroceryItem rice = createItem("Rice", "85.50");
        GroceryItem milk = createItem("Milk", "30.00");

        OrderRequest request = new OrderRequest();
        request.setCustomerId(1L);
        request.setGroceryItemIds(List.of(1L, 2L));

        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));

        when(groceryItemRepository.findAllById(any()))
                .thenReturn(List.of(rice, milk));

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Order result = orderService.createOrder(request);

        assertEquals(customer, result.getCustomer());
        assertEquals(2, result.getGroceryItems().size());

        assertEquals(
                0,
                new BigDecimal("115.50")
                        .compareTo(result.getTotalPrice())
        );

        assertNotNull(result.getOrderDate());

        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void shouldThrow404WhenCustomerDoesNotExist() {

        OrderRequest request = new OrderRequest();
        request.setCustomerId(999L);
        request.setGroceryItemIds(List.of(1L));

        when(customerRepository.findById(999L))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> orderService.createOrder(request)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());

        verifyNoInteractions(groceryItemRepository);
        verifyNoInteractions(orderRepository);
    }

    @Test
    void shouldThrow404WhenGroceryItemDoesNotExist() {

        Customer customer = new Customer(
                "Sree", "sree@example.com",
                "Hyderabad", "9876543210"
        );

        OrderRequest request = new OrderRequest();
        request.setCustomerId(1L);
        request.setGroceryItemIds(List.of(1L, 2L));

        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));

        // Only one of the two requested items exists.
        when(groceryItemRepository.findAllById(any()))
                .thenReturn(List.of(createItem("Rice", "85.50")));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> orderService.createOrder(request)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());

        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void shouldReturnOrderById() {

        Order order = new Order();

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        Order result = orderService.getOrderById(1L);

        assertSame(order, result);
    }

    @Test
    void shouldThrow404WhenOrderDoesNotExist() {

        when(orderRepository.findById(999L))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> orderService.getOrderById(999L)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void shouldDeleteOrder() {

        Order order = new Order();

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        orderService.deleteOrder(1L);

        verify(orderRepository).delete(order);
    }
}

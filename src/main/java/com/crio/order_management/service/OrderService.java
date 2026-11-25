
package com.crio.order_management.service;

import com.crio.order_management.dto.OrderRequest;
import com.crio.order_management.entity.Customer;
import com.crio.order_management.entity.GroceryItem;
import com.crio.order_management.entity.Order;
import com.crio.order_management.repository.CustomerRepository;
import com.crio.order_management.repository.GroceryItemRepository;
import com.crio.order_management.repository.OrderRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final GroceryItemRepository groceryItemRepository;

    public OrderService(
            OrderRepository orderRepository,
            CustomerRepository customerRepository,
            GroceryItemRepository groceryItemRepository) {

        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.groceryItemRepository = groceryItemRepository;
    }

    // Create order
    @Transactional
    public Order createOrder(OrderRequest request) {

        // Find customer
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Customer not found with ID: " + request.getCustomerId()
                ));

        // Find grocery items
        List<Long> requestedIds = request.getGroceryItemIds()
                .stream()
                .distinct()
                .toList();

        List<GroceryItem> groceryItems =
                groceryItemRepository.findAllById(requestedIds);

        if (groceryItems.size() != requestedIds.size()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "One or more grocery items were not found"
            );
        }

        // Calculate total price
        BigDecimal totalPrice = groceryItems.stream()
                .map(GroceryItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Create order
        Order order = new Order();

        order.setCustomer(customer);
        order.setGroceryItems(groceryItems);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalPrice(totalPrice);

        return orderRepository.save(order);
    }

    // Get all orders
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // Get order by ID
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Order not found with ID: " + id
                ));
    }

    // Delete order
    public void deleteOrder(Long id) {
        Order order = getOrderById(id);
        orderRepository.delete(order);
    }


    @Transactional
    public Order updateOrder(Long id, OrderRequest request) {

        Order existingOrder = getOrderById(id);

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Customer not found with ID: " + request.getCustomerId()
                ));

        List<Long> requestedIds = request.getGroceryItemIds()
                .stream()
                .distinct()
                .toList();

        List<GroceryItem> groceryItems =
                groceryItemRepository.findAllById(requestedIds);

        if (groceryItems.size() != requestedIds.size()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "One or more grocery items were not found"
            );
        }

        BigDecimal totalPrice = groceryItems.stream()
                .map(GroceryItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        existingOrder.setCustomer(customer);
        existingOrder.setGroceryItems(groceryItems);
        existingOrder.setTotalPrice(totalPrice);

        return orderRepository.save(existingOrder);
    }

}

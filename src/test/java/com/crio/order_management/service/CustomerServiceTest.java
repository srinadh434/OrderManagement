
package com.crio.order_management.service;

import com.crio.order_management.entity.Customer;
import com.crio.order_management.repository.CustomerRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void shouldCreateCustomer() {
        Customer customer = new Customer(
                "Sree", "sree@example.com",
                "Hyderabad", "9876543210"
        );

        when(customerRepository.save(any(Customer.class)))
                .thenReturn(customer);

        Customer result = customerService.createCustomer(customer);

        assertEquals("Sree", result.getName());
        verify(customerRepository).save(customer);
    }

    @Test
    void shouldReturnAllCustomers() {
        List<Customer> customers = List.of(
                new Customer("Sree", "sree@example.com",
                        "Hyderabad", "9876543210")
        );

        when(customerRepository.findAll()).thenReturn(customers);

        List<Customer> result = customerService.getAllCustomers();

        assertEquals(1, result.size());
        assertEquals("Sree", result.get(0).getName());
    }

    @Test
    void shouldReturnCustomerById() {
        Customer customer = new Customer(
                "Sree", "sree@example.com",
                "Hyderabad", "9876543210"
        );

        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));

        Customer result = customerService.getCustomerById(1L);

        assertEquals("Sree", result.getName());
    }

    @Test
    void shouldThrow404WhenCustomerDoesNotExist() {
        when(customerRepository.findById(999L))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> customerService.getCustomerById(999L)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void shouldUpdateCustomer() {
        Customer existing = new Customer(
                "Old Name", "old@example.com",
                "Hyderabad", "9876543210"
        );

        Customer updated = new Customer(
                "New Name", "new@example.com",
                "Bangalore", "9999999999"
        );

        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(existing));

        when(customerRepository.save(any(Customer.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Customer result = customerService.updateCustomer(1L, updated);

        assertEquals("New Name", result.getName());
        assertEquals("new@example.com", result.getEmail());
        assertEquals("Bangalore", result.getAddress());
    }

    @Test
    void shouldDeleteCustomer() {
        Customer customer = new Customer(
                "Sree", "sree@example.com",
                "Hyderabad", "9876543210"
        );

        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));

        customerService.deleteCustomer(1L);

        verify(customerRepository).delete(customer);
    }
}

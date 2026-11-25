
package com.crio.order_management.controller;

import com.crio.order_management.entity.Customer;
import com.crio.order_management.service.CustomerService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(customerController)
                .build();
    }

    @Test
    void shouldCreateCustomer() throws Exception {

        Customer customer = new Customer(
                "Sree",
                "sree@example.com",
                "Hyderabad",
                "9876543210"
        );

        when(customerService.createCustomer(any(Customer.class)))
                .thenReturn(customer);

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "name": "Sree",
                            "email": "sree@example.com",
                            "address": "Hyderabad",
                            "phone": "9876543210"
                        }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Sree"))
                .andExpect(jsonPath("$.email")
                        .value("sree@example.com"));
    }

    @Test
    void shouldGetAllCustomers() throws Exception {

        Customer customer = new Customer(
                "Sree",
                "sree@example.com",
                "Hyderabad",
                "9876543210"
        );

        when(customerService.getAllCustomers())
                .thenReturn(List.of(customer));

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Sree"));
    }

    @Test
    void shouldGetCustomerById() throws Exception {

        Customer customer = new Customer(
                "Sree",
                "sree@example.com",
                "Hyderabad",
                "9876543210"
        );

        when(customerService.getCustomerById(1L))
                .thenReturn(customer);

        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Sree"));
    }

    @Test
    void shouldUpdateCustomer() throws Exception {

        Customer customer = new Customer(
                "Updated Sree",
                "updated@example.com",
                "Bangalore",
                "9999999999"
        );

        when(customerService.updateCustomer(
                eq(1L), any(Customer.class)))
                .thenReturn(customer);

        mockMvc.perform(put("/api/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "name": "Updated Sree",
                            "email": "updated@example.com",
                            "address": "Bangalore",
                            "phone": "9999999999"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name")
                        .value("Updated Sree"));
    }

    @Test
    void shouldDeleteCustomer() throws Exception {

        doNothing().when(customerService).deleteCustomer(1L);

        mockMvc.perform(delete("/api/customers/1"))
                .andExpect(status().isNoContent());

        verify(customerService).deleteCustomer(1L);
    }
}

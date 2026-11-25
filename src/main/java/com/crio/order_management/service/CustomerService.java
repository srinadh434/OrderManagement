
package com.crio.order_management.service;

import com.crio.order_management.entity.Customer;
import com.crio.order_management.repository.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // Create customer
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    // Get all customers
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    // Get customer by ID
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Customer not found with ID: " + id
                ));
    }

    // Update customer
    public Customer updateCustomer(Long id, Customer updatedCustomer) {
        Customer existingCustomer = getCustomerById(id);

        existingCustomer.setName(updatedCustomer.getName());
        existingCustomer.setEmail(updatedCustomer.getEmail());
        existingCustomer.setAddress(updatedCustomer.getAddress());
        existingCustomer.setPhone(updatedCustomer.getPhone());

        return customerRepository.save(existingCustomer);
    }

    // Delete customer
    public void deleteCustomer(Long id) {
        Customer existingCustomer = getCustomerById(id);
        customerRepository.delete(existingCustomer);
    }
}

package com.example.Management.controller;

import com.example.Management.entity.Customer;
import com.example.Management.repository.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {



    @Autowired
    private CustomerRepo customerRepo;

    @GetMapping
    public List<Customer> getAllCustomers() {
        List<Customer> customers = customerRepo.findAll();
        return customers.isEmpty() ? Collections.emptyList() : customers;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        Optional<Customer> customer = customerRepo.findById(id);
        return customer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        try {
            Customer savedCustomer = customerRepo.save(customer);
            return ResponseEntity.ok(savedCustomer);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer customerDetails) {
        Optional<Customer> customerOptional = customerRepo.findById(id);
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            customer.setName(customerDetails.getName());
            customer.setGstNumber(customerDetails.getGstNumber());
            customer.setAddress(customerDetails.getAddress());
            customer.setState(customerDetails.getState());
            Customer updatedCustomer = customerRepo.save(customer);
            return ResponseEntity.ok(updatedCustomer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long id) {
        try {
            customerRepo.deleteById(id);
            return ResponseEntity.ok("Customer deleted successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting customer");
        }
    }
}

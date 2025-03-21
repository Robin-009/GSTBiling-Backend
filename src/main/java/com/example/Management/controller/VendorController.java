package com.example.Management.controller;


import com.example.Management.entity.Vendor;
import com.example.Management.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/vendors")
public class VendorController {

    @Autowired
    private VendorRepository vendorRepository;

   @GetMapping
public List<Vendor> getAllVendors() {
    List<Vendor> vendors = vendorRepository.findAll();
    return vendors.isEmpty() ? Collections.emptyList() : vendors;
}
    @GetMapping("/{id}")
    public ResponseEntity<Vendor> getVendorById(@PathVariable Long id) {
        Optional<Vendor> vendor = vendorRepository.findById(id);
        return vendor.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Vendor> createVendor(@RequestBody Vendor vendor) {
        try {
            Vendor savedVendor = vendorRepository.save(vendor);
            return ResponseEntity.ok(savedVendor);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vendor> updateVendor(@PathVariable Long id, @RequestBody Vendor vendorDetails) {
        Optional<Vendor> vendorOptional = vendorRepository.findById(id);
        if (vendorOptional.isPresent()) {
            Vendor vendor = vendorOptional.get();
            vendor.setName(vendorDetails.getName());
            vendor.setGstNumber(vendorDetails.getGstNumber());
            vendor.setAddress(vendorDetails.getAddress());
            vendor.setState(vendorDetails.getState());
            Vendor updatedVendor = vendorRepository.save(vendor);
            return ResponseEntity.ok(updatedVendor);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVendor(@PathVariable Long id) {
        try {
            vendorRepository.deleteById(id);
            return ResponseEntity.ok("Vendor deleted successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting vendor");
        }
    }
}
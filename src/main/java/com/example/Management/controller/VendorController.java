package com.example.Management.controller;


import com.example.Management.entity.Vendor;
import com.example.Management.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/vendors")
public class VendorController {

    @Autowired
    private VendorRepository vendorRepository;

    // ✅ Get all vendors with Pagination
    @GetMapping
    public Page<Vendor> getAllVendors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return vendorRepository.findAll(pageable);
    }

    // ✅ Get Vendor by ID with Pagination
    @GetMapping("/{id}")
    public ResponseEntity<Page<Vendor>> getVendorsById(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Vendor> vendorPage = vendorRepository.findById(id, pageable);

        if (vendorPage.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(vendorPage);
    }

    // ✅ Create Vendor with Image
    @PostMapping
    public ResponseEntity<Vendor> createVendor(
            @RequestParam("name") String name,
            @RequestParam("gstNumber") String gstNumber,
            @RequestParam("address") String address,
            @RequestParam("state") String state,
            @RequestParam("contactNumber") String contactNumber,
            @RequestParam("email") String email,
            @RequestParam("panNumber") String panNumber,
            @RequestParam("bankName") String bankName,
            @RequestParam("accountNumber") String accountNumber,
            @RequestParam("ifscCode") String ifscCode,
            @RequestParam(value = "image", required = false) MultipartFile imageFile // Image upload
    ) {
        try {
            Vendor vendor = new Vendor();
            vendor.setName(name);
            vendor.setGstNumber(gstNumber);
            vendor.setAddress(address);
            vendor.setState(state);
            vendor.setContactNumber(contactNumber);
            vendor.setEmail(email);
            vendor.setPanNumber(panNumber);
            vendor.setBankName(bankName);
            vendor.setAccountNumber(accountNumber);
            vendor.setIfscCode(ifscCode);

            // ✅ Convert image to byte[] and set it
            if (imageFile != null && !imageFile.isEmpty()) {
                vendor.setImage(imageFile.getBytes());
            }

            Vendor savedVendor = vendorRepository.save(vendor);
            return ResponseEntity.ok(savedVendor);

        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
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

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getVendorImage(@PathVariable Long id) {
        return vendorRepository.findById(id)
                .map(vendor -> ResponseEntity
                        .ok()
                        .header("Content-Type", "image/jpeg")
                        .body(vendor.getImage()))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/fetch")
    public List<Vendor> getAllVendors() {
        List<Vendor> vendors = vendorRepository.findAll();
        return vendors.isEmpty() ? Collections.emptyList() : vendors;
    }
    @GetMapping("fetchbyid/{id}")
    public ResponseEntity<Vendor> getVendorById(@PathVariable Long id) {
        Optional<Vendor> vendor = vendorRepository.findById(id);
        return vendor.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}

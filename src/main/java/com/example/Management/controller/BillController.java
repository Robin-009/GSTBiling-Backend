package com.example.Management.controller;

import com.example.Management.entity.Bill;
import com.example.Management.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    @Autowired
    private BillService billService;

    // Create a new Bill
    @PostMapping
    public Bill createBill(@RequestBody Bill bill) {
        return billService.createBill(bill);
    }

    // Get all Bills
    @GetMapping
    public List<Bill> getAllBills() {
        return billService.getAllBills();
    }

    // Get a Bill by ID
    @GetMapping("/{id}")
    public Bill getBillById(@PathVariable Long id) {
        return billService.getBillById(id);
    }
    //Get bills by vendor name
    @GetMapping("/vendor/{vendorName}")
    public List<Bill> getBillsByVendorName(@PathVariable String vendorName) {
        return billService.getBillsByVendorName(vendorName);
    }
    //get bills by date
    @GetMapping("/date/{date}")
    public List<Bill> getBillsByDate(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return billService.getBillsByDate(date);
    }
    // Get all pending bills
    @GetMapping("/pending")
    public List<Bill> getPendingStatusBills() {
        return billService.getPendingStatusBills();
    }
    // Get all bills with payment "pending"
    @GetMapping("/payment/pending")
    public List<Bill> getPendingPayments() {
        return billService.getPendingPayments();
    }
    // Update a Bill
    @PutMapping("/{id}")
    public Bill updateBill(@PathVariable Long id, @RequestBody Bill billDetails) {
        return billService.updateBill(id, billDetails);
    }
    // Delete a Bill
    @DeleteMapping("/{id}")
    public String deleteBill(@PathVariable Long id) {
        boolean isDeleted = billService.deleteBill(id);
        return isDeleted ? "Bill deleted successfully." : "Bill not found.";
    }
}

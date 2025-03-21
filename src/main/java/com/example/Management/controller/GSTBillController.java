package com.example.Management.controller;

import com.example.Management.service.GSTBillService;
import com.example.Management.entity.GSTBill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/GSTbills")
public class GSTBillController {


    @Autowired
    private GSTBillService GSTBillService;

    @GetMapping
    public List<GSTBill> getAllBills() {
        return GSTBillService.getAllBills();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GSTBill> getBillById(@PathVariable Long id) {
        Optional<GSTBill> bill = GSTBillService.getBillById(id);
        return bill.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<GSTBill> createBill(@RequestBody GSTBill GSTBill) {
        try {
            GSTBill savedGSTBill = GSTBillService.createBill(GSTBill);
            return ResponseEntity.ok(savedGSTBill);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<GSTBill> updateBill(@PathVariable Long id, @RequestBody GSTBill GSTBillDetails) {
        try {
            GSTBill updatedGSTBill = GSTBillService.updateBill(id, GSTBillDetails);
            return ResponseEntity.ok(updatedGSTBill);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }


    @GetMapping("/{id}/markPaid")
    public ResponseEntity<GSTBill> markBillAsPaid(@PathVariable Long id) {
        try {
            GSTBill updatedGSTBill = GSTBillService.paidStatusMark(id);
            return ResponseEntity.ok(updatedGSTBill);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBill(@PathVariable Long id) {
        try {
            GSTBillService.deleteBill(id);
            return ResponseEntity.ok("Bill deleted successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting bill");
        }
    }
}

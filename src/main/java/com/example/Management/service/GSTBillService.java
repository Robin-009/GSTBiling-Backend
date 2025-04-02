package com.example.Management.service;

import com.example.Management.repository.GSTBillItemRepository;
import com.example.Management.repository.GSTBillRepository;
import com.example.Management.entity.GSTBill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class GSTBillService {

    @Autowired
    private GSTBillRepository GSTBillRepository;

    @Autowired
    private GSTBillItemRepository GSTBillItemRepository;

    // ✅ Get All Bills with Pagination
    public Page<GSTBill> getAllBills(Pageable pageable) {
        return GSTBillRepository.findAll(pageable);
    }

    public Optional<GSTBill> getBillById(Long id) {
        return GSTBillRepository.findById(id);
    }

    public GSTBill createBill(GSTBill gSTBill) {
        try {
            gSTBill.setPaymentStatus("PENDING");
            if (gSTBill.getGSTBillItems() != null) {
                gSTBill.getGSTBillItems().forEach(item -> item.setBill(gSTBill));
            }
            return GSTBillRepository.save(gSTBill);
        } catch (Exception e) {
            throw new RuntimeException("Error creating bill", e);
        }
    }

    public GSTBill paidStatusMark(Long id) {
        return GSTBillRepository.findById(id).map(GSTBill -> {
            GSTBill.setPaymentStatus("PAID");
            GSTBill.setPaidDate(java.time.LocalDate.now());
            return GSTBillRepository.save(GSTBill);
        }).orElseThrow(() -> new RuntimeException("Bill not found with id " + id));
    }

    public GSTBill updateBill(Long id, GSTBill GSTBillDetails) {
        return GSTBillRepository.findById(id).map(GSTBill -> {
            GSTBill.setBillDate(GSTBillDetails.getBillDate());
            GSTBill.setPaidDate(GSTBillDetails.getPaidDate());
            GSTBill.setGSTBillItems(GSTBillDetails.getGSTBillItems());
            GSTBill.setBillType(GSTBillDetails.getBillType());
            GSTBill.setDescription(GSTBillDetails.getDescription());
            GSTBill.setTotalAmount(GSTBillDetails.getTotalAmount());
            GSTBill.setGstAmount(GSTBillDetails.getGstAmount());
            GSTBill.setPaymentStatus(GSTBillDetails.getPaymentStatus());
            GSTBill.setVendor(GSTBillDetails.getVendor());
            GSTBill.setVendorName(GSTBillDetails.getVendorName());
            return GSTBillRepository.save(GSTBill);
        }).orElseThrow(() -> new RuntimeException("Bill not found with id " + id));
    }

    public void deleteBill(Long id) {
        try {
            GSTBillRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting bill", e);
        }
    }
}
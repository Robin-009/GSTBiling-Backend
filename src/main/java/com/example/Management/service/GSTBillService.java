package com.example.Management.service;

import com.example.Management.repository.GSTBillItemRepository;
import com.example.Management.repository.GSTBillRepository;
import com.example.Management.entity.GSTBill;
import org.springframework.beans.factory.annotation.Autowired;
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




    public List<GSTBill> getAllBills() {
        List<GSTBill> GSTBills = GSTBillRepository.findAll();
        return GSTBills.isEmpty() ? Collections.emptyList() : GSTBills;
    }

    public Optional<GSTBill> getBillById(Long id) {
        return GSTBillRepository.findById(id);
    }

    public GSTBill createBill(GSTBill GSTBill) {
        try {
            GSTBill.setPaymentStatus("PENDING");
            // Ensure each BillItem is linked to the Bill before saving
            if (GSTBill.getGSTBillItems() != null) {
                GSTBill.getGSTBillItems().forEach(item -> item.setBill(GSTBill));
            }
            return GSTBillRepository.save(GSTBill);
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

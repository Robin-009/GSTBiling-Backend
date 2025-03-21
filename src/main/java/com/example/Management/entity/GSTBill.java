package com.example.Management.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GSTBill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long billNumber;
    private LocalDate billDate;
    private LocalDate paidDate;

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<GSTBillItem> GSTBillItems;

    private String billType; // GST, NON_GST

    // PAID, PENDING, PARTIALLY_PAID



    private String description;
    private BigDecimal totalAmount;
    private BigDecimal gstAmount; // Manually entered
    private String paymentStatus; // PAID, PENDING, PARTIALLY_PAID

    // CASH, CHEQUE, ONLINE, UPI
    private Long vendor;
    private String vendorName;
}

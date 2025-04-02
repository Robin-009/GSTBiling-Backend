package com.example.Management.repository;


import com.example.Management.entity.GSTBill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GSTBillRepository extends JpaRepository<GSTBill, Long> {
    Page<GSTBill> findAll(Pageable pageable);
}
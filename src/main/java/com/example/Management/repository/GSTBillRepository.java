package com.example.Management.repository;


import com.example.Management.entity.GSTBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GSTBillRepository extends JpaRepository<GSTBill, Long> {
}
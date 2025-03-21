package com.example.Management.repository;

import com.example.Management.entity.GSTBillItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GSTBillItemRepository extends JpaRepository<GSTBillItem, Long> {
}
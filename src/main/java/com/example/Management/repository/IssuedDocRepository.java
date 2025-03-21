package com.example.Management.repository;

import com.example.Management.entity.IssuedDoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface IssuedDocRepository extends JpaRepository<IssuedDoc, Long> {
    Optional<IssuedDoc> findByEmployeeId(Long employeeId);
    List<IssuedDoc> findByDateOfIssue(LocalDate dateOfIssue);
    List<IssuedDoc> findByTypeOfDoc(String typeOfDoc);
    List<IssuedDoc> findByIssuedBy(String issuedBy);



}

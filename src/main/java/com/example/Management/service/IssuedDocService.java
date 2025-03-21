package com.example.Management.service;

import com.example.Management.entity.IssuedDoc;
import com.example.Management.repository.EmployeeRepository;
import com.example.Management.repository.IssuedDocRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class IssuedDocService {

    @Autowired
    private IssuedDocRepository issuedDocRepository;

    @Autowired
    private EmployeeRepository employeeRepository; // Inject EmployeeRepository

    public IssuedDoc createIssuedDoc(Long employeeId, String typeOfDoc, String issuedBy, MultipartFile file) {
        // Check if the employee exists
        if (!employeeRepository.existsById(employeeId)) {
            throw new RuntimeException("Employee with ID " + employeeId + " does not exist");
        }

        IssuedDoc issuedDoc = new IssuedDoc();
        issuedDoc.setEmployeeId(employeeId);
        issuedDoc.setTypeOfDoc(typeOfDoc);
        issuedDoc.setIssuedBy(issuedBy);
        issuedDoc.setDateOfIssue(LocalDate.now()); // Set the current date

        try {
            issuedDoc.setDoc(file.getBytes()); // Convert file to byte array
        } catch (IOException e) {
            throw new RuntimeException("Error processing file", e);
        }

        return issuedDocRepository.save(issuedDoc);
    }public List<IssuedDoc> getAllIssuedDocs() {
        return issuedDocRepository.findAll();
    }
    public Optional<IssuedDoc> getIssuedDocByEmployeeId(Long employeeId) {
        return issuedDocRepository.findByEmployeeId(employeeId);
    }
    public List<IssuedDoc> getIssuedDocsByDate(LocalDate dateOfIssue) {
        return issuedDocRepository.findByDateOfIssue(dateOfIssue);
    }
    public List<IssuedDoc> getIssuedDocsByTypeOfDoc(String typeOfDoc) {
        return issuedDocRepository.findByTypeOfDoc(typeOfDoc);
    }
    public List<IssuedDoc> getIssuedDocsByIssuedBy(String issuedBy) {
        return issuedDocRepository.findByIssuedBy(issuedBy);
    }
    public Optional<IssuedDoc> getIssuedDocById(Long id) {
        return issuedDocRepository.findById(id);
    }
    public IssuedDoc updateIssuedDoc(Long id, Long employeeId, String typeOfDoc, String issuedBy,
                                     LocalDate dateOfIssue, MultipartFile file) {
        IssuedDoc existingDoc = issuedDocRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found with ID: " + id));

        // Update fields if provided
        if (employeeId != null) existingDoc.setEmployeeId(employeeId);
        if (typeOfDoc != null) existingDoc.setTypeOfDoc(typeOfDoc);
        if (issuedBy != null) existingDoc.setIssuedBy(issuedBy);
        if (dateOfIssue != null) existingDoc.setDateOfIssue(dateOfIssue);

        // Update the document only if a new file is provided
        if (file != null && !file.isEmpty()) {
            try {
                existingDoc.setDoc(file.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Error updating the document file", e);
            }
        }

        return issuedDocRepository.save(existingDoc);
    }

    public void deleteIssuedDoc(Long id) {
        issuedDocRepository.deleteById(id);
    }
}
package com.example.Management.service;

import com.example.Management.dto.IssuedDocDTO;
import com.example.Management.entity.Employee;
import com.example.Management.entity.IssuedDoc;
import com.example.Management.repository.EmployeeRepository;
import com.example.Management.repository.IssuedDocRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    }

    public Page<IssuedDocDTO> getAllIssuedDocs(Pageable pageable) {
        Page<IssuedDoc> issuedDocs = issuedDocRepository.findAll(pageable);

        return issuedDocs.map(doc -> {
            Optional<Employee> employee = employeeRepository.findById(doc.getEmployeeId());
            return new IssuedDocDTO(
                    doc.getId(),
                    doc.getEmployeeId(),
                    employee.map(Employee::getEmployeeName).orElse("N/A"),
                    employee.map(Employee::getRole).orElse("N/A"),
                    doc.getTypeOfDoc(),
                    doc.getDateOfIssue(),
                    doc.getIssuedBy(),
                    doc.getDoc()
            );
        });
    }


    public Optional<IssuedDoc> getIssuedDocByEmployeeId(Long employeeId) {
        return issuedDocRepository.findByEmployeeId(employeeId);
    }


//    public List<IssuedDoc> getIssuedDocsByDate(LocalDate dateOfIssue) {
//        return issuedDocRepository.findByDateOfIssue(dateOfIssue);
//    }
//    public List<IssuedDoc> getIssuedDocsByTypeOfDoc(String typeOfDoc) {
//        return issuedDocRepository.findByTypeOfDoc(typeOfDoc);
//    }
//    public List<IssuedDoc> getIssuedDocsByIssuedBy(String issuedBy) {
//        return issuedDocRepository.findByIssuedBy(issuedBy);
//    }


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


//    public Page<IssuedDoc> getAllIssuedDocs(Pageable pageable) {
//        return issuedDocRepository.findAll(pageable);
//    }

    public Page<IssuedDoc> getIssuedDocsByEmployeeId(Long employeeId, Pageable pageable) {
        return issuedDocRepository.findByEmployeeId(employeeId, pageable);
    }

    public Page<IssuedDoc> getIssuedDocsByDate(LocalDate dateOfIssue, Pageable pageable) {
        return issuedDocRepository.findByDateOfIssue(dateOfIssue, pageable);
    }

    public Page<IssuedDoc> getIssuedDocsByTypeOfDoc(String typeOfDoc, Pageable pageable) {
        return issuedDocRepository.findByTypeOfDoc(typeOfDoc, pageable);
    }

    public Page<IssuedDoc> getIssuedDocsByIssuedBy(String issuedBy, Pageable pageable) {
        return issuedDocRepository.findByIssuedBy(issuedBy, pageable);
    }
}
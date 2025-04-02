package com.example.Management.controller;

import com.example.Management.dto.IssuedDocDTO;
import com.example.Management.entity.Employee;
import com.example.Management.entity.IssuedDoc;
import com.example.Management.repository.EmployeeRepository;
import com.example.Management.repository.IssuedDocRepository;
import com.example.Management.service.IssuedDocService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/issued-docs")
public class IssuedDocController {

    @Autowired
    private IssuedDocService issuedDocService;
    @Autowired
    private IssuedDocRepository issuedDocRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @PostMapping
    public IssuedDoc createIssuedDoc(
            @RequestParam Long employeeId,
            @RequestParam String typeOfDoc,
            @RequestParam String issuedBy,
            @RequestParam("file") MultipartFile file
    ) {
        return issuedDocService.createIssuedDoc(employeeId, typeOfDoc, issuedBy, file);
    }

//    @GetMapping
//    public List<IssuedDoc> getAllIssuedDocs() {
//        return issuedDocService.getAllIssuedDocs();
//    }




//    @GetMapping("/getall")
//    public List<IssuedDoc> getAlllIssuedDocs() {
//        return issuedDocService.getAllIssuedDocs();
//    }


//    @GetMapping("/by-date")
//    public List<IssuedDoc> getIssuedDocsByDate(
//            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfIssue) {
//        return issuedDocService.getIssuedDocsByDate(dateOfIssue);
//    }

//    @GetMapping("/by-type")
//    public List<IssuedDoc> getIssuedDocsByTypeOfDoc(@RequestParam String typeOfDoc) {
//        return issuedDocService.getIssuedDocsByTypeOfDoc(typeOfDoc);
//    }

//    @GetMapping("/by-issuedby")
//    public List<IssuedDoc> getIssuedDocsByIssuedBy(@RequestParam String issuedBy) {
//        return issuedDocService.getIssuedDocsByIssuedBy(issuedBy);
//    }

    @PutMapping("/{id}")
    public IssuedDoc updateIssuedDoc(
            @PathVariable Long id,
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = false) String typeOfDoc,
            @RequestParam(required = false) String issuedBy,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfIssue,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) {
        return issuedDocService.updateIssuedDoc(id, employeeId, typeOfDoc, issuedBy, dateOfIssue, file);
    }

    @DeleteMapping("/{id}")
    public void deleteIssuedDoc(@PathVariable Long id) {
        issuedDocService.deleteIssuedDoc(id);
    }

    @GetMapping("/download/{id}")
    public void downloadIssuedDoc(@PathVariable Long id, HttpServletResponse response) {
        IssuedDoc issuedDoc = issuedDocService.getIssuedDocById(id)
                .orElseThrow(() -> new RuntimeException("Document not found with ID: " + id));

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=document_" + id + ".pdf");  // Adjust extension as needed

        try (OutputStream outputStream = response.getOutputStream()) {
            outputStream.write(issuedDoc.getDoc());
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException("Error while downloading the document", e);
        }
    }

    @GetMapping("/view/{id}")
    public void viewIssuedDoc(@PathVariable Long id, HttpServletResponse response) {
        IssuedDoc issuedDoc = issuedDocService.getIssuedDocById(id)
                .orElseThrow(() -> new RuntimeException("Document not found with ID: " + id));
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=document_" + id + ".pdf");
        try (OutputStream outputStream = response.getOutputStream()) {
            outputStream.write(issuedDoc.getDoc());
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException("Error while displaying the document", e);
        }
    }

    @GetMapping("/getall")
    public Page<IssuedDocDTO> getAllIssuedDocs(Pageable pageable) {
        Page<IssuedDoc> issuedDocs = issuedDocRepository.findAll(pageable);

        return issuedDocs.map(doc -> {
            Optional<Employee> employee = employeeRepository.findById(doc.getEmployeeId());
            return new IssuedDocDTO(
                    doc.getId(),
                    doc.getEmployeeId(),
                    employee.map(Employee::getEmployeeName).orElse("N/A"), // Now matches DTO
                    employee.map(Employee::getRole).orElse("N/A"), // Now matches DTO
                    doc.getTypeOfDoc(),
                    doc.getDateOfIssue(),
                    doc.getIssuedBy(),
                    doc.getDoc()
            );
        });
    }


    @GetMapping("/employee/{employeeId}")
    public Page<IssuedDoc> getIssuedDocsByEmployeeId(@PathVariable Long employeeId, Pageable pageable) {
        return issuedDocService.getIssuedDocsByEmployeeId(employeeId, pageable);
    }

    @GetMapping("/by-date")
    public Page<IssuedDoc> getIssuedDocsByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfIssue,
            Pageable pageable) {
        return issuedDocService.getIssuedDocsByDate(dateOfIssue, pageable);
    }

    @GetMapping("/by-type")
    public Page<IssuedDoc> getIssuedDocsByTypeOfDoc(@RequestParam String typeOfDoc, Pageable pageable) {
        return issuedDocService.getIssuedDocsByTypeOfDoc(typeOfDoc, pageable);
    }

    @GetMapping("/by-issuedby")
    public Page<IssuedDoc> getIssuedDocsByIssuedBy(@RequestParam String issuedBy, Pageable pageable) {
        return issuedDocService.getIssuedDocsByIssuedBy(issuedBy, pageable);
    }
}
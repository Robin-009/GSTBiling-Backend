package com.example.Management.controller;

import com.example.Management.entity.IssuedDoc;
import com.example.Management.service.IssuedDocService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/issued-docs")
public class IssuedDocController {

    @Autowired
    private IssuedDocService issuedDocService;
    @PostMapping
    public IssuedDoc createIssuedDoc(
            @RequestParam Long employeeId,
            @RequestParam String typeOfDoc,
            @RequestParam String issuedBy,
            @RequestParam("file") MultipartFile file
    ) {
        return issuedDocService.createIssuedDoc(employeeId, typeOfDoc, issuedBy, file);
    }

    @GetMapping
    public List<IssuedDoc> getAllIssuedDocs() {
        return issuedDocService.getAllIssuedDocs();
    }

    @GetMapping("employee/{employeeId}")
    public IssuedDoc getIssuedDocByEmployeeId(@PathVariable Long employeeId) {
        return issuedDocService.getIssuedDocByEmployeeId(employeeId)
                .orElse(null);  // Returns null if no document is found
    }

    @GetMapping("/by-date")
    public List<IssuedDoc> getIssuedDocsByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfIssue) {
        return issuedDocService.getIssuedDocsByDate(dateOfIssue);
    }

    @GetMapping("/by-type")
    public List<IssuedDoc> getIssuedDocsByTypeOfDoc(@RequestParam String typeOfDoc) {
        return issuedDocService.getIssuedDocsByTypeOfDoc(typeOfDoc);
    }

    @GetMapping("/by-issuedby")
    public List<IssuedDoc> getIssuedDocsByIssuedBy(@RequestParam String issuedBy) {
        return issuedDocService.getIssuedDocsByIssuedBy(issuedBy);
    }

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
}
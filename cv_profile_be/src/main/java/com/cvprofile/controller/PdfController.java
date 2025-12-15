package com.cvprofile.controller;

import com.cvprofile.service.PdfExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pdf")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PdfController {
    
    private final PdfExportService pdfExportService;
    
    @GetMapping("/export/{userId}")
    public ResponseEntity<byte[]> exportCvToPdf(@PathVariable Long userId) {
        try {
            byte[] pdfBytes = pdfExportService.generateCvPdf(userId);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "CV_Profile_" + userId + ".pdf");
            headers.setContentLength(pdfBytes.length);
            
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

package com.field2fork.controller;

import com.field2fork.dtos.SalesReportDTO;
import com.field2fork.service.SalesReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sales-report")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class SalesReportController {

    private final SalesReportService salesReportService;

    @GetMapping("/{sellerId}")
    public ResponseEntity<SalesReportDTO> getSalesReport(@PathVariable Long sellerId) {
        SalesReportDTO report = salesReportService.getSalesReportForSeller(sellerId);
        return ResponseEntity.ok(report);
    }
}

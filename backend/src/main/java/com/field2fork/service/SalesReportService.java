package com.field2fork.service;

import com.field2fork.dtos.SalesReportDTO;

public interface SalesReportService {
    SalesReportDTO getSalesReportForSeller(Long sellerId);
}

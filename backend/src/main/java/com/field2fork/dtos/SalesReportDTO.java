package com.field2fork.dtos;

import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalesReportDTO {
    private Long totalOrders;         // Count of distinct orders that include the seller's products
    private BigDecimal totalRevenue;  // Sum of (price * quantity) for seller's products
    private Long totalItemsSold;      // Sum of quantities sold
    private List<ProductSalesDTO> productSales;  // Breakdown per product
}

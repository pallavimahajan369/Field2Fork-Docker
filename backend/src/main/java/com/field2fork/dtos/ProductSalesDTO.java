package com.field2fork.dtos;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSalesDTO {
    private Long productId;
    private String productName;
    private Long orderCount;      // Count of distinct orders for this product
    private Long itemsSold;       // Total quantity sold for this product
    private BigDecimal revenue;   // Total revenue for this product
}

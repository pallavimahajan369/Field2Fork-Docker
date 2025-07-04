package com.field2fork.service;

import com.field2fork.dao.OrderItemDao;
import com.field2fork.dtos.ProductSalesDTO;
import com.field2fork.dtos.SalesReportDTO;
import com.field2fork.pojos.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalesReportServiceImpl implements SalesReportService {

    private final OrderItemDao orderItemRepository;

    @Override
    public SalesReportDTO getSalesReportForSeller(Long sellerId) {
        // Fetch order items where the product's seller (user) equals the provided sellerId
        List<OrderItem> orderItems = orderItemRepository.findByProductUserId(sellerId);

        if (orderItems.isEmpty()) {
            return new SalesReportDTO(0L, BigDecimal.ZERO, 0L, Collections.emptyList());
        }

        // Total items sold: sum of quantities from all order items
        long totalItemsSold = orderItems.stream()
                .mapToLong(OrderItem::getQuantity)
                .sum();

        // Total revenue: sum of (price * quantity) for each order item
        BigDecimal totalRevenue = orderItems.stream()
                .map(oi -> oi.getPrice().multiply(BigDecimal.valueOf(oi.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Total orders: count distinct orders that include these order items
        long totalOrders = orderItems.stream()
                .map(oi -> oi.getOrder().getId())
                .distinct()
                .count();

        // Group by product to generate breakdown details
        Map<Long, List<OrderItem>> itemsByProduct = orderItems.stream()
                .collect(Collectors.groupingBy(oi -> oi.getProduct().getId()));

        List<ProductSalesDTO> productSales = itemsByProduct.entrySet().stream().map(entry -> {
            Long productId = entry.getKey();
            List<OrderItem> items = entry.getValue();
            String productName = items.get(0).getProduct().getName();
            long orderCount = items.stream()
                    .map(oi -> oi.getOrder().getId())
                    .distinct()
                    .count();
            long itemsSold = items.stream().mapToLong(OrderItem::getQuantity).sum();
            BigDecimal revenue = items.stream()
                    .map(oi -> oi.getPrice().multiply(BigDecimal.valueOf(oi.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            return new ProductSalesDTO(productId, productName, orderCount, itemsSold, revenue);
        }).collect(Collectors.toList());

        return new SalesReportDTO(totalOrders, totalRevenue, totalItemsSold, productSales);
    }
}

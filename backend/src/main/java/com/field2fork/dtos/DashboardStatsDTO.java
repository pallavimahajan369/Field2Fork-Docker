package com.field2fork.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardStatsDTO {
    private long totalUsers;
    private long activeUsers;
    private long inactiveUsers;
    private long totalBuyers;
    private long activeBuyers;
    private long inactiveBuyers;
    private long totalSellers;
    private long activeSellers;
    private long inactiveSellers;
    private long totalProducts;
    private long activeProducts;
    private long inactiveProducts;
    private long totalOrders;
    private long successfulTransactions;
}

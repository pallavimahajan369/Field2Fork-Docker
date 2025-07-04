package com.field2fork.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.field2fork.dtos.DashboardStatsDTO;
import com.field2fork.dao.*;
import com.field2fork.pojos.PaymentStatus;
import com.field2fork.pojos.Role;

@Service
public class DashboardService {

    @Autowired
    private UserDao userdao;

    @Autowired
    private ProductDao productdao;

    @Autowired
    private OrderDao orderdao;

    @Autowired
    private PaymentsDao paymentsdao;

    public DashboardStatsDTO getDashboardStats() {
        long totalUsers = userdao.count();
        long activeUsers = userdao.countByActiveStatus(true);
        long inactiveUsers = userdao.countByActiveStatus(false);

        long totalBuyers = userdao.countByRole(Role.BUYER);
        long activeBuyers = userdao.countByRoleAndActiveStatus(Role.BUYER, true);
        long inactiveBuyers = userdao.countByRoleAndActiveStatus(Role.BUYER, false);

        long totalSellers = userdao.countByRole(Role.SELLER);
        long activeSellers = userdao.countByRoleAndActiveStatus(Role.SELLER, true);
        long inactiveSellers = userdao.countByRoleAndActiveStatus(Role.SELLER, false);

        long totalProducts = productdao.count();
        long activeProducts = productdao.countByActiveStatus(true);
        long inactiveProducts = productdao.countByActiveStatus(false);

        long totalOrders = orderdao.count();
        long successfulTransactions = paymentsdao.countByPaymentStatus(PaymentStatus.SUCCESS);

        return new DashboardStatsDTO(
            totalUsers, activeUsers, inactiveUsers,
            totalBuyers, activeBuyers, inactiveBuyers,
            totalSellers, activeSellers, inactiveSellers,
            totalProducts, activeProducts, inactiveProducts,
            totalOrders, successfulTransactions
        );
    }
}

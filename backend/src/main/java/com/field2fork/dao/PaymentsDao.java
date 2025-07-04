package com.field2fork.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.field2fork.pojos.Payments;
import com.field2fork.pojos.PaymentStatus;

public interface PaymentsDao extends JpaRepository<Payments, Long> {
    
    long countByPaymentStatus(PaymentStatus paymentStatus);

    @Query("SELECT COUNT(p) FROM Payments p WHERE p.paymentStatus = :status")
    long getCountByPaymentStatus(@Param("status") PaymentStatus status);
}

package com.field2fork.dtos;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class PaymentRequestDTO {
    private Long orderId;
    private BigDecimal amount; 
    private String paymentMethod; // e.g., "RAZORPAY"
    private String razorpayPaymentId;
    private String razorpayOrderId;
    private String razorpaySignature;
    // status from Razorpay: "SUCCESS" or "FAILED"
    private String paymentStatus;
}

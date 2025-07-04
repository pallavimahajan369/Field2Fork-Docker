package com.field2fork.dtos;

import lombok.Data;

@Data
public class PaymentResponseDTO {
	private Long orderId;
    private String message;
    private String paymentStatus;
    private String razorpayPaymentId;
    private String razorpayOrderId;
    private String razorpaySignature;
}

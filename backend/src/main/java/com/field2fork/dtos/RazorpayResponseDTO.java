package com.field2fork.dtos;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class RazorpayResponseDTO {
	private Long orderId;
    private String razorpayOrderId; // The order ID from Razorpay
    private BigDecimal amount; // The total amount for the order
    private String currency; // The currency for the order
    private String receipt; // The receipt ID for the order
    private String status; // The status of the order (e.g., created, paid, etc.)

    public RazorpayResponseDTO(Long orderId,String razorpayOrderId, BigDecimal amount, String currency, String receipt, String status) {
    	this.orderId=orderId;
        this.razorpayOrderId = razorpayOrderId;
        this.amount = amount;
        this.currency = currency;
        this.receipt = receipt;
        this.status = status;
    }
}

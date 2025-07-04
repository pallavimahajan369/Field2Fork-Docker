package com.field2fork.service;

import java.util.List;

import com.field2fork.dtos.PaymentRequestDTO;
import com.field2fork.dtos.PaymentResponseDTO;

public interface PaymentService {

	PaymentResponseDTO processPayment(PaymentRequestDTO paymentRequest);

	PaymentResponseDTO getPaymentById(Long userId);
}

package com.field2fork.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.field2fork.dtos.ApiResponse;
import com.field2fork.dtos.PaymentRequestDTO;
import com.field2fork.dtos.PaymentResponseDTO;
import com.field2fork.service.PaymentService;

@RestController
@RequestMapping("/payments")
@CrossOrigin(origins = "http://localhost:5173")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;
    
    
    @GetMapping("/{paymentId}")
    public ResponseEntity<?> getPaymentsById(@PathVariable Long paymentId) {
    	PaymentResponseDTO payments = paymentService.getPaymentById(paymentId);
    	if (payments ==null) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} else {
			return ResponseEntity.ok(payments);
		}
    }
    
    // Endpoint to process payment callback
    @PostMapping("/process")
    public ResponseEntity<?> processPayment(@RequestBody PaymentRequestDTO paymentRequest) {
    try { 
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(paymentService.processPayment(paymentRequest));
     }catch(RuntimeException e) {
   	  return ResponseEntity.
   				status(HttpStatus.BAD_GATEWAY)
   				.body(new ApiResponse(e.getMessage()));
       }
     }
 }

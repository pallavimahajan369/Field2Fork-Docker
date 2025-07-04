package com.field2fork.service;


import java.sql.Timestamp;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.field2fork.custom_exception.ResourceNotFoundException;
import com.field2fork.dao.CartDao;
import com.field2fork.dao.OrderDao;
import com.field2fork.dao.PaymentsDao;
import com.field2fork.dtos.PaymentRequestDTO;
import com.field2fork.dtos.PaymentResponseDTO;
import com.field2fork.pojos.Cart;
import com.field2fork.pojos.Order;
import com.field2fork.pojos.OrderStatus;
import com.field2fork.pojos.Payments;
import com.field2fork.razorpay.RazorpaySignatureVerifier;
import com.field2fork.pojos.PaymentMethod;
import com.field2fork.pojos.PaymentStatus;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentsDao paymentsDao;
    
    @Autowired
    private OrderDao orderDao;
    
    @Autowired
    private CartDao cartDao;
    
    @Autowired
    private ModelMapper modelMapper;
    
 // Replace with your Razorpay key secret
    private static final String RAZORPAY_KEY_SECRET = "iwtKOlM4GHLXSjnE1dmzssqc";
    
    @Override
    public PaymentResponseDTO getPaymentById(Long paymentId) {
        Payments payment = paymentsDao.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
        
        PaymentResponseDTO dto = modelMapper.map(payment, PaymentResponseDTO.class);
        
        // Manually set missing fields if mapping is incomplete
        dto.setOrderId(payment.getOrder() != null ? payment.getOrder().getId() : null);
        dto.setPaymentStatus(payment.getPaymentStatus().toString());
        
        return dto;
    }

    @Override
    public PaymentResponseDTO processPayment(PaymentRequestDTO paymentRequest) {
        PaymentResponseDTO response = new PaymentResponseDTO();
        
        // Fetch the associated order
        Order order = orderDao.findById(paymentRequest.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        Payments payment = new Payments();
        payment.setOrder(order);
        payment.setAmount(paymentRequest.getAmount());
        payment.setPayment_date(new Timestamp(System.currentTimeMillis()));
        // Set the payment method (here we assume "RAZORPAY")
        payment.setPaymentMethod(PaymentMethod.valueOf(paymentRequest.getPaymentMethod().toUpperCase()));
        
        // Set Razorpay details
        payment.setRazorpayPaymentId(paymentRequest.getRazorpayPaymentId());
        payment.setRazorpayOrderId(paymentRequest.getRazorpayOrderId());
        payment.setRazorpaySignature(paymentRequest.getRazorpaySignature());
        
        // Process based on payment status reported by the client/Razorpay
        if ("SUCCESS".equalsIgnoreCase(paymentRequest.getPaymentStatus())) {
        	
        	// Verify Razorpay signature
            boolean validSignature = RazorpaySignatureVerifier.verifySignature(
                    paymentRequest.getRazorpayOrderId(),
                    paymentRequest.getRazorpayPaymentId(),
                    paymentRequest.getRazorpaySignature(),
                    RAZORPAY_KEY_SECRET
            );
            if (!validSignature) {
                throw new RuntimeException("Invalid Razorpay signature");
            }
            
            payment.setPaymentStatus(PaymentStatus.SUCCESS);
            order.setOrderStatus(OrderStatus.CONFIRMED);
            orderDao.save(order);
            
         // Clear the cart items now since payment succeeded.
            clearCartForUser(order.getUser().getId());
            
            response.setMessage("Payment processed successfully.");
        } else {
            payment.setPaymentStatus(PaymentStatus.FAILED);
            // Update order status to FAILED
            order.setOrderStatus(OrderStatus.FAILED);
            orderDao.save(order);
            response.setMessage("Payment failed. Cart items have been restored.");
        }
        
        // Save payment record
        paymentsDao.save(payment);
        response.setOrderId(order.getId());
        response.setPaymentStatus(payment.getPaymentStatus().toString());
        response.setRazorpayPaymentId(paymentRequest.getRazorpayPaymentId());
        response.setRazorpayOrderId(paymentRequest.getRazorpayOrderId());
        response.setRazorpaySignature(paymentRequest.getRazorpaySignature());
        return response;
    }
    
    /**
     * Clears the cart items for the specified user.
     */
    private void clearCartForUser(Long userId) {
        Cart cart = cartDao.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + userId));
        cart.getCartItems().clear();
        cartDao.save(cart);
    }
 
}

package com.field2fork.razorpay;

import com.razorpay.Utils;

public class RazorpaySignatureVerifier {

    /**
     * Verifies the signature sent by Razorpay.
     * @param razorpayOrderId The Razorpay order id.
     * @param razorpayPaymentId The Razorpay payment id.
     * @param razorpaySignature The signature sent by Razorpay.
     * @param keySecret Your Razorpay secret key.
     * @return true if the signature is valid; false otherwise.
     */
    public static boolean verifySignature(String razorpayOrderId, String razorpayPaymentId, 
                                            String razorpaySignature, String keySecret) {
        try {
            String payload = razorpayOrderId + "|" + razorpayPaymentId;
            // Verify signature using Razorpay's Utils
            return Utils.verifyWebhookSignature(payload, razorpaySignature, keySecret);
        } catch (Exception e) {
            return false;
        }
    }
}

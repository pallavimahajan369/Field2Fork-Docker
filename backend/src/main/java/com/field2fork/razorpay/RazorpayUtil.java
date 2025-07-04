package com.field2fork.razorpay;

import java.math.BigDecimal;
import org.json.JSONObject;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;

public class RazorpayUtil {

    // Replace with your actual credentials
    private static final String KEY_ID = "rzp_test_kQx4rr9DSjnBdc";
    private static final String KEY_SECRET = "iwtKOlM4GHLXSjnE1dmzssqc";

    /**
     * Create a Razorpay order.
     * @param amount Order amount in the application currency (e.g., INR)
     * @param currency Currency code (e.g., "INR")
     * @param receipt A unique receipt id for the order
     * @return Razorpay Order object containing details such as order id.
     * @throws Exception if order creation fails.
     */
    public static Order createRazorpayOrder(BigDecimal amount, String currency, String receipt) throws Exception {
        RazorpayClient client = new RazorpayClient(KEY_ID, KEY_SECRET);

        JSONObject options = new JSONObject();
        // Amount must be in the smallest currency unit (e.g., paise for INR)
        options.put("amount", amount.multiply(new BigDecimal(100)).intValue());
        options.put("currency", currency);
        options.put("receipt", receipt);

        return client.orders.create(options);
    }
}

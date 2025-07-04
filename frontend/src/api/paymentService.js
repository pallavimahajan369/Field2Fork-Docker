const API_BASE_URL = "/api/payments"; // ✅ Nginx-proxied path

export const processPayment = async (paymentData) => {
  const authDataStr = sessionStorage.getItem("authData");
  const token = authDataStr ? JSON.parse(authDataStr).token : "";

  if (!token) {
    console.error("No valid token found in session storage.");
    return;
  }

  try {
    const response = await fetch(`${API_BASE_URL}/process`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(paymentData),
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || "Payment processing failed.");
    }

    const responseBody = await response.json();
    console.log("Payment Response:", responseBody);

    return {
      orderId: responseBody.orderId,
      message: responseBody.message,
      paymentStatus: responseBody.paymentStatus,
      razorpayOrderId: responseBody.razorpayOrderId,
      razorpaySignature: responseBody.razorpaySignature,
    };
  } catch (error) {
    console.error("Error during payment:", error);
    return null;
  }
};

// Razorpay Integration
export const loadRazorpayScript = () => {
  return new Promise((resolve) => {
    const script = document.createElement("script");
    script.src = "https://checkout.razorpay.com/v1/checkout.js";
    script.onload = () => resolve(true);
    script.onerror = () => resolve(false);
    document.body.appendChild(script);
  });
};

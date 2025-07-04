const API_BASE_URL = "/api/cart"; // 🟢 Use /api to go through Nginx proxy

export const getCartDetailsById = async (id) => {
  const authDataStr = sessionStorage.getItem("authData");
  const token = authDataStr ? JSON.parse(authDataStr).token : "";
  if (!token) {
    console.error("No valid token found in session storage.");
    return;
  }

  try {
    const response = await fetch(`${API_BASE_URL}/${id}`, {
      headers: { Authorization: `Bearer ${token}` },
    });

    if (!response.ok)
      throw new Error(
        `Failed to fetch cart details (Status: ${response.status})`
      );

    const data = await response.json();
    return data;
  } catch (error) {
    console.error("Error fetching cart:", error);
    return null;
  }
};

export const fetchCartTotalQuantity = async (userId) => {
  const authDataStr = sessionStorage.getItem("authData");
  const token = authDataStr ? JSON.parse(authDataStr).token : "";

  if (!token) {
    console.error("No valid token found in session storage.");
    return;
  }

  try {
    const response = await fetch(`${API_BASE_URL}/user/${userId}`, {
      headers: { Authorization: `Bearer ${token}` },
    });

    if (!response.ok) {
      throw new Error(`HTTP error! Status: ${response.status}`);
    }

    const data = await response.json();

    if (!data || !data.cartItems) {
      console.warn("Invalid response structure:", data);
      return 0;
    }

    const totalQuantity = data.cartItems.reduce(
      (sum, item) => sum + item.quantity,
      0
    );
    return totalQuantity;
  } catch (error) {
    console.error("Error fetching cart items:", error);
    return 0;
  }
};

export const getTotalQuantity = (cartItems) => {
  return cartItems.reduce((total, item) => total + item.quantity, 0);
};

export const deleteCartItem = async (cartId, itemId) => {
  const authDataStr = sessionStorage.getItem("authData");
  const token = authDataStr ? JSON.parse(authDataStr).token : "";

  if (!token) {
    console.error("No valid token found in session storage.");
    return;
  }

  try {
    const response = await fetch(`${API_BASE_URL}/${cartId}/items/${itemId}`, {
      method: "DELETE",
      headers: { Authorization: `Bearer ${token}` },
    });

    if (!response.ok) throw new Error("Failed to delete cart item");
    return true;
  } catch (error) {
    console.error("Error deleting item:", error);
    return false;
  }
};

export const checkoutCart = async (cartId) => {
  const authDataStr = sessionStorage.getItem("authData");
  const token = authDataStr ? JSON.parse(authDataStr).token : "";

  if (!token) {
    console.error("No valid token found in session storage.");
    return;
  }

  try {
    const response = await fetch(`${API_BASE_URL}/checkout/${cartId}`, {
      method: "POST",
      headers: { Authorization: `Bearer ${token}` },
    });

    if (!response.ok) throw new Error("Failed to Proceed for Checkout");

    const responseBody = await response.json();

    return {
      orderId: responseBody.orderId,
      razorpayOrderId: responseBody.razorpayOrderId,
      amount: responseBody.amount,
      currency: responseBody.currency,
      receipt: responseBody.receipt,
      status: responseBody.status,
    };
  } catch (error) {
    console.error("Error during checkout:", error);
    return null;
  }
};

export const addToCart = async (cartRequest) => {
  const authDataStr = sessionStorage.getItem("authData");
  const token = authDataStr ? JSON.parse(authDataStr).token : "";

  if (!token) {
    console.error("No valid token found in session storage.");
    return;
  }

  try {
    const response = await fetch(`${API_BASE_URL}/add`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(cartRequest),
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message);
    }

    return await response.json();
  } catch (error) {
    console.error("Error adding to cart:", error);
    throw error;
  }
};

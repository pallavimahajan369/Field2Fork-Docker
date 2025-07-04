// src/api/orderService.js
const ORDER_API_BASE_URL = "/api/orders"; // ✅ Proxy through nginx

export const fetchOrdersBySeller = async (sellerId) => {
  const authDataStr = sessionStorage.getItem("authData");
  const token = authDataStr ? JSON.parse(authDataStr).token : "";

  if (!token) {
    console.error("No valid token found in session storage.");
    return;
  }

  try {
    const response = await fetch(`${ORDER_API_BASE_URL}/user/${sellerId}`, {
      headers: { Authorization: `Bearer ${token}` },
    });

    if (!response.ok) throw new Error("Failed to fetch orders for seller");

    return await response.json();
  } catch (error) {
    console.error("Error fetching orders by seller:", error);
    return [];
  }
};

export const fetchOrdersByUserId = async (userId) => {
  const authDataStr = sessionStorage.getItem("authData");
  const token = authDataStr ? JSON.parse(authDataStr).token : "";

  if (!token) {
    console.error("No valid token found in session storage.");
    return [];
  }

  try {
    const response = await fetch(`${ORDER_API_BASE_URL}/user/${userId}`, {
      headers: { Authorization: `Bearer ${token}` },
    });

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(errorText || "Failed to fetch orders");
    }

    const contentType = response.headers.get("content-type");
    if (contentType && contentType.includes("application/json")) {
      return await response.json();
    } else {
      return [];
    }
  } catch (error) {
    console.error("fetchOrdersByUserId error:", error.message);
    return [];
  }
};

export const CancelOrderById = async (orderId) => {
  try {
    const response = await fetch(`${ORDER_API_BASE_URL}/${orderId}`, {
      method: "DELETE",
    });

    if (!response.ok) throw new Error("Failed to cancel order");

    return response;
  } catch (error) {
    console.error("CancelOrderById error:", error.message);
    throw error;
  }
};

export const fetchOrderItemsByOrderId = async (orderId) => {
  try {
    const response = await fetch(`${ORDER_API_BASE_URL}/${orderId}/items`);

    if (!response.ok) throw new Error("Failed to fetch order items");

    return await response.json();
  } catch (error) {
    console.error("fetchOrderItemsByOrderId error:", error.message);
    throw error;
  }
};

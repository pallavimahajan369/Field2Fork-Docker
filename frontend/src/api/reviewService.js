const API_BASE_URL = "/api/reviews"; // ✅ Now uses Nginx reverse proxy

export const fetchReviews = async () => {
  const authDataStr = sessionStorage.getItem("authData");
  const token = authDataStr ? JSON.parse(authDataStr).token : "";

  if (!token) {
    console.error("No valid token found in session storage.");
    return;
  }
  try {
    const response = await fetch(API_BASE_URL, {
      headers: { Authorization: `Bearer ${token}` },
    });
    if (!response.ok) throw new Error("Failed to fetch reviews");
    return await response.json();
  } catch (error) {
    console.error("Error fetching reviews:", error);
    return [];
  }
};

export const deleteReviewById = async (id) => {
  const authDataStr = sessionStorage.getItem("authData");
  const token = authDataStr ? JSON.parse(authDataStr).token : "";

  if (!token) {
    console.error("No valid token found in session storage.");
    return;
  }
  try {
    const response = await fetch(`${API_BASE_URL}/${id}`, {
      method: "DELETE",
      headers: { Authorization: `Bearer ${token}` },
    });
    if (!response.ok) throw new Error("Failed to delete review");
    return await response.json();
  } catch (error) {
    console.error("Error deleting review:", error);
    throw error;
  }
};

export const fetchReviewByProductId = async (productId) => {
  try {
    const response = await fetch(`${API_BASE_URL}/${productId}`);
    if (!response.ok)
      throw new Error("Failed to fetch reviews for the product");
    return await response.json();
  } catch (error) {
    console.error("Error fetching reviews by Product ID:", error);
    return [];
  }
};

export const addReview = async (reviewData) => {
  const authDataStr = sessionStorage.getItem("authData");
  const token = authDataStr ? JSON.parse(authDataStr).token : "";

  if (!token) {
    console.error("No valid token found in session storage.");
    return;
  }
  try {
    const response = await fetch(API_BASE_URL, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(reviewData),
    });

    if (!response.ok) {
      throw new Error("Error adding review");
    }
    return await response.json(); // ✅ Correct return for 201 Created response
  } catch (error) {
    console.error("Error adding review:", error);
    throw error;
  }
};

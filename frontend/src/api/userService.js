const API_BASE_URL = "/api/users"; // ✅ Updated to use Nginx proxy path
 // Change this if needed

export const getBuyers = async () => {
  const authDataStr = sessionStorage.getItem("authData");
  const token = authDataStr ? JSON.parse(authDataStr).token : "";

  // If there's no token, log an error and don't make the request.
  if (!token) {
    console.error("No valid token found in session storage.");
    return;
  }
  try {
    const response = await fetch(`${API_BASE_URL}/buyers/after/0`, {
      headers: { Authorization: `Bearer ${token}` },
    });
    if (!response.ok) throw new Error("Failed to fetch buyers");
    return await response.json();
  } catch (error) {
    console.error("Error fetching buyers:", error);
    return [];
  }
};

export const fetchMoreBuyers = async (lastFetchedId) => {
  const authDataStr = sessionStorage.getItem("authData");
  const token = authDataStr ? JSON.parse(authDataStr).token : "";

  // If there's no token, log an error and don't make the request.
  if (!token) {
    console.error("No valid token found in session storage.");
    return;
  }
  try {
    const response = await fetch(
      `${API_BASE_URL}/buyers/after/${lastFetchedId}`,
      {
        headers: { Authorization: `Bearer ${token}` },
      }
    );
    if (!response.ok) throw new Error("Failed to fetch more buyers");
    return await response.json();
  } catch (error) {
    console.error("Error fetching more buyers:", error);
    return [];
  }
};

export const fetchPrevBuyers = async (firstBuyerId) => {
  const authDataStr = sessionStorage.getItem("authData");
  const token = authDataStr ? JSON.parse(authDataStr).token : "";

  // If there's no token, log an error and don't make the request.
  if (!token) {
    console.error("No valid token found in session storage.");
    return;
  }
  try {
    const response = await fetch(
      `${API_BASE_URL}/buyers/before/${firstBuyerId}`,
      {
        headers: { Authorization: `Bearer ${token}` },
      }
    );
    if (!response.ok) throw new Error("Failed to fetch previous buyers");
    return await response.json();
  } catch (error) {
    console.error("Error fetching previous buyers:", error);
    return [];
  }
};

export const getBuyerById = async (id) => {
  const authDataStr = sessionStorage.getItem("authData");
  const token = authDataStr ? JSON.parse(authDataStr).token : "";

  // If there's no token, log an error and don't make the request.
  if (!token) {
    console.error("No valid token found in session storage.");
    return;
  }
  try {
    const response = await fetch(`${API_BASE_URL}/buyers/${id}`, {
      headers: { Authorization: `Bearer ${token}` },
    });
    if (!response.ok) throw new Error("Failed to fetch buyer");
    return await response.json();
  } catch (error) {
    console.error("Error fetching buyer by ID:", error);
    return [];
  }
};

export const getSellers = async () => {
  const authDataStr = sessionStorage.getItem("authData");
  const token = authDataStr ? JSON.parse(authDataStr).token : "";

  // If there's no token, log an error and don't make the request.
  if (!token) {
    console.error("No valid token found in session storage.");
    return;
  }
  try {
    const response = await fetch(`${API_BASE_URL}/sellers/after/0`, {
      headers: { Authorization: `Bearer ${token}` },
    });
    if (!response.ok) throw new Error("Failed to fetch sellers");
    return await response.json();
  } catch (error) {
    console.error("Error fetching sellers:", error);
    return [];
  }
};

export const fetchMoreSellers = async (lastFetchedId) => {
  const authDataStr = sessionStorage.getItem("authData");
  const token = authDataStr ? JSON.parse(authDataStr).token : "";

  // If there's no token, log an error and don't make the request.
  if (!token) {
    console.error("No valid token found in session storage.");
    return;
  }
  try {
    const response = await fetch(
      `${API_BASE_URL}/sellers/after/${lastFetchedId}`,
      {
        headers: { Authorization: `Bearer ${token}` },
      }
    );
    if (!response.ok) throw new Error("Failed to fetch more sellers");
    return await response.json();
  } catch (error) {
    console.error("Error fetching more sellers:", error);
    return [];
  }
};

export const fetchPrevSellers = async (firstSellerId) => {
  const authDataStr = sessionStorage.getItem("authData");
  const token = authDataStr ? JSON.parse(authDataStr).token : "";

  // If there's no token, log an error and don't make the request.
  if (!token) {
    console.error("No valid token found in session storage.");
    return;
  }
  try {
    const response = await fetch(
      `${API_BASE_URL}/sellers/before/${firstSellerId}`,
      {
        headers: { Authorization: `Bearer ${token}` },
      }
    );
    if (!response.ok) throw new Error("Failed to fetch previous sellers");
    return await response.json();
  } catch (error) {
    console.error("Error fetching previous sellers:", error);
    return [];
  }
};

export const getSellerById = async (id) => {
  const authDataStr = sessionStorage.getItem("authData");
  const token = authDataStr ? JSON.parse(authDataStr).token : "";

  // If there's no token, log an error and don't make the request.
  if (!token) {
    console.error("No valid token found in session storage.");
    return;
  }
  try {
    const response = await fetch(`${API_BASE_URL}/sellers/${id}`, {
      headers: { Authorization: `Bearer ${token}` },
    });
    if (!response.ok) throw new Error("Failed to fetch seller");
    return await response.json();
  } catch (error) {
    console.error("Error fetching seller by ID:", error);
    return [];
  }
};

export const deleteUser = async (id) => {
  const authDataStr = sessionStorage.getItem("authData");
  const token = authDataStr ? JSON.parse(authDataStr).token : "";

  // If there's no token, log an error and don't make the request.
  if (!token) {
    console.error("No valid token found in session storage.");
    return;
  }
  try {
    const response = await fetch(`${API_BASE_URL}/${id}`, {
      method: "PATCH",
      headers: { Authorization: `Bearer ${token}` },
    });
    if (!response.ok) throw new Error("Failed to delete user");
    return await response.json(); // Return the response if needed
  } catch (error) {
    console.error("Error deleting user:", error);
    throw error; // Rethrow the error for handling in the component
  }
};

export const restoreUser = async (id) => {
  const authDataStr = sessionStorage.getItem("authData");
  const token = authDataStr ? JSON.parse(authDataStr).token : "";

  // If there's no token, log an error and don't make the request.
  if (!token) {
    console.error("No valid token found in session storage.");
    return;
  }
  try {
    const response = await fetch(`${API_BASE_URL}/${id}/restore`, {
      method: "PATCH",
      headers: { Authorization: `Bearer ${token}` },
    });
    if (!response.ok) throw new Error("Failed to restore user");
    return await response.json(); // Return the response if needed
  } catch (error) {
    console.error("Error restoring user:", error);
    throw error; // Rethrow the error for handling in the component
  }
};

export const registerBuyer = async (formData) => {
  try {
    const response = await fetch(`${API_BASE_URL}/buyers/register`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        ...formData,
        role: "BUYER",
        location: formData.address,
        rating: 0.1,
      }),
    });

    const data = await response.json();

    if (!response.ok) {
      throw new Error(data.message || "Registration failed");
    }

    return data;
  } catch (error) {
    throw error;
  }
};

export const registerSeller = async (formData) => {
  try {
    const response = await fetch(`${API_BASE_URL}/sellers/register`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
  username: formData.username,
  password: formData.password,
  email: formData.email,
  role: "SELLER",
  contactNumber: formData.contactNumber,
  location: formData.location,
  rating: 0.1,
}),

    });

    const data = await response.json();

    if (!response.ok) {
      throw new Error(data.message || "Registration failed");
    }

    return data;
  } catch (error) {
    throw error;
  }
};

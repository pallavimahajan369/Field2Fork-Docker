// src/api/productImageService.js
const PRODUCT_IMAGE_API_BASE_URL = "/api/product-images"; // ✅ Proxy path

export const fetchProductImages = async (productId) => {
  try {
    const response = await fetch(
      `${PRODUCT_IMAGE_API_BASE_URL}/product/${productId}`
    );
    if (!response.ok) throw new Error("Failed to fetch product images");
    return await response.json();
  } catch (error) {
    console.error("Error fetching product images:", error);
    return [];
  }
};

export const deleteProductImage = async (imageId) => {
  const authDataStr = sessionStorage.getItem("authData");
  const token = authDataStr ? JSON.parse(authDataStr).token : "";

  if (!token) {
    console.error("No valid token found in session storage.");
    return;
  }

  try {
    const response = await fetch(`${PRODUCT_IMAGE_API_BASE_URL}/${imageId}`, {
      method: "DELETE",
      headers: { Authorization: `Bearer ${token}` },
    });
    if (!response.ok) throw new Error("Failed to delete product image");
    return await response.json();
  } catch (error) {
    console.error("Error deleting product image:", error);
    throw error;
  }
};

export const uploadProductImage = async (productId, file) => {
  const authDataStr = sessionStorage.getItem("authData");
  const token = authDataStr ? JSON.parse(authDataStr).token : "";

  if (!token) {
    console.error("No valid token found in session storage.");
    return;
  }

  try {
    const formData = new FormData();
    formData.append("productId", productId);
    formData.append("image", file);

    const response = await fetch(`${PRODUCT_IMAGE_API_BASE_URL}/upload`, {
      method: "POST",
      body: formData,
      headers: { Authorization: `Bearer ${token}` },
    });

    if (!response.ok) throw new Error("Failed to upload image");
    return await response.json();
  } catch (error) {
    console.error("Error uploading product image:", error);
    throw error;
  }
};

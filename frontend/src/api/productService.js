const API_BASE_URL = "/api/products"; // ✅ Nginx-proxy path

export const fetchProducts = async () => {
  try {
    const response = await fetch(API_BASE_URL);
    if (!response.ok) throw new Error("Failed to fetch products");
    return await response.json();
  } catch (error) {
    console.error("Error fetching products:", error);
    return [];
  }
};

export const getProductById = async (productId) => {
  try {
    const response = await fetch(`${API_BASE_URL}/${productId}`);
    if (!response.ok) throw new Error("Product not found");
    return await response.json();
  } catch (error) {
    console.error("Error fetching product by ID:", error);
    return null;
  }
};

export const deleteProduct = async (productId) => {
  const token = JSON.parse(sessionStorage.getItem("authData") || "{}").token || "";
  if (!token) return console.error("No valid token found in session storage.");

  try {
    const response = await fetch(`${API_BASE_URL}/${productId}`, {
      method: "PATCH",
      headers: { Authorization: `Bearer ${token}` },
    });
    if (!response.ok) throw new Error("Failed to delete product");
    return await response.json();
  } catch (error) {
    console.error("Error deleting product:", error);
  }
};

export const restoreProduct = async (productId) => {
  const token = JSON.parse(sessionStorage.getItem("authData") || "{}").token || "";
  if (!token) return console.error("No valid token found in session storage.");

  try {
    const response = await fetch(`${API_BASE_URL}/${productId}/restore`, {
      method: "PATCH",
      headers: { Authorization: `Bearer ${token}` },
    });
    if (!response.ok) throw new Error("Failed to restore product");
    return await response.json();
  } catch (error) {
    console.error("Error restoring product:", error);
  }
};

export const fetchProductsBySeller = async (sellerId) => {
  const token = JSON.parse(sessionStorage.getItem("authData") || "{}").token || "";
  if (!token) return console.error("No valid token found in session storage.");

  try {
    const response = await fetch(`${API_BASE_URL}/seller/${sellerId}`, {
      headers: { Authorization: `Bearer ${token}` },
    });
    if (!response.ok) throw new Error("Failed to fetch products for seller");
    return await response.json();
  } catch (error) {
    console.error("Error fetching products by seller:", error);
    return [];
  }
};

export const addNewProduct = async (productData) => {
  const token = JSON.parse(sessionStorage.getItem("authData") || "{}").token || "";
  if (!token) return console.error("No valid token found in session storage.");

  try {
    const response = await fetch(`${API_BASE_URL}/add`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(productData),
    });
    if (!response.ok) throw new Error("Failed to add product");
    return await response.json();
  } catch (error) {
    console.error("Error adding product:", error);
    throw error;
  }
};

export const updateProductDetails = async (productData, productId) => {
  const token = JSON.parse(sessionStorage.getItem("authData") || "{}").token || "";
  if (!token) return console.error("No valid token found in session storage.");

  try {
    const response = await fetch(`${API_BASE_URL}/${productId}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(productData),
    });
    if (!response.ok) throw new Error("Failed to update product");
    return await response.json();
  } catch (error) {
    console.error("Error updating product:", error);
    throw error;
  }
};

export const fetchCategories = async () => {
  const token = JSON.parse(sessionStorage.getItem("authData") || "{}").token || "";
  if (!token) return console.error("No valid token found in session storage.");

  try {
    const response = await fetch(`${API_BASE_URL}/categories`, {
      headers: { Authorization: `Bearer ${token}` },
    });
    if (!response.ok) throw new Error("Failed to fetch categories");
    return await response.json();
  } catch (error) {
    console.error("Error fetching categories:", error);
    return [];
  }
};

export const fetchProductsByCategories = async (categoryName) => {
  try {
    const formattedCategoryName = categoryName.toLowerCase().replace(/\s+/g, "_");
    const response = await fetch(
      `${API_BASE_URL}/category?category=${formattedCategoryName}`
    );
    if (!response.ok) throw new Error("Failed to fetch categories");
    return await response.json();
  } catch (error) {
    console.error("Error fetching categories:", error);
    return [];
  }
};

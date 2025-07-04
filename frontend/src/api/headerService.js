const API_BASE_URL = "/api/products";

export const fetchCategories = async () => {
  try {
    const response = await fetch(`${API_BASE_URL}/categories`);
    if (!response.ok) {
      throw new Error("Network response was not ok");
    }

    const data = await response.json();

    return data.map((cat) => ({
      id: cat,
      name: cat.toString().replace(/_/g, " "),
    }));
  } catch (error) {
    console.error("Error fetching categories:", error);
    return [];
  }
};

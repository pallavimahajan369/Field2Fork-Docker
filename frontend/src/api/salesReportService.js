// src/api/salesReportService.js
const SALES_REPORT_API_BASE_URL = "/api/sales-report"; // ✅ Now uses Nginx proxy path

export const fetchSalesReport = async (sellerId) => {
  const authDataStr = sessionStorage.getItem("authData");
  const token = authDataStr ? JSON.parse(authDataStr).token : "";

  if (!token) {
    console.error("No valid token found in session storage.");
    return;
  }
  try {
    const response = await fetch(`${SALES_REPORT_API_BASE_URL}/${sellerId}`, {
      headers: { Authorization: `Bearer ${token}` },
    });
    console.log(response);
    if (!response.ok) throw new Error("Failed to fetch sales report");
    const data = await response.json();
    return data;
  } catch (error) {
    console.error("Error fetching sales report:", error);
    return null;
  }
};

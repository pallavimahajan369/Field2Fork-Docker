import React, { useEffect, useState } from "react";
import axios from "axios";
import { motion } from "framer-motion";
import { PieChart, Pie, Cell, Tooltip, ResponsiveContainer } from "recharts";
import { DollarSign, ShoppingCart, Package } from "lucide-react";

const SellerDashboardStats = () => {
  const authData = JSON.parse(sessionStorage.getItem("authData"));
  const sellerId = authData.user.id;

  const [report, setReport] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Retrieve auth data from session storage
    const authDataStr = sessionStorage.getItem("authData");
    const token = authDataStr ? JSON.parse(authDataStr).token : "";

    // If there's no token, log an error and don't make the request.
    if (!token) {
      console.error("No valid token found in session storage.");
      return;
    }

    axios
      .get(`/api/sales-report/${sellerId}`, {
        headers: { Authorization: `Bearer ${token}` },
      })
      .then((response) => {
        setReport(response.data);
        setLoading(false);
      })
      .catch((error) => {
        console.error("Error fetching sales report:", error);
        setLoading(false);
      });
  }, [sellerId]);

  if (loading) {
    return <p className="text-center text-gray-600">Loading sales report...</p>;
  }

  if (!report) {
    return (
      <p className="text-center text-red-500">Failed to load sales report</p>
    );
  }

  // Color palette for the pie chart slices
  const colors = [
    "#4CAF50",
    "#FF9800",
    "#2196F3",
    "#F44336",
    "#9C27B0",
    "#3F51B5",
  ];

  // Prepare data for the pie chart using product revenue
  const pieData = report.productSales.map((ps) => ({
    name: ps.productName,
    value: ps.revenue,
  }));

  // Define stat cards for overall metrics
  const statCards = [
    {
      label: "Total Orders",
      value: report.totalOrders,
      icon: <ShoppingCart className="w-8 h-8" />,
      bg: "from-blue-500 to-indigo-600",
    },
    {
      label: "Total Revenue",
      value: `₹${report.totalRevenue.toFixed(2)}`,
      icon: <DollarSign className="w-8 h-8" />,
      bg: "from-green-500 to-green-700",
    },
    {
      label: "Total Items Sold",
      value: report.totalItemsSold,
      icon: <Package className="w-8 h-8" />,
      bg: "from-purple-500 to-pink-600",
    },
  ];

  return (
    <div className="p-6">
      {/* Stat Cards */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        {statCards.map((card, index) => (
          <motion.div
            key={index}
            className={`p-6 rounded-2xl shadow-lg text-white bg-gradient-to-r ${card.bg} text-center`}
            whileHover={{ scale: 1.05 }}
            transition={{ duration: 0.3 }}
          >
            <div className="flex justify-center mb-2">{card.icon}</div>
            <p className="text-lg font-medium">{card.label}</p>
            <motion.h2
              className="text-4xl font-bold mt-2"
              initial={{ opacity: 0, y: 10 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.5, delay: index * 0.1 }}
            >
              {card.value}
            </motion.h2>
          </motion.div>
        ))}
      </div>

      {/* Pie Chart: Product Revenue Breakdown */}
      <div className="mt-8 bg-white p-6 rounded-lg shadow-lg">
        <h3 className="text-lg font-semibold mb-4 text-gray-700">
          Product Revenue Breakdown
        </h3>
        <ResponsiveContainer width="100%" height={300}>
          <PieChart>
            <Pie
              data={pieData}
              cx="50%"
              cy="50%"
              outerRadius={100}
              fill="#8884d8"
              dataKey="value"
              label
            >
              {pieData.map((entry, idx) => (
                <Cell key={`cell-${idx}`} fill={colors[idx % colors.length]} />
              ))}
            </Pie>
            <Tooltip />
          </PieChart>
        </ResponsiveContainer>
      </div>
    </div>
  );
};

export default SellerDashboardStats;

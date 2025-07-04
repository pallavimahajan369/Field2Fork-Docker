// src/components/SellerDashboard/SellerProductTable.jsx
import React, { useEffect, useState } from "react";
import { fetchProductsBySeller } from "../../api/productService";
import SellerProductCard from "./SellerProductCard";

const SellerProductTable = () => {
  const [products, setProducts] = useState([]);
  const authData = JSON.parse(sessionStorage.getItem("authData"));
  const sellerId = authData.user.id;

  useEffect(() => {
    loadProducts();
  }, []);

  const loadProducts = async () => {
    const data = await fetchProductsBySeller(sellerId);
    setProducts(data);
  };

  // Update product's activeStatus in state.
  const handleProductUpdate = (productId, newStatus) => {
    setProducts((prevProducts) =>
      prevProducts.map((product) =>
        product.id === productId
          ? { ...product, activeStatus: newStatus }
          : product
      )
    );
  };

  return (
    <div className="p-5">
      <h2 className="text-2xl font-bold mb-6">My Products</h2>
      <div className="flex flex-col gap-6">
        {products.length > 0 ? (
          products.map((product) => (
            <SellerProductCard
              key={product.id}
              product={product}
              onProductUpdate={handleProductUpdate}
            />
          ))
        ) : (
          <p className="text-center text-gray-500">No products found.</p>
        )}
      </div>
    </div>
  );
};

export default SellerProductTable;

import { useEffect, useState } from "react";
import { Search, Trash2, RotateCcw } from "lucide-react";
import {
  fetchProducts,
  deleteProduct,
  restoreProduct,
  getProductById,
} from "../../api/productService";

const ProductTable = () => {
  const [products, setProducts] = useState([]);
  const [searchId, setSearchId] = useState("");

  useEffect(() => {
    loadProducts();
  }, []);

  const loadProducts = async () => {
    const data = await fetchProducts();
    setProducts(data);
  };

  const handleSearch = async () => {
    if (!searchId) {
      loadProducts();
      return;
    }
    const product = await getProductById(searchId);
    if (product) {
      // Wrap the single product object in an array
      setProducts([product]);
    } else {
      alert("Product not found");
    }
  };

  const handleDelete = async (productId) => {
    await deleteProduct(productId);
    // Update the product status to false or 0 in the state
    setProducts((prevProducts) =>
      prevProducts.map((product) =>
        product.id === productId ? { ...product, activeStatus: false } : product
      )
    );
  };

  const handleRestore = async (productId) => {
    await restoreProduct(productId);
    // Update the product status to true or 1 in the state
    setProducts((prevProducts) =>
      prevProducts.map((product) =>
        product.id === productId ? { ...product, activeStatus: true } : product
      )
    );
  };

  return (
    <div className="p-5">
      <h2 className="text-xl font-bold mb-4">Products List</h2>

      {/* Search Bar */}
      <div className="mb-6 flex items-center bg-white border border-gray-300 rounded-full shadow-sm px-4 py-2 w-full max-w-md">
        <Search className="text-gray-500" size={20} />
        <input
          type="text"
          placeholder="Search by Product ID"
          value={searchId}
          onChange={(e) => setSearchId(e.target.value)}
          className="w-full px-3 py-1 text-gray-700 bg-transparent focus:outline-none"
        />
        <button
          onClick={handleSearch}
          className="bg-green-500 text-white px-4 py-1.5 rounded-full hover:bg-green-600 transition"
        >
          Search
        </button>
      </div>

      <div className="overflow-x-auto">
        <table className="min-w-full bg-white rounded-lg shadow-md">
          <thead>
            <tr className="bg-gradient-to-r from-green-500 to-teal-500 text-white uppercase text-sm leading-normal">
              <th className="p-3 text-left">ID</th>
              <th className="p-3 text-left">Name</th>
              <th className="p-3 text-left">Description</th>
              <th className="p-3 text-left">Price</th>
              <th className="p-3 text-left">Stock</th>
              <th className="p-3 text-left">Status</th>
              <th className="p-3 text-left">Category</th>
              <th className="p-3 text-left">Actions</th>
            </tr>
          </thead>
          <tbody className="text-gray-600 text-sm font-light">
            {products.length > 0 ? (
              products.map((product) => (
                <tr
                  key={product.id} // Ensure this is unique
                  className="transition duration-300 ease-in-out transform hover:scale-102 hover:bg-teal-100"
                >
                  <td className="p-3">{product.id}</td>
                  <td className="p-3">{product.name}</td>
                  <td className="p-3">{product.description}</td>
                  <td className="p-3">₹{product.pricePerUnit}</td>
                  <td className="p-3">{product.stockQuantity}</td>
                  <td className="p-3">{product.status}</td>
                  <td className="p-3">{product.category}</td>
                  <td className="p-3 flex gap-3">
                    {product.activeStatus ? (
                      <button
                        onClick={() => handleDelete(product.id)}
                        className="bg-red-500 text-white px-3 py-1 rounded-full hover:bg-red-600 transition flex items-center gap-1"
                      >
                        <Trash2 size={16} /> Delete
                      </button>
                    ) : (
                      <button
                        onClick={() => handleRestore(product.id)}
                        className="bg-green-500 text-white px-3 py-1 rounded-full hover:bg-green-600 transition flex items-center gap-1"
                      >
                        <RotateCcw size={16} /> Restore
                      </button>
                    )}
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan="8" className="p-3 text-center text-gray-500">
                  No products found.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default ProductTable;

import { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import { motion, AnimatePresence } from "framer-motion";
import {
  ShoppingCart,
  User,
  Bell,
  ShoppingBag,
  ChevronDown,
  Menu,
  X,
  Leaf,
  LogIn,
} from "lucide-react";
import { fetchCategories } from "../../api/headerService";
import { fetchCartTotalQuantity } from "../../api/cartService";

const Header = () => {
  const [isLoggedIn, setIsLoggedIn] = useState(false); // Add your auth state logic
  const [showRegister, setShowRegister] = useState(false);
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const [isDropdownOpen, setIsDropdownOpen] = useState(false);
  const [categories, setCategories] = useState([]);
  const [cartQuantity, setCartQuantity] = useState(0);
  const navigate = useNavigate();

  const authData = sessionStorage.getItem("authData");
  const userId = authData ? JSON.parse(authData).user?.id : null;

  useEffect(() => {
    const checkAuth = () => {
      const authData = sessionStorage.getItem("authData");
      setIsLoggedIn(!!(authData && JSON.parse(authData).token));
    };

    // Check auth status on mount
    checkAuth();

    // Add storage event listener
    window.addEventListener("storage", checkAuth);

    return () => window.removeEventListener("storage", checkAuth);
  }, []);

  // logout handler
  const handleLogout = () => {
    sessionStorage.removeItem("authData");
    sessionStorage.clear();
    setIsLoggedIn(false);
    navigate("/login");
  };

  useEffect(() => {
    const loadCategories = async () => {
      const categoriesData = await fetchCategories();
      setCategories(categoriesData);
    };
    loadCategories();
  }, []);

  // Fetch cart items quantity
  useEffect(() => {
    const fetchCartQuantity = async () => {
      if (userId) {
        const totalQuantity = await fetchCartTotalQuantity(userId);
        console.log("Total Quantity:", totalQuantity); // Debugging
        setCartQuantity(totalQuantity);
      } else {
        console.log("No user ID found in sessionStorage");
      }
    };
    fetchCartQuantity();
  }, []);
  const [cartId, setCartId] = useState(null);

  useEffect(() => {
    const getCartId = async () => {
      if (userId) {
        const id = await fetchCartIdByUserId(userId);
        if (id) {
          setCartId(id);
          sessionStorage.setItem("cartId", id);
        }
      }
    };

    getCartId();
  }, [userId]);

  const fetchCartIdByUserId = async (userId) => {
    const authDataStr = sessionStorage.getItem("authData");
    const token = authDataStr ? JSON.parse(authDataStr).token : "";

    try {
      const response = await fetch(
        `/api/cart/user/${userId}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (!response.ok) {
        throw new Error(`Cart not found for userId ${userId}`);
      }

      const data = await response.json();
      return data.id; // this is the cartId
    } catch (error) {
      console.error("Error fetching cart by userId:", error);
      return null;
    }
  };

  const authLinks = [
    { icon: Bell, label: "Notifications", path: "#" },
    { icon: ShoppingBag, label: "Orders", path: "/orders" },
    { icon: ShoppingCart, label: "Cart", path: `/cart/${cartId}` },
  ];

  return (
    <>
      <motion.div
        initial={{ opacity: 0, y: -50 }}
        animate={{ opacity: 1, y: 0 }}
      >
        {/* Main Header */}
        <header className="bg-white border-b border-gray-100 sticky top-0 z-50">
          <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div className="h-16 flex items-center justify-between">
              {/* Left Section */}
              <div className="flex items-center space-x-8">
                <Link to="/" className="flex items-center">
                  <Leaf className="h-8 w-8 text-emerald-600" />
                  <span className="ml-2 text-xl font-bold text-gray-900">
                    Field2Fork
                  </span>
                </Link>

                {/* Desktop Navigation */}
                <nav className="hidden md:flex items-center space-x-6">
                  <div className="relative">
                    <button
                      onMouseEnter={() => setIsDropdownOpen(true)}
                      onMouseLeave={() => setIsDropdownOpen(false)}
                      className="flex items-center text-gray-600 hover:text-emerald-600 transition-colors font-medium"
                    >
                      Categories
                      <ChevronDown className="ml-1 h-4 w-4" />
                    </button>

                    <AnimatePresence>
                      {isDropdownOpen && (
                        <motion.div
                          initial={{ opacity: 0, y: 10 }}
                          animate={{ opacity: 1, y: 0 }}
                          exit={{ opacity: 0, y: 10 }}
                          className="absolute top-full left-0 w-48 bg-white border border-gray-100 rounded-lg shadow-lg py-2 mt-2"
                          onMouseEnter={() => setIsDropdownOpen(true)} // Keep dropdown open on hover
                          onMouseLeave={() => setIsDropdownOpen(false)}
                        >
                          {categories.length > 0 ? (
                            <ul className="space-y-1">
                              {categories.map((category, index) => (
                                <li
                                  key={index}
                                  className="hover:bg-teal-100 rounded-md p-2"
                                >
                                  <Link
                                    to={`/category/${category.id}`}
                                    className="flex items-center text-gray-600"
                                  >
                                    {category.name}
                                  </Link>
                                </li>
                              ))}
                            </ul>
                          ) : (
                            <p className="px-4 py-2 text-gray-500">
                              Loading...
                            </p>
                          )}
                        </motion.div>
                      )}
                    </AnimatePresence>
                  </div>
                  <Link
                    to="/shop"
                    className="text-gray-600 hover:text-emerald-600 transition-colors font-medium"
                  >
                    Shop
                  </Link>
                  <Link
                    to="/contactus"
                    className="text-gray-600 hover:text-emerald-600 transition-colors font-medium"
                  >
                    Contact
                  </Link>
                  <Link
                    to="/aboutus"
                    className="text-gray-600 hover:text-emerald-600 transition-colors font-medium"
                  >
                    About
                  </Link>
                </nav>
              </div>

              {/* Right Section */}
              <div className="flex items-center space-x-6">
                {/* Search Bar */}
                <div className="hidden md:block relative w-72">
                  <input
                    type="text"
                    placeholder="Search products..."
                    className="w-full pl-4 pr-10 py-2 border border-gray-200 rounded-lg focus:ring-2 focus:ring-emerald-500 focus:border-transparent hover:bg-gray-100 transition-colors duration-200 transform hover:scale-105"
                  />
                  <button className="absolute right-3 top-2.5 text-gray-400 hover:text-emerald-600">
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      className="h-5 w-5"
                      fill="none"
                      viewBox="0 0 24 24"
                      stroke="currentColor"
                    >
                      <path
                        strokeLinecap="round"
                        strokeLinejoin="round"
                        strokeWidth={2}
                        d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
                      />
                    </svg>
                  </button>
                </div>

                {/* Action Icons */}
                <div className="flex items-center space-x-3">
                  {authLinks.map((item, index) => (
                    <Link
                      key={index}
                      to={item.path}
                      className="p-2 text-gray-600 hover:text-emerald-600 rounded-lg hover:bg-gray-50 relative"
                    >
                      <item.icon className="h-5 w-5" />
                      {item.label === "Cart" && (
                        <span className="absolute -top-1 -right-1 bg-emerald-600 text-white text-xs h-4 w-4 rounded-full flex items-center justify-center">
                          {cartQuantity}
                          {/* Display total cart items quantity */}
                        </span>
                      )}
                    </Link>
                  ))}

                  {/* Auth Section */}
                  <div className="relative ml-2 group">
                    {isLoggedIn ? (
                      <>
                        <button className="flex items-center space-x-1">
                          <div className="h-8 w-8 rounded-full bg-emerald-100 flex items-center justify-center hover:bg-emerald-200 transition-colors">
                            <User className="h-4 w-4 text-emerald-600" />
                          </div>
                        </button>

                        {/* Profile Dropdown */}
                        <div
                          className="absolute right-0 mt-2 w-48 bg-white border border-gray-100 rounded-lg shadow-lg py-2 
                      opacity-0 invisible group-hover:visible group-hover:opacity-100 
                      transition-all duration-200 z-50"
                        >
                          <Link
                            to="/profile"
                            className="block px-4 py-2 text-gray-700 hover:bg-gray-100"
                          >
                            Profile
                          </Link>
                          <button
                            onClick={handleLogout}
                            className="w-full text-left px-4 py-2 text-gray-700 hover:bg-gray-100"
                          >
                            Logout
                          </button>
                        </div>
                      </>
                    ) : (
                      <button
                        onClick={() => setShowRegister(true)}
                        className="flex items-center space-x-1 bg-emerald-600 text-white px-4 py-2 rounded-lg hover:bg-emerald-700 transition-colors duration-200 transform hover:scale-105"
                      >
                        <LogIn className="h-4 w-4" />
                        <span>Sign In</span>
                      </button>
                    )}
                  </div>

                  {/* Mobile Menu Button */}
                  <button
                    onClick={() => setIsMenuOpen(!isMenuOpen)}
                    className="md:hidden p-2 text-gray-600 hover:text-emerald-600"
                  >
                    {isMenuOpen ? (
                      <X className="h-6 w-6" />
                    ) : (
                      <Menu className="h-6 w-6" />
                    )}
                  </button>
                </div>
              </div>
            </div>

            {/* Mobile Menu */}
            <AnimatePresence>
              {isMenuOpen && (
                <motion.div
                  initial={{ opacity: 0, y: -20 }}
                  animate={{ opacity: 1, y: 0 }}
                  exit={{ opacity: 0, y: -20 }}
                  className="md:hidden absolute w-full bg-white border-b border-gray-100"
                >
                  <div className="px-4 py-4 space-y-4">
                    <Link
                      to="/shop"
                      className="block text-gray-700 hover:text-emerald-600"
                    >
                      Shop
                    </Link>
                    <div className="space-y-2">
                      <button className="flex items-center justify-between w-full">
                        <span className="text-gray-700">Categories</span>
                        <ChevronDown className="h-4 w-4" />
                      </button>
                      <div className="pl-4 space-y-2">
                        {categories.map((category) => (
                          <Link
                            key={category.id}
                            to={`/category/${category.id}`}
                            className="block text-gray-600 hover:text-emerald-600"
                          >
                            {category.name}
                          </Link>
                        ))}
                      </div>
                    </div>
                    <Link
                      to="/contact"
                      className="block text-gray-700 hover:text-emerald-600"
                    >
                      Contact
                    </Link>
                    <Link
                      to="/about"
                      className="block text-gray-700 hover:text-emerald-600"
                    >
                      About
                    </Link>
                  </div>
                </motion.div>
              )}
            </AnimatePresence>

            {/* Registration Modal */}
            <AnimatePresence>
              {showRegister && (
                <motion.div
                  initial={{ opacity: 0 }}
                  animate={{ opacity: 1 }}
                  exit={{ opacity: 0 }}
                  className="fixed inset-0 bg-black/50 backdrop-blur-sm flex items-center justify-center"
                >
                  <motion.div
                    initial={{ scale: 0.95 }}
                    animate={{ scale: 1 }}
                    exit={{ scale: 0.95 }}
                    className="bg-white rounded-xl p-8 max-w-md w-full mx-4"
                  >
                    <h2 className="text-2xl font-bold text-gray-900 mb-6">
                      Join Our Farm Community
                    </h2>
                    <div className="space-y-4">
                      <motion.button
                        onClick={() => {
                          navigate("/signup/buyer");
                          setShowRegister(false);
                        }}
                        whileHover={{ scale: 1.02 }}
                        className="w-full py-3 bg-gradient-to-r from-green-600 to-teal-600 text-white rounded-xl font-semibold hover:shadow-lg transition-shadow"
                      >
                        Continue as Buyer
                      </motion.button>
                      <motion.button
                        onClick={() => {
                          navigate("/signup/seller");
                          setShowRegister(false);
                        }}
                        whileHover={{ scale: 1.02 }}
                        className="w-full py-3 bg-gradient-to-r from-amber-500 to-orange-500 text-white rounded-xl font-semibold hover:shadow-lg transition-shadow"
                      >
                        Continue as Seller
                      </motion.button>
                    </div>
                    <button
                      onClick={() => setShowRegister(false)}
                      className="mt-4 text-gray-500 hover:text-gray-700 text-sm"
                    >
                      Cancel
                    </button>
                  </motion.div>
                </motion.div>
              )}
            </AnimatePresence>
          </div>
        </header>
      </motion.div>
    </>
  );
};

export default Header;

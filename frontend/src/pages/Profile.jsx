import { useEffect, useState } from "react";
import { User, Mail, MapPin, Star, Phone } from "lucide-react";

import Header from "../components/Header/Header";
import Footer from "../components/Footer/Footer";

const Profile = () => {
  const [user, setUser] = useState({
    username: "",
    email: "",
    address: "",
    location: "",
    contactNumber: "",
    role: "",
    rating: null,
  });

  useEffect(() => {
    const stored = JSON.parse(sessionStorage.getItem("user"));
    if (stored) {
      setUser(stored);
    }
  }, []);

  return (
    <div className="flex flex-col min-h-screen">
      <Header />

      {/* Page content */}
      <main className="flex-grow flex justify-center items-center bg-[#f4f3fc] p-4">
        <div className="bg-white rounded-2xl shadow-xl w-full max-w-sm p-6 text-center relative">
          <h3 className="text-sm text-violet-500 font-medium mb-2">
            Profile Information
          </h3>

          {/* Username */}
          <div className="flex items-center justify-center text-lg font-semibold text-gray-800 gap-2 mb-1">
            <User size={18} /> {user.username}
          </div>

          {/* Email */}
          <div className="flex items-center justify-center text-sm text-gray-500 mb-1">
            <Mail size={16} className="mr-1" />
            {user.email}
          </div>

          {/* Address or Location */}
          <div className="flex items-center justify-center text-sm text-gray-500 mb-1">
            <MapPin size={16} className="mr-1" />
            {user.role === "BUYER"
              ? user.address
              : user.location || "N/A"}
          </div>

          {/* Role */}
          <div className="text-sm text-gray-600 mb-1">
            Role:{" "}
            <span className="uppercase font-semibold text-emerald-600 tracking-wider">
              {user.role || "N/A"}
            </span>
          </div>

          {/* Rating */}
          {user.role === "SELLER" &&
            user.rating &&
            user.rating !== "null" && (
              <div className="flex items-center justify-center gap-1 text-sm text-yellow-600 mt-1">
                <Star size={16} />
                {user.rating} / 5
              </div>
            )}

          {/* Contact */}
          <div className="text-sm text-gray-600 mt-1">
            Contact: <span className="font-medium">{user.contactNumber}</span>
          </div>
        </div>
      </main>

      <Footer />
    </div>
  );
};

export default Profile;

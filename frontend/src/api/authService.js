// authService.js

/**
 * Logs in the user by calling the backend API.
 *
 * @param {string} email - The email input by the user.
 * @param {string} password - The password input by the user.
 * @returns {Promise<Object>} - Returns the login response data.
 * @throws {Error} - Throws an error if the login fails.
 */
export async function login(email, password) {
  try {
    const response = await fetch("/api/auth/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ email, password }),
    });

    if (!response.ok) {
      // Clone the response so we can read its body for error details
      const responseClone = response.clone();
      let errorMessage = `Error: ${response.status}`;
      try {
        const errorData = await responseClone.json();
        errorMessage = errorData.message || errorMessage;
      } catch (jsonError) {
        try {
          const errorText = await responseClone.text();
          errorMessage = errorText || errorMessage;
        } catch (textError) {
          // fallback remains errorMessage
        }
      }
      throw new Error(errorMessage);
    }

    // Parse the JSON response (this reads the original response)
    const data = await response.json();

    // Store the returned data in sessionStorage
    sessionStorage.setItem("authData", JSON.stringify(data));

    // Store data properly
    sessionStorage.setItem("authData", JSON.stringify(data));
    sessionStorage.setItem("user", JSON.stringify(data.user));   
    sessionStorage.setItem("token", data.token);                
    window.location.reload();

    // Check the user role and navigate accordingly
    const role = data.user?.role;
   if (role === "ADMIN") {
  window.location.href = "/admin";
} else if (role === "SELLER") {
  window.location.href = "/seller";
} else if (role === "BUYER") {
  window.location.href = "/";
} else {
  throw new Error("Unknown user role.");
}


    return data;
  } catch (error) {
    console.error("Error during login:", error);
    throw error;
  }
}

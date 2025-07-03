import React from "react";
import { Navigate } from "react-router-dom";

const PrivateRoute = ({ children }) => {
    const valid = validateToken()
    return valid ? children : <Navigate to="/login" replace />;
};

async function validateToken() {
    const token = localStorage.getItem("jwtToken");
    if (!token) return false;

    const response = await fetch("localhost:3000/auth/validate", {
        method: "GET",
        headers: {
            "JWT-Token": token,
        }
    });

    return response.ok;
}

export default PrivateRoute;
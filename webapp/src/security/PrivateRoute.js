import React from "react";
import { Navigate } from "react-router-dom";

const API_URL = process.env.REACT_APP_API_URL;

const PrivateRoute = ({ children }) => {
    const valid = validateToken()
    return valid ? children : <Navigate to="/login" replace />;
};

async function validateToken() {
    const token = localStorage.getItem("jwtToken");
    if (!token) return false;

    const response = await fetch(`${API_URL}/auth/validate`, {
        method: "GET",
        headers: {
            "JWT-Token": token,
        }
    });

    return response.ok;
}

export default PrivateRoute;
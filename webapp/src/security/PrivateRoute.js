import React, {useEffect, useState} from "react";
import { Navigate } from "react-router-dom";

const API_URL = process.env.REACT_APP_API_URL;

const PrivateRoute = ({ children }) => {
    const [isValid, setIsValid] = useState(null);

    useEffect(() => {
        async function checkToken() {
            const valid = await validateToken();
            setIsValid(valid);
        }
        checkToken();
    }, []);

    if (isValid === null) return null; // TODO: Add a loading spinner

    return isValid ? children : <Navigate to="/" replace />;
};

async function validateToken() {
    const token = localStorage.getItem("jwtToken");
    if (!token) return false;

    try {
        const response = await fetch(`${API_URL}/auth/validate`, {
            method: "POST",
            headers: {
                "JWT-Token": token,
            }
        });
        return response.ok;
    } catch {
        return false;
    }
}

export default PrivateRoute;
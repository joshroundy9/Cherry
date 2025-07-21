import React, {useEffect, useState} from "react";
import { Navigate } from "react-router-dom";
import {LoadingState} from "../dashboard/DashboardComponents";

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

    if (isValid === null) return <LoadingState />;

    return isValid ? children : <Navigate to="/login" replace />;
};

async function validateToken() {
    const token = localStorage.getItem("jwtToken");
    if (!token)
    {
        localStorage.removeItem('jwtToken');
        return false;
    }

    try {
        const response = await fetch(`${API_URL}/auth/validate`, {
            method: "POST",
            headers: {
                "JWT-Token": token,
            }
        });
        return response.ok;
    } catch {
        localStorage.removeItem('jwtToken');
        return false;
    }
}

export default PrivateRoute;
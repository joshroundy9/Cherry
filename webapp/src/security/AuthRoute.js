import React, {useEffect, useState} from "react";
import { Navigate } from "react-router-dom";
import {LoadingState} from "../dashboard/DashboardComponents";

const API_URL = process.env.REACT_APP_API_URL;

const AuthRoute = ({ children }) => {
    const [isValid, setIsValid] = useState(null);

    useEffect(() => {
        validateToken();
    }, []);

    async function validateToken() {
        const token = localStorage.getItem("jwtToken");
        if (!token) {
            localStorage.removeItem('jwtToken');
            setIsValid(false);
            return;
        }

        const controller = new AbortController();
        const timeoutId = setTimeout(() => controller.abort(), 5000); // 5 seconds timeout

        try {
            const response = await fetch(`${API_URL}/auth/validate`, {
                method: "POST",
                headers: {
                    "JWT-Token": token,
                },
                signal: controller.signal
            });
            clearTimeout(timeoutId);
            if (response.ok) {
                setIsValid(true);
            } else {
                localStorage.removeItem('jwtToken');
                setIsValid(false);
            }
        } catch {
            clearTimeout(timeoutId);
            localStorage.removeItem('jwtToken');
            setIsValid(false);
        }
    }

    if (isValid === null) return <LoadingState />;
    if (isValid) return <Navigate to="/dashboard" replace />;

    return children;
};

export default AuthRoute;
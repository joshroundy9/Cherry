import React, {useEffect, useState} from "react";
import { Navigate } from "react-router-dom";
import {LoadingState} from "../dashboard/DashboardComponents";

const AuthRoute = ({ children }) => {
    const [isValid, setIsValid] = useState(null);

    useEffect(() => {
        const token = localStorage.getItem("jwtToken");
        if (token && token !== '') {
            setIsValid(true);
        } else {
            setIsValid(false);
        }
    }, []);

    if (isValid === null) return <LoadingState />;
    if (isValid) return <Navigate to="/dashboard" replace />;

    return children;
};

export default AuthRoute;
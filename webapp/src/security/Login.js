// webapp/src/Login.js
import React, { useState } from 'react';
import {useLocation, useNavigate} from "react-router-dom";

const API_URL = process.env.REACT_APP_API_URL;

function Login({ onLogin }) {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const navigate = useNavigate();
    const location = useLocation();
    const message = location.state?.message;

    const handleSubmit = async (e) => {
        e.preventDefault();
        // Replace with your backend login endpoint
        const response = await fetch(`${API_URL}/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password }),
        });
        if (response.ok) {
            const data = await response.json();
            localStorage.setItem('jwtToken', data.jwt);
            onLogin(data);
            setTimeout(() => {
            navigate('/dashboard');
            }, 50);
        } else {
            alert('Login failed');
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <h2>Login</h2>
            <input
                type="text"
                placeholder="Username"
                value={username}
                onChange={e => setUsername(e.target.value)}
                required
            />
            <input
                type="password"
                placeholder="Password"
                value={password}
                onChange={e => setPassword(e.target.value)}
                required
            />
            <button type="submit">Login</button>
            {message && <div className="info-message">{message}</div>}
        </form>
    );
}

export default Login;

// webapp/src/Login.js
import React, { useState } from 'react';
import {Link, useLocation, useNavigate} from "react-router-dom";

const API_URL = process.env.REACT_APP_API_URL;

function Login({ onLogin }) {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const navigate = useNavigate();
    const location = useLocation();
    const message = location.state?.message;

    const handleSubmit = async (e) => {
        const controller = new AbortController();
        const timeoutId = setTimeout(() => controller.abort(), 2000);
        e.preventDefault();
        try {
            const response = await fetch(`${API_URL}/auth/login`, {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({username, password}),
                signal: controller.signal,
            });

            clearTimeout(timeoutId);
            if (response.ok) {
                const data = await response.json();
                localStorage.setItem('jwtToken', data.jwt);
                onLogin(data);
                setTimeout(() => {
                    navigate('/dashboard');
                }, 50);
            } else {
                navigate('/', { state: { message: 'Login failed!' } });
            }
        } catch (error) {
            console.error('Login error:', error);
            navigate('/', { state: { message: 'An error occurred while logging in.' } });
        }
    };

    return (
        <div className={"Auth-background"}>
            <div className={"Login-form"}>
                <div className={"Auth-form-header"}>
                    <p className={"Auth-form-text"}>Welcome back!</p>
                    <p className={"Auth-form-text"}>Meeting your goals starts today.</p>
                </div>
                <form style={{}} className={"Login-form"} onSubmit={handleSubmit}>
                    <p className={"Auth-form-text"}>Login</p>
                    <input
                        className={"Auth-form-input"}
                        type="text"
                        placeholder="Username"
                        value={username}
                        onChange={e => setUsername(e.target.value)}
                        required
                    />
                    <input
                        className={"Auth-form-input"}
                        type="password"
                        placeholder="Password"
                        value={password}
                        onChange={e => setPassword(e.target.value)}
                        required
                    />
                    <button className={"Form-button"} type="submit">Login</button>
                    <div style={{
                        display: 'flex',
                        justifyContent: 'center',
                        flexDirection: 'row',
                        fontSize: '3vh',
                        gap: '0.1em'
                    }}>
                        <span>Not signed up?&#32;</span><Link className="App-link" to="/register">Sign Up</Link>
                    </div>
                    <div style={{minHeight: '1em', textAlign: 'center', color: 'red'}}>
                        {message && <div className="Error-message">{message}</div>}
                    </div>
                </form>
            </div>
            <div className={"Auth-text"}>
                <p className={"Auth-text-header"}>Calorie tracking you can trust.</p>
                <p className={"Auth-text-body"}>Cherry provides everything you need for efficient calorie tracking.</p>
                <p className={"Auth-text-body"}>Don’t know much about nutrition?
                    We're here to help.</p>
            </div>
        </div>

    );
}

export default Login;

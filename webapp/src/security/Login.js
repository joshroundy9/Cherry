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
                localStorage.setItem('userId', data.user.userID);
                localStorage.setItem('username', data.user.username);
                localStorage.setItem('weight', data.user.weight);
                onLogin(data);
                setTimeout(() => {
                    navigate('/dashboard');
                }, 50);
            } else if (response.status === 400) {
                const errorText = await response.text();
                navigate('/login', { state: { message: errorText } });
            } else {
                navigate('/login', { state: { message: 'Login failed!' } });
            }
        } catch (error) {
            console.error('Login error:', error);
            navigate('/login', { state: { message: 'An error occurred while logging in.' } });
        }
    };

    return (
        <div className={"Auth-background"}>
            <div className={"Login-container"}>
                <div className={"Auth-form-header"}>
                    <div className={"Auth-form-text"}>Welcome back to <span className={"Logo-red"}>CHERRY</span>!</div>
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
                    <div style={{position: 'relative'}}>
                        <input
                            className={"Auth-form-input"}
                            type="password"
                            placeholder="Password"
                            value={password}
                            onChange={e => setPassword(e.target.value)}
                            required
                        />
                        <Link
                            to="/forgot-password"
                            className={"App-link"}
                            style={{
                                position: 'absolute',
                                left: 1.5,
                                bottom: '-1.2em',
                                fontSize: '1.3em',
                                color: '#ff0606',
                                cursor: 'pointer',
                            }}
                        >
                            Forgot your password?
                        </Link>
                    </div>
                    <button className={"Form-button Hover-expand"} type="submit">Login</button>
                    <div style={{
                        display: 'flex',
                        justifyContent: 'center',
                        flexDirection: 'row',
                        fontSize: 'x-large',
                        gap: '0.1em'
                    }}>
                        <span>Not signed up?&#32;</span><Link className="App-link" to="/register">Sign Up</Link>
                    </div>
                    <div style={{minHeight: '1em', textAlign: 'center', color: 'red', maxWidth: '90%'}}>
                        {message && <div className="Error-message">{message}</div>}
                    </div>
                </form>
            </div>
            <div className={"Auth-text"}>
                <p className={"Auth-text-header"}>AI-powered nutrition tracking.</p>
                <p className={"Auth-text-body"}>Cherry removes the hassle of calorie, protein, and weight
                    tracking and gives you the tools you need to meet your goals.</p>
                <p className={"Auth-text-body"}>All online, for free.</p>
            </div>
        </div>

    );
}

export default Login;

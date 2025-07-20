// webapp/src/Register.js
import React, { useState } from 'react';
import {Link, useNavigate, useLocation} from "react-router-dom";

const API_URL = process.env.REACT_APP_API_URL;

function Register({ onRegister }) {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [email, setEmail] = useState('');
    const [confirmEmail, setConfirmEmail] = useState('');
    const [dateOfBirth, setDateOfBirth] = useState('');
    const [weight, setWeight] = useState('');

    const navigate = useNavigate();
    const location = useLocation();
    const message = location.state?.message;

    const handleSubmit = async (e) => {
        const controller = new AbortController();
        const timeoutId = setTimeout(() => controller.abort(), 2000);

        if (password !== confirmPassword || email !== confirmEmail) {
            return;
        }

        e.preventDefault();
        try {
            const response = await fetch(`${API_URL}/auth/register`, {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({username, password, email, dateOfBirth, weight}),
                signal: controller.signal,
            });
            clearTimeout(timeoutId);
            if (response.ok) {
                const data = await response.json();
                onRegister(data); // Pass user/token up to App
                navigate('/login', {state: {message: 'Registration successful! Please log in.'}});
            } else {
                navigate('/register', {state: {message: 'Registration failed!'}});
            }
        } catch (error) {
            console.error('Registration error:', error);
            navigate('/register', {state: {message: 'An error occurred during registration.'}});
        }
    };

    return (
        <div className={"Auth-background"}>
            <div className={"Register-container"}>
                <div className={"Auth-form-header"}>
                    <div className={"Auth-form-text"}>Welcome to <span className={"Logo-red"}>CHERRY</span>!</div>
                </div>
                <form style={{marginTop: 0 + 'em'}} className={"Register-form"} onSubmit={handleSubmit}>
                    <p className={"Auth-form-text"}>Register</p>
                    <input className={"Auth-form-input"}
                           type="text"
                           placeholder="Username"
                           value={username}
                           onChange={e => setUsername(e.target.value)}
                           required
                    />
                    <input className={"Auth-form-input"}
                           type="text"
                           placeholder="Email"
                           value={email}
                           onChange={e => setEmail(e.target.value)}
                           required
                    />
                    <input className={"Auth-form-input"}
                           type="text"
                           placeholder="Confirm Email"
                           value={confirmEmail}
                           onChange={e => setConfirmEmail(e.target.value)}
                           required
                    />
                    <input className={"Auth-form-input"}
                           type="password"
                           placeholder="Password"
                           value={password}
                           onChange={e => setPassword(e.target.value)}
                           required
                    />
                    <input className={"Auth-form-input"}
                           type="password"
                           placeholder="Confirm Password"
                           value={confirmPassword}
                           onChange={e => setConfirmPassword(e.target.value)}
                           required
                    />
                    <input className={"Auth-form-input"}
                           type="date"
                           placeholder="Date of Birth"
                           value={dateOfBirth}
                           onChange={e => setDateOfBirth(e.target.value)}
                           required
                    />
                    <input className={"Auth-form-input"}
                           type="number"
                           placeholder="Body Weight (LBS)"
                           value={weight}
                           onChange={e => setWeight(e.target.value)}
                           required
                    />
                    <button className={"Form-button Hover-expand"} type="submit">Register</button>
                    <div style={{
                        display: 'flex',
                        justifyContent: 'center',
                        flexDirection: 'row',
                        fontSize: 'x-large',
                        gap: '0.1em'
                    }}>
                        <span>Already signed up?&#32;</span><Link className="App-link" to="/login">Sign In</Link>
                    </div>
                    <div style={{minHeight: '3em', textAlign: 'center', color: 'red'}}>
                        {message && <div className="Error-message">{message}</div>}
                        {password !== confirmPassword && email !== confirmEmail && (
                            <div className="Error-message"> Emails and Passwords do not match!</div>
                        )}
                        {password !== confirmPassword && email === confirmEmail && (
                            <div className="Error-message">Passwords do not match!</div>
                        )}
                        {email !== confirmEmail && password === confirmPassword && (
                            <div className="Error-message">Emails do not match!</div>
                        )}
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

            export default Register;

// webapp/src/Register.js
import React, { useState } from 'react';
import {Link, useNavigate} from "react-router-dom";

const API_URL = process.env.REACT_APP_API_URL;

function Register({ onRegister }) {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [email, setEmail] = useState('');
    const [confirmEmail, setConfirmEmail] = useState('');
    const [dateOfBirth, setDateOfBirth] = useState('');
    const [height, setHeight] = useState('');
    const [weight, setWeight] = useState('');

    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (email !== confirmEmail) {
            navigate('/register', {state: {message: 'Emails do not match!'}});
            return;
        }
        if (password !== confirmPassword) {
            navigate('/register', {state: {message: 'Passwords do not match!'}});
            return;
        }
        try {
            const response = await fetch(`${API_URL}/auth/register`, {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({username, password, email, dateOfBirth, height, weight}),
            });
            if (response.ok) {
                const data = await response.json();
                onRegister(data); // Pass user/token up to App
                navigate('/', {state: {message: 'Registration successful! Please log in.'}});
            } else {
                navigate('/register', {state: {message: 'Registration failed!'}});
            }
        } catch (error) {
            console.error('Registration error:', error);
            navigate('/register', {state: {message: 'An error occurred during registration. Please try again.'}});
        }
    };

    return (
        <div className={"Auth-background"}>
            <div className={"Register-form"}>
                <div className={"Auth-form-header"}>
                    <p className={"Auth-form-text"}>Welcome to <span className={"Logo-red"}>CHERRY</span>!</p>
                    <p className={"Auth-form-text"}>Meeting your goals starts today.</p>
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
                           placeholder="Height (IN)"
                           value={height}
                           onChange={e => setHeight(e.target.value)}
                           min="0"
                           max="100"
                           required
                    />
                    <input className={"Auth-form-input"}
                           type="number"
                           placeholder="Weight (LBS)"
                           value={weight}
                           onChange={e => setWeight(e.target.value)}
                           required
                    />
                    <button className={"Form-button"} type="submit">Register</button>
                    <div style={{
                        display: 'flex',
                        justifyContent: 'center',
                        flexDirection: 'row',
                        fontSize: '3vh',
                        gap: '0.1em'
                    }}>
                        <span>Already signed up?&#32;</span><Link className="App-link" to="/">Sign In</Link>
                    </div>
                </form>
                </div>
                <div className={"Auth-text"}>
                    <p className={"Auth-text-header"}>Calorie tracking you can trust.</p>
                    <p className={"Auth-text-body"}>Cherry provides everything you need for proper calorie
                        tracking.</p>
                    <p className={"Auth-text-body"}>All online, for free.</p>
                </div>
            </div>
            );
            }

            export default Register;

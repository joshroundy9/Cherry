// webapp/src/Register.js
import React, { useState } from 'react';

function Register({ onRegister }) {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [email, setEmail] = useState('');
    const [dateOfBirth, setDateOfBirth] = useState('');
    const [height, setHeight] = useState('');
    const [weight, setWeight] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        // Replace with your backend login endpoint
        const response = await fetch('http://localhost:8080/auth/register', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password, email, dateOfBirth, height, weight}),
        });
        if (response.ok) {
            const data = await response.json();
            onRegister(data); // Pass user/token up to App
        } else {
            alert('Registration failed');
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <h2>Register</h2>
            <input
                type="text"
                placeholder="Username"
                value={username}
                onChange={e => setUsername(e.target.value)}
                required
            />
            <input
                type="text"
                placeholder="Email"
                value={email}
                onChange={e => setEmail(e.target.value)}
                required
            />
            <input
                type="password"
                placeholder="Password"
                value={password}
                onChange={e => setPassword(e.target.value)}
                required
            />
            <input
                type="date"
                placeholder="Date of Birth"
                value={dateOfBirth}
                onChange={e => setDateOfBirth(e.target.value)}
                required
            />
            <input
                type="number"
                placeholder="Height in Inches"
                value={height}
                onChange={e => setHeight(e.target.value)}
                min="0"
                max="100"
                required
            />
            <input
                type="number"
                placeholder="Weight (LBS)"
                value={weight}
                onChange={e => setWeight(e.target.value)}
                required
            />
            <button type="submit">Login</button>
        </form>
    );
}

export default Register;

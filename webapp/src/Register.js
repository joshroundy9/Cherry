// webapp/src/Register.js
import React, { useState } from 'react';
import {Link} from "react-router-dom";

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
        <div className={"Register-background"}>
            <div className={"Register-form"}>
                <p className={"Register-form-text"}>Welcome to <span className={"Logo-red"}>CHERRY</span>!</p>
                <p className={"Register-form-text"}>Meeting your goals starts today.</p>
                <form style={{marginTop: 0 + 'em'}} className={"Register-form"} onSubmit={handleSubmit}>
                    <p className={"Register-form-text"}>Register</p>
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
                           type="text"
                           placeholder="Confirm Password"
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
                    <div style={{display: 'flex', justifyContent: 'center', flexDirection: 'row', fontSize: '3vh'}}>
                    Already signed up?&#32;<Link className="App-link" to="/login">Sign In</Link>
                    </div>
                </form>
            </div>
            <div className={"Register-text"}>
                <p className={"Register-text-header"}>Calorie tracking you can trust.</p>
                <p className={"Register-text-body"}>Cherry provides everything you need for proper calorie tracking.</p>
                <p className={"Register-text-body"}>All online, for free.</p>
            </div>
        </div>
    );
}

export default Register;

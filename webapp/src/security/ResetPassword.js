// webapp/src/ResetPassword.js
import React, {useEffect, useState} from 'react';
import {Link, useLocation, useNavigate, useSearchParams} from "react-router-dom";

const API_URL = process.env.REACT_APP_API_URL;

export function ResetPassword({ onResetPassword }) {
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [searchParams] = useSearchParams();

    const navigate = useNavigate();
    const location = useLocation();
    const [message, setMessage] = useState(location.state?.message);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        if (!searchParams.get('token')) {
            navigate('/forgot-password', { state: { message: 'Invalid reset link.' } });
        }
    }, [searchParams, navigate]);

    const handleSubmit = async (e) => {
        const controller = new AbortController();
        const timeoutId = setTimeout(() => controller.abort(), 10000);
        const token = searchParams.get('token');
        e.preventDefault();
        if (loading) {
            return; // Prevent multiple submissions
        } else {
            setLoading(true);
        }
        if (password !== confirmPassword) {
            return;
        }
        try {
            const response = await fetch(`${API_URL}/auth/reset-password`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Token': token,
                    'Password': password,
                },
                body: null,
                signal: controller.signal,
            });

            clearTimeout(timeoutId);
            if (response.ok) {
                setTimeout(() => {
                    navigate('/login', { state: { message: 'Password reset successful! Please login.' } });
                }, 50);
            } else if (response.status === 400) {
                setMessage(response.text());
            } else {
                setMessage('Password reset failed!');

            }
        } catch (error) {
            console.error('Password reset error:', error);
            setMessage('An error occurred while resetting your password.');
        }
        setLoading(false);
    };

    return (
        <div className={"Auth-background"}>
            <div className={"Login-container"}>
                <div className={"Auth-form-header"}>
                    <div className={"Auth-form-text"}>Welcome back to <span className={"Logo-red"}>CHERRY</span>!</div>
                </div>
                <form style={{paddingTop: '2em'}} className={"Login-form"} onSubmit={handleSubmit}>
                    <p className={"Auth-form-text"}>Enter New Password</p>
                    <input
                        className={"Auth-form-input"}
                        type="password"
                        placeholder="Password"
                        value={password}
                        onChange={e => setPassword(e.target.value)}
                        required
                    />
                    <input
                        className={"Auth-form-input"}
                        type="password"
                        placeholder="Confirm Password"
                        value={confirmPassword}
                        onChange={e => setConfirmPassword(e.target.value)}
                        required
                    />
                    <button className={"Form-button Hover-expand"} type="submit">Update Password</button>
                    <div style={{
                        display: 'flex',
                        justifyContent: 'center',
                        flexDirection: 'row',
                        fontSize: 'x-large',
                        gap: '0.1em'
                    }}>
                        <span>Remember your password?&#32;</span><Link className="App-link" to="/login">Login</Link>
                    </div>
                    <div style={{minHeight: '1em', textAlign: 'center', color: 'red', maxWidth: '90%'}}>
                        {password !== confirmPassword && (
                            <div className="Error-message">Passwords do not match!</div>
                        )}
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

export default ResetPassword;
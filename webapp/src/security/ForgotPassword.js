// webapp/src/ForgotPassword.js
import React, { useState } from 'react';
import {Link, useLocation, useNavigate} from "react-router-dom";
import ReCAPTCHA from "react-google-recaptcha";

const API_URL = process.env.REACT_APP_API_URL;

export function ForgotPassword({ onForgotPassword }) {
    const [email, setEmail] = useState('');
    const [captchaToken, setCaptchaToken] = useState('');
    const [loading, setLoading] = useState(false);

    const navigate = useNavigate();
    const location = useLocation();
    const message = location.state?.message;

    const handleSubmit = async (e) => {
        const controller = new AbortController();
        const timeoutId = setTimeout(() => controller.abort(), 10000);
        e.preventDefault();
        if (loading) {
            return; // Prevent multiple submissions
        } else {
            setLoading(true);
        }
        try {
            const response = await fetch(`${API_URL}/auth/forgot-password`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Email': email,
                    'Captcha-Token': captchaToken
                },
                body: null,
                signal: controller.signal,
            });

            clearTimeout(timeoutId);
            if (response.ok) {
                setTimeout(() => {
                    navigate('/forgot-password', { state: { message: 'Password reset email sent! If you have an account with this email, you will be receiving an link shortly.' } });
                }, 50);
            } else if (response.status === 400) {
                const errorText = await response.text();
                navigate('/forgot-password', { state: { message: errorText } });
            }
        } catch (error) {
            console.error('Password reset error:', error);
            navigate('/forgot-password', { state: { message: 'An error occurred while sending the password reset request.' } });
        }
        setLoading(false);
    };

    return (
        <div className={"Auth-background"}>
            <div className={"Login-container"}>
                <div className={"Auth-form-header"}>
                    <div className={"Auth-form-text"}>Welcome back to <span className={"Logo-red"}>CHERRY</span>!</div>
                </div>
                <form style={{paddingTop: '1em'}} className={"Login-form"} onSubmit={handleSubmit}>
                    <p className={"Auth-form-text"}>Reset Password</p>
                    <input
                        className={"Auth-form-input"}
                        type="text"
                        placeholder="Email"
                        value={email}
                        onChange={e => setEmail(e.target.value)}
                        required
                    />
                    <ReCAPTCHA
                        sitekey="6Lcy4JArAAAAAA3lKAEzvS36ijPRDnrzJiR_m5zw"
                        onChange={setCaptchaToken}
                    />
                    <button className={"Form-button Hover-expand"} type="submit">Send Reset Link</button>
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

export default ForgotPassword;
import {LoadingState} from "../dashboard/DashboardComponents";
import {useNavigate, useSearchParams} from "react-router-dom";
import {useEffect, useState} from "react";

const API_URL = process.env.REACT_APP_API_URL;

export function EmailVerify({onEmailVerify}) {
    const [loading, setLoading] = useState(true);
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();



    useEffect(() => {
        const verifyEmail = async (token) => {
            const controller = new AbortController();
            const timeoutId = setTimeout(() => controller.abort(), 8000); // 8 seconds timeout
            try {
                await fetch(API_URL + '/auth/email/validate', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Token': token
                    },
                    body: null,
                    signal: controller.signal
                })
                    .then(res => {
                        if (res.status === 400) {
                            navigate('/login', {state: {message: res.text()}});
                            return;
                        }
                        if (!res.ok) {
                            navigate('/login', {state: {message: 'An error occurred when verifying your email.'}});
                            return;
                        }
                        navigate('/login', {state: {message: 'Email verified! Please log in.'}});
                    });
            } catch (err) {
                navigate('/login', {state: {message: 'An error occurred when verifying your email.'}});
            } finally {
                setLoading(false);
                clearTimeout(timeoutId);
            }
        }
        const token = searchParams.get('token');
        if (token) {
            verifyEmail(token);
        } else {
            navigate('/login', {state: {message: 'Invalid verification link.'}});
            setLoading(false);
        }
    }, [searchParams, navigate] );


    if (loading) {
        return (<div style={{height: '95vh'}}><LoadingState/></div>)
    }

    return (
        <div />
    );
}
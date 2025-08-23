import {LoadingState} from "../dashboard/DashboardComponents";
import {useNavigate, useSearchParams} from "react-router-dom";
import {useEffect, useRef, useState} from "react";

const API_URL = process.env.REACT_APP_API_URL;

function DeleteAccount({onDeleteAccount}) {
    const [loading, setLoading] = useState(true);
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();
    const requestSentRef = useRef(false); // Add this ref to track request status

    useEffect(() => {
        const deleteAccount = async (token) => {
            if (requestSentRef.current) return;
            requestSentRef.current = true;
            const controller = new AbortController();
            const timeoutId = setTimeout(() => controller.abort(), 8000); // 8 seconds timeout
            try {
                await fetch(API_URL + '/auth/delete-account', {
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
                            navigate('/request-delete-account', {state: {message: res.text()}});
                            return;
                        }
                        if (!res.ok) {
                            console.log(res.status);
                            navigate('/request-delete-account', {state: {message: 'An error occurred when deleting your account.'}});
                            return;
                        }
                        navigate('/request-delete-account', {state: {message: 'Account successfully deleted!'}});
                    });
            } catch (err) {
                console.log(err);
                navigate('/request-delete-account', {state: {message: 'An error occurred when deleting your account.'}});
            } finally {
                setLoading(false);
                clearTimeout(timeoutId);
            }
        }
        const token = searchParams.get('token');
        if (token) {
            deleteAccount(token);
        } else {
            navigate('/request-delete-account', {state: {message: 'Invalid deletion link.'}});
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
export default DeleteAccount;

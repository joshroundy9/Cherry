import {useEffect, useState} from "react";
import {genericRequest, getCurrentDate, getDataHeaders} from "../utils/DashboardUtil";

const API_URL = process.env.REACT_APP_API_URL;

export function LoadingState () {
    return (
    <div className="Loading-container">
        <div className="Spinner"></div>
    </div>
    );
}

export function ErrorState ({ message }) {
    return (
        <div className="Error-container">
            <p>{message}</p>
        </div>
    );
}

export function DailyWeightInput ({date, dateId, setError}) {
    const [weight, setWeight] = useState('');
    const [loading, setLoading] = useState(false);

    const incrementWeight = () => {
        setWeight((prev) => Math.min(999, parseInt(prev || 0) + 1));
    };

    const decrementWeight = () => {
        setWeight((prev) => Math.max(0, parseInt(prev || 0) - 1));
    };

    const getDailyWeight = async () => {
        setLoading(true);

        try {
            const responseBody = await genericRequest(
                `${API_URL}/data/date/from-user-and-date?date=${date}`,
                'GET',
                getDataHeaders(),
                null);
            if (!responseBody || !responseBody.dailyWeight) {
                setWeight(localStorage.getItem('weight'));
            } else {
                setWeight(responseBody.dailyWeight);
            }
        } catch (err) {
            setError('Failed to fetch daily weight');
        } finally {
            setLoading(false);
        }
    }
    const updateDailyWeight = async () => {
        setLoading(true);
        try {
            await genericRequest(
                `${API_URL}/data/date/update-weight?dateid=${dateId}&weight=${weight}`,
                'POST',
                getDataHeaders(),
                null);
            setWeight(weight);
            if (date === getCurrentDate()) {
                localStorage.setItem('weight', weight);
                await genericRequest(
                    `${API_URL}/data/user/update-weight?weight=${weight}`,
                    'POST',
                    getDataHeaders(),
                    null
                )
            }
        } catch (err) {
            setError('Failed to update daily weight');
        } finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        if (dateId) {
            getDailyWeight();
        }
    }, []);

    if (loading) return <div className={"Info-message"}>Loading...</div>;
    return (
        <div className={"Daily-weight-container"}>
            <div style={{marginRight: '2px'}}>Daily Weight:</div>
            <input
                className={"Daily-weight-input"}
                type="number"
                value={weight}
                onChange={e => setWeight(e.target.value)}
                onBlur={updateDailyWeight}
                max={999}
                onKeyDown={e => {
                    if (e.key === "Enter") {
                        e.target.blur();
                    }
                }}
            />
            <div style={{marginLeft: '2px'}}>lbs</div>
        </div>
    );
}
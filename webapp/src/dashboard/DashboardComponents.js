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
            <div>{message}</div>
            <button className="Panel-footer-button" onClick={() => window.location.reload()}>Reload</button>
        </div>
    );
}

export function DailyWeightInput ({date, dateId, weight, setWeight, setError}) {
    const [loading, setLoading] = useState(false);

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
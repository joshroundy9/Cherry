import {useState} from "react";
import {genericRequest, getDataHeaders} from "./DataUtil";

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

export function DailyWeightInput (date, setError) {
    const [weight, setWeight] = useState('');
    const [dateId, setDateId] = useState(null);
    const [loading, setLoading] = useState(false);

    const getDailyWeight = async () => {
        setLoading(true);

        try {
            const responseBody = await genericRequest(
                `${API_URL}/data/date/from-user-and-date?userid=${localStorage.getItem("userId")}&date=${date}`,
                'GET',
                getDataHeaders(),
                null);
            setWeight(responseBody.dailyWeight);
            setDateId(responseBody.dateID);
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
        } catch (err) {
            setError('Failed to update daily weight');
        } finally {
            setLoading(false);
        }
    }

    if (loading) return <div className={"Info-message"}>Loading...</div>;

    return (
        <div className={"Daily-weight-input-container"}>
            <input
                className={"Daily-weight-input"}
                type="number"
                value={weight}
                onChange={e => setWeight(e.target.value)}
                onBlur={updateDailyWeight}
                placeholder="Enter your weight here"
            />
        </div>
    );
}
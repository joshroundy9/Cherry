import {useState} from "react";

const API_URL = process.env.REACT_APP_API_URL;

export const genericDataRequest = async (url, method, headers, body) => {
    const controller = new AbortController();
    const timeoutId = setTimeout(() => controller.abort(), 8000); // 8 seconds timeout
    try {
        const res = await fetch(url, {
            method: method,
            headers: headers,
            body: body,
            signal: controller.signal
        });
        if (!res.ok) throw new Error("Error making request: " + res.status);

        const text = await res.text();
        if (!text) return null;
        return JSON.parse(text);
    } catch (err) {
        throw err;
    } finally {
        clearTimeout(timeoutId);
    }
};

const updateMealTime = async (mealID, mealTime, setError) => {
    try {
        const parsableTime = mealTime + ':00'; // Ensure time is in HH:mm:ss format
        await genericDataRequest(
            `${API_URL}/data/meal/update-time?mealid=${mealID}&time=${parsableTime}`,
            'POST',
            {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ` + localStorage.getItem("jwtToken"),
                'User-ID': localStorage.getItem("userId")
            },
            null
        );
        setError(null);
    } catch (err) {
        setError(err.message);
    }
}

export function DailyWeightInput (date, setError) {
    const [weight, setWeight] = useState('');
    const [dateId, setDateId] = useState(null);
    const [loading, setLoading] = useState(false);

    const getDailyWeight = async () => {
        setLoading(true);

        try {
            const responseBody = await genericDataRequest(
                `${API_URL}/data/date/from-user-and-date?userid=${localStorage.getItem("userId")}&date=${date}`,
                'GET',
                {
                    'Authorization': `Bearer ` + localStorage.getItem("jwtToken"),
                    'User-ID': localStorage.getItem("userId")
                }, null);
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
            await genericDataRequest(
                `${API_URL}/data/date/update-weight?dateid=${dateId}&weight=${weight}`,
                'POST',
                {
                    'Authorization': `Bearer ` + localStorage.getItem("jwtToken"),
                    'User-ID': localStorage.getItem("userId")
                }, null);
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
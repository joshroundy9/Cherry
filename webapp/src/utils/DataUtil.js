
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
        if (!text) return null; // No content to parse
        return JSON.parse(text);
    } catch (err) {
        alert(`Request failed: ${err.message}`);
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
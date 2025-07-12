import { format, parseISO, parse } from 'date-fns';

const API_URL = process.env.REACT_APP_API_URL;

export const getDataHeaders = () => {
    return {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ` + localStorage.getItem("jwtToken"),
        'User-ID': localStorage.getItem("userId")
    }
}

export const genericRequest = async (url, method, headers, body) => {
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

export const updateDateNutrition = async (dateID, totalCalories, totalProtein, setError) => {
    try {
        await genericRequest(
            `${API_URL}/data/date/update-nutrition?dateid=${dateID}&calories=${totalCalories}&protein=${totalProtein}`,
            'POST',
            getDataHeaders(),
            null
        );
        setError(null);
    } catch (err) {
        setError(err.message);
    }
}

export const updateMealNutrition = async (mealID, totalCalories, totalProtein, setError) => {
    try {
        await genericRequest(
            `${API_URL}/data/meal/update-nutrition?mealid=${mealID}&calories=${totalCalories}&protein=${totalProtein}`,
            'POST',
            getDataHeaders(),
            null
        );
        setError(null);
    } catch (err) {
        setError(err.message);
    }
}

export const updateMealTime = async (mealID, mealTime, setError) => {
    try {
        const parsableTime = mealTime + ':00'; // Ensure time is in HH:mm:ss format
        await genericRequest(
            `${API_URL}/data/meal/update-time?mealid=${mealID}&time=${parsableTime}`,
            'POST',
            getDataHeaders(),
            null
        );
        setError(null);
    } catch (err) {
        setError(err.message);
    }
}

export function formatDateWithOrdinal(dateString) {
    const date = parseISO(dateString);
    return format(date, "MMMM do, yyyy"); // e.g., August 20th, 2024
}

export function formatTimeTo12Hour(timeString) {
    if (!timeString) return '';
    const date = parse(timeString, 'HH:mm:ss', new Date());
    return format(date, 'h:mm a'); // e.g., 2:47 PM
}
import React, { useState } from 'react';
import MealPanel from "./MealPanel";

const API_URL = process.env.REACT_APP_API_URL;

function NutritionForm({ addMealItem }) {
    const [foodEntry, setFoodEntry] = useState('');
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(false);

    const handleSubmit = async (e) => {
        if (loading) return; // Prevent multiple submissions
        setLoading(true);

        e.preventDefault();
        setError(null);
        try {
            const response = await fetch(`${API_URL}/ai/nutritiondata`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                    'Food-Entry': foodEntry,
                    'Authorization': `Bearer ` + localStorage.getItem("jwtToken")
                },
                body: ''
            });
            if (response.ok) {
                const data = await response.json();
                addMealItem(data.foodEntry, data.calories, data.protein);
                setFoodEntry('')
            } else {
                setError('Request failed: ' + response.status + ' ' + response.statusText);
            }
        } catch (err) {
            setError('Network error: ' + err.message);
        }
    };

    return (
        <form className={"Nutrition-form"} onSubmit={handleSubmit}>
            <input
                className={"Nutrition-form-input"}
                type="text"
                value={foodEntry}
                onChange={e => setFoodEntry(e.target.value)}
                placeholder="Describe what you ate here, the more specific the better!"
            />
            <button className={"Nutrition-form-button"} type="submit">Add New Item</button>
            {error && <div className={"Error-message"}>{error}</div>}
        </form>
    );
}

export default NutritionForm;
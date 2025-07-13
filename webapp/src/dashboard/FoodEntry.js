import React, { useState } from 'react';

const API_URL = process.env.REACT_APP_API_URL;

function NutritionForm({ addMealItem, numberOfMealItems }) {
    const [foodEntry, setFoodEntry] = useState('');
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(false);

    const handleSubmit = async (e) => {
        if (loading) return;
        setLoading(true);

        e.preventDefault();
        setError('');
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
                if (data.isValidEntry) {
                    addMealItem(data.foodEntry, data.calories, data.protein, setError);
                } else {
                    setError('Invalid food entry. Please try again with a more specific description.');
                }
                setFoodEntry('')
                setLoading(false)
            } else {
                setError('Request failed: ' + response.status + ' ' + response.statusText);
            }
        } catch (err) {
            setError('Network error: ' + err.message);
        }
    };

    return (
        <div className={"Nutrition-form-container"}>
            <form className={"Nutrition-form"} onSubmit={handleSubmit}>
                <input
                    className={"Nutrition-form-input"}
                    type="text"
                    value={foodEntry}
                    onChange={e => setFoodEntry(e.target.value)}
                    placeholder="Enter what you ate here, the more specific the better!"
                />
                <button className={"Nutrition-form-button"} type="submit">Add New Item</button>
            </form>
            <div className={"Nutrition-form-error"}>
                {error && <div className={"Error-message"}>{error}</div>}
                {numberOfMealItems() >= 10 && <div className={"Error-message"}>Meal item limit reached!</div>}
            </div>
        </div>
    );
}

export default NutritionForm;
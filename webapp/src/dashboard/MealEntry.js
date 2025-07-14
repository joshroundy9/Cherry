import React, { useState } from 'react';

function MealEntry({ addMeal, numberOfMeals }) {
    const [mealName, setMealName] = useState('');
    const [mealTime, setMealTime] = useState('');
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(false);

    const handleSubmit = async (e) => {
        if (loading) return;
        setLoading(true);
        e.preventDefault();
        addMeal(mealName, mealTime, setError);
        setMealName('');
        setMealTime('');
        setLoading(false);
    };

    return (
        <div className={"Nutrition-form-container"}>
            <form className={"Meal-form"} onSubmit={handleSubmit}>
                <input
                    className={"Meal-form-input"}
                    type="text"
                    value={mealName}
                    onChange={e => setMealName(e.target.value)}
                    placeholder="Enter meal name here"
                    required={true}
                />
                <input
                    className={"DateTime-meal-input"}
                    type="time"
                    value={mealTime}
                    onChange={e => setMealTime(e.target.value)}
                    placeholder=""
                    required={true}
                />
                <button className={"Nutrition-form-button"} type="submit">Add New Meal</button>
            </form>
            <div className={"Nutrition-form-error"}>
                {error && <div className={"Error-message"}>{error}</div>}
                {numberOfMeals() >= 8 && <div className={"Error-message"}>Meal item limit reached!</div>}
            </div>
        </div>
    );
}

export default MealEntry;
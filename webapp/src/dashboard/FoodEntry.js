import React, { useState } from 'react';

const API_URL = process.env.REACT_APP_API_URL;

function NutritionForm() {
    const [foodEntry, setFoodEntry] = useState('');
    const [result, setResult] = useState(null);
    const [error, setError] = useState(null);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(null);
        setResult(null);
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
                setResult(data);
            } else {
                setError('Request failed: ' + response.status + ' ' + response.statusText);
            }
        } catch (err) {
            setError('Network error: ' + err.message);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <input
                type="text"
                value={foodEntry}
                onChange={e => setFoodEntry(e.target.value)}
                placeholder="Enter food"
            />
            <button type="submit">Submit</button>
            {error && <div style={{ color: 'red' }}>{error}</div>}
            {result && <pre>{JSON.stringify(result, null, 2)}</pre>}
        </form>
    );
}

export default NutritionForm;
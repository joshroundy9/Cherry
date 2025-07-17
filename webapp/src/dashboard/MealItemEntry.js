import React, { useState, useEffect, useRef } from 'react';

const API_URL = process.env.REACT_APP_API_URL;
const MODES = ['AI', 'Manual', 'Recents'];

function NutritionForm({ addMealItem, numberOfMealItems }) {
    const [foodEntry, setFoodEntry] = useState('');
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(false);
    const [mode, setMode] = useState(localStorage.getItem('nutritionInputMode') || 'AI');
    const [dropdownOpen, setDropdownOpen] = useState(false);
    const dropdownRef = useRef(null);

    useEffect(() => {
        localStorage.setItem('nutritionInputMode', mode);
    }, [mode]);

    useEffect(() => {
        function handleClickOutside(event) {
            if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
                setDropdownOpen(false);
            }
        }
        document.addEventListener('mousedown', handleClickOutside);
        return () => document.removeEventListener('mousedown', handleClickOutside);
    }, []);

    const handleModeChange = (opt) => {
        setMode(opt);
        setFoodEntry('');
        setError('');
        setDropdownOpen(false);
    };

    const handleSubmit = async (e) => {
        if (loading) return;
        setLoading(true);

        e.preventDefault();
        setError('');
        if (mode === 'Manual') {
            const [food, calories, protein] = foodEntry.split(',');
            if (food && calories && protein) {
                addMealItem(food.trim(), Number(calories), Number(protein), setError);
                setFoodEntry('');
            } else {
                setError('Enter as: food,calories,protein');
            }
            setLoading(false);
            return;
        }
        if (mode === 'Recents') {
            setError('Recents mode not implemented.');
            setLoading(false);
            return;
        }
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
                setFoodEntry('');
                setLoading(false);
            } else {
                setError('Request failed: ' + response.status + ' ' + response.statusText);
            }
        } catch (err) {
            setError('Network error: ' + err.message);
        }
    };

    return (
        <div className={"Nutrition-form-container"}>
            <form className={"Nutrition-form"} onSubmit={handleSubmit} style={{ display: 'flex', alignItems: 'center' }}>
                <div ref={dropdownRef} style={{ position: 'relative', marginRight: '8px' }}>
                    <button
                        type="button"
                        onClick={() => setDropdownOpen((open) => !open)}
                        style={{
                            background: 'white',
                            border: '1px solid #ccc',
                            borderRadius: '4px',
                            width: '32px',
                            height: '32px',
                            fontSize: '20px',
                            cursor: 'pointer',
                            padding: 0,
                        }}
                        aria-label="Select input mode"
                    >
                        ⌄
                    </button>
                    {dropdownOpen && (
                        <ul style={{
                            position: 'absolute',
                            left: 0,
                            top: '110%',
                            background: 'white',
                            border: '1px solid #ccc',
                            borderRadius: '4px',
                            listStyle: 'none',
                            margin: 0,
                            padding: 0,
                            zIndex: 10,
                            minWidth: '80px'
                        }}>
                            {MODES.map(opt => (
                                <li key={opt}>
                                    <button
                                        type="button"
                                        onClick={() => handleModeChange(opt)}
                                        style={{
                                            background: 'none',
                                            border: 'none',
                                            width: '100%',
                                            textAlign: 'left',
                                            padding: '8px',
                                            cursor: 'pointer'
                                        }}
                                    >
                                        {opt}
                                    </button>
                                </li>
                            ))}
                        </ul>
                    )}
                </div>
                {mode === 'Manual' ? (
                    <input
                        className={"Nutrition-form-input"}
                        type="text"
                        value={foodEntry}
                        onChange={e => setFoodEntry(e.target.value)}
                        placeholder="food,calories,protein"
                    />
                ) : mode === 'Recents' ? (
                    <input
                        className={"Nutrition-form-input"}
                        type="text"
                        value={foodEntry}
                        onChange={e => setFoodEntry(e.target.value)}
                        placeholder="(Recents mode not implemented)"
                        disabled
                    />
                ) : (
                    <input
                        className={"Nutrition-form-input"}
                        type="text"
                        value={foodEntry}
                        onChange={e => setFoodEntry(e.target.value)}
                        placeholder="Enter what you ate here, the more specific the better!"
                    />
                )}
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
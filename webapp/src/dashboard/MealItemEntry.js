import React, { useState, useEffect, useRef } from 'react';
import {getMealItemRecents} from "../utils/DashboardUtil";

const API_URL = process.env.REACT_APP_API_URL;
const MODES = ['AI', 'Manual', 'Recents'];

function NutritionForm({ addMealItem, setError }) {
    const [foodEntry, setFoodEntry] = useState('');
    const [calories, setCalories] = useState('');
    const [protein, setProtein] = useState('');
    const [recents, setRecents] = useState([]);
    const [loading, setLoading] = useState(false);
    const [mode, setMode] = useState(localStorage.getItem('nutritionInputMode') || 'AI');
    const [dropdownOpen, setDropdownOpen] = useState(false);
    const dropdownRef = useRef(null);

    useEffect(() => {
        localStorage.setItem('nutritionInputMode', mode);
        if (mode === 'Recents') {
            getRecents();
        }
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
        if (opt === 'Recents') {
            getRecents();
        }
    };

    const getRecents = () => {
        setLoading(true)
        getMealItemRecents(setError)
            .then(data => {
                if (data.length > 0) {
                    setRecents(data);
                } else {
                    setRecents([]);
                }
            }).finally(() => setLoading(false));}

    const handleSubmit = async (e) => {
        if (loading) return;
        setLoading(true);

        e.preventDefault();
        setError('');
        if (mode === 'Manual') {
            if (foodEntry && calories && protein) {
                addMealItem(foodEntry, Number(calories), Number(protein), false, setError);
                setFoodEntry('');
                setCalories('');
                setProtein('');
            } else {
                setError('All fields are required in Manual mode.');
            }
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
                    addMealItem(data.foodEntry, data.calories, data.protein, true, setError);
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
        setLoading(false);
    };

    return (
        <div className={"Nutrition-form-container"}>
            <form className={"Nutrition-form"} onSubmit={handleSubmit} style={{ display: 'flex', alignItems: 'center' }}>
                <div ref={dropdownRef} style={{position: 'relative', marginRight: '0.5%' }}>
                    <button
                        type="button"
                        onClick={() => setDropdownOpen((open) => !open)}
                        className={`Nutrition-form-dropdown${dropdownOpen ? ' open' : ''}`}
                        aria-label="Select input mode"
                    >

                    </button>
                    {dropdownOpen && (
                        <ul className={"Nutrition-form-dropdown-menu"}>
                            {MODES.map(opt => (
                                <li key={opt}>
                                    <button
                                        type="button"
                                        onClick={() => handleModeChange(opt)}
                                        className={"Nutrition-form-dropdown-menu-item"}
                                    >
                                        {opt}
                                    </button>
                                </li>
                            ))}
                        </ul>
                    )}
                </div>
                {mode === 'Manual' && (
                    <div style={{ display: 'flex', flexDirection: 'row', width: '100%' }}>
                        <input
                            className={"Nutrition-form-input Nutrition-form-description-input"}
                            type="text"
                            value={foodEntry}
                            onChange={e => setFoodEntry(e.target.value)}
                            placeholder="Enter what you ate here"
                            maxLength={100}
                            required={true}
                        />
                        <input
                            className={"Nutrition-form-input Nutrition-form-basic-input"}
                            type="number"
                            value={calories}
                            onChange={e => setCalories(e.target.value)}
                            placeholder="Calories"
                            min={0}
                            max={10000}
                            required={true}
                        />
                        <input
                            className={"Nutrition-form-input Nutrition-form-basic-input"}
                            type="number"
                            value={protein}
                            onChange={e => setProtein(e.target.value)}
                            placeholder="Protein"
                            min={0}
                            max={1000}
                            required={true}
                        />
                    </div>
                )}
                {mode === 'AI' && (
                    <input
                        className={"Nutrition-form-input"}
                        type="text"
                        value={foodEntry}
                        onChange={e => setFoodEntry(e.target.value)}
                        placeholder="Enter what you ate here, the more specific the better!"
                        maxLength={100}
                        required={true}
                    />
                )}
            {mode === 'Recents' && (
                recents.length > 0 ? (
                <div className={"Nutrition-form-recents-header"}>
                    Select a recent manual meal item to add
                </div>
                ) : (
                    <div className={"Nutrition-form-recents-header"}>
                        No recent meal items found. Please add some manually first.
                    </div>
                )
            )}

                {mode !== 'Recents' &&   (
                    <button className={"Nutrition-form-button"} type="submit">Add New Item</button>
                )}
            </form>
            {(mode === 'Recents' && loading === false) && (
                <div className={"Nutrition-form-recents"}>
                    {loading ? (
                        <div className={"Info-message"}>Loading recents...</div>
                    ) : (

                            recents.map(item => (
                                <button className={"MealItemList-li Meal-form-input Nutrition-form-recents-item"}
                                        style={{paddingTop: '0.4vh', paddingBottom: '0.5vh', border: 'none', cursor: 'pointer', width: '100%'}}
                                        key={item.itemID}
                                        onClick={() => {
                                            addMealItem(item.itemName, item.itemCalories, item.itemProtein, true, setError);
                                            setFoodEntry('');
                                        }}
                                >
                                    <div className={"Meal-name-wrapper"} style={{color: 'inherit'}}>{item.itemName}</div>
                                    <div/>
                                    <div>{item.itemCalories}</div>
                                    <div/>
                                    <div style={{marginRight: '1.5vw'}}>{item.itemProtein}g</div>
                                    <div className={"Delete-button"}
                                            style={{visibility: 'hidden'}}>
                                        X
                                    </div>
                                </button>
                            ))
                    )}
                </div>
            )}
        </div>
    );
}

export default NutritionForm;
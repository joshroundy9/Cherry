import {useEffect, useState} from "react";
import NutritionForm from "./FoodEntry";

const API_URL = process.env.REACT_APP_API_URL;

function MealPanel({ mealId, date }) {
    const [mealItems, setMealItems] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const addMealItem = (description, calories, protein) => {
        mealItems.push({description, calories, protein})
    }

    const removeMealItem = (itemId) => {
        mealItems(prevItems => prevItems.filter(item => item.id !== itemId));
    }

    useEffect(() => {
        if (!mealId) return;
        setLoading(true);
        fetch(`${API_URL}/data/meal-item`, {
            method: 'GET',
                headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'MealID': mealId,
                'Authorization': `Bearer ` + localStorage.getItem("jwtToken")
        }})
            .then(res => {
                if (!res.ok) throw new Error("Failed to fetch meal items");
                return res.json();
            })
            .then(data => {
                setMealItems(data);
                setError(null);
            })
            .catch(err => setError(err.message))
            .finally(() => setLoading(false));
    }, [mealId]);

    if (loading) return <div>Loading...</div>;
    if (error) return <div>Error: {error}</div>;

    return (
        <div className="MealPanel">
            <div className="MealPanel-header">
                <p>Meal #${mealId}</p> <!-- TODO: Update with simplified meal number from the date -->
            </div>
            <form>
                <div className={"MealPanel-date-header"}>
                    <input type={"datetime-local"} value={date} />
                </div>
                <div className={"MealItemList-header"}>

                </div>
                <div className={"MealItemList"}>
                    <ul>
                        {mealItems.map(item => (
                            <li key={item.id}>
                                <p>{item.name}</p>
                                <p>{item.calories}</p>
                                <p>{item.protein}</p>
                                <button className={"Delete-button"} type={"button"} onClick={() => removeMealItem(item.id)}>
                                    x
                                </button>
                            </li>
                        ))}
                    </ul>
                    < NutritionForm addMealItem={addMealItem} />
                </div>
            </form>
        </div>
    );
}

export default MealPanel;
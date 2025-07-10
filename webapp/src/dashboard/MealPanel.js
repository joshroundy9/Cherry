import {useEffect, useState} from "react";
import NutritionForm from "./FoodEntry";
import { genericDataRequest } from "../utils/DataUtil";

const API_URL = process.env.REACT_APP_API_URL;

function MealPanel({ mealId, date, dateId }) {
    const [mealItems, setMealItems] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const addMealItem = async (itemName, itemCalories, itemProtein) => {
        try {
            const responseBody = await genericDataRequest(
                `${API_URL}/data/meal-item`,
                'POST',
                {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ` + localStorage.getItem("jwtToken")
                },
                `{
                "userID": "${localStorage.getItem("userId")}",
                "dateID": "${dateId}",
                "mealID": "${mealId}",
                "itemName": "${itemName}",
                "itemCalories": "${itemCalories}",
                "itemProtein": "${itemProtein}"
            }`
            );
            setMealItems(prevItems => [
                ...prevItems,
                responseBody
            ]);
            setError(null);
        } catch (err) {
            setError(err.message);
        }
    };

    const removeMealItem = async (itemId) => {
        try {
            await genericDataRequest(
                `${API_URL}/data/meal-item/delete?mealitemid=${itemId}`, 'DELETE', {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ` + localStorage.getItem("jwtToken")
                },
                null
            );
            setMealItems(prevItems => prevItems.filter(item => item.itemID !== itemId));
            setError(null);
        } catch (err) {
            setError(err.message);
        }
    }

    useEffect(() => {
        if (!mealId) return;
        setLoading(true);

        const controller = new AbortController();
        const timeoutId = setTimeout(() => controller.abort(), 8000); // 8 seconds

        fetch(`${API_URL}/data/meal-item?mealid=${mealId}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'Authorization': `Bearer ` + localStorage.getItem("jwtToken")
            },
            signal: controller.signal
        })
            .then(res => {
                if (!res.ok) throw new Error("Failed to fetch meal items");
                return res.json();
            })
            .then(data => {
                setMealItems(data);
                setError(null);
            })
            .catch(err => {
                if (err.name === 'AbortError') {
                    setError('Request timed out');
                } else {
                    setError(err.message);
                }
            })
            .finally(() => {
                clearTimeout(timeoutId);
                setLoading(false);
            });

        return () => {
            clearTimeout(timeoutId);
            controller.abort();
        };
    }, [mealId]);

    if (loading) return <div>Loading...</div>;
    if (error) return <div>Error: {error}</div>;

    return (
        <div className="MealPanel">
            <div className="MealPanel-header">
                <p>Meal #{mealId}</p>
            </div>
            <div className={"MealPanel-date-header"}>
                <input type={"datetime-local"} value={date} />
            </div>
            <div className={"MealItemList-header"}>

            </div>
            <div className={"MealItemList"}>
                <ul>
                    {mealItems.map(item => (
                        <li key={item.itemID}>
                            <p>{item.itemName}</p>
                            <p>{item.itemCalories}</p>
                            <p>{item.itemProtein}</p>
                            <button className={"Delete-button"} type={"button"} onClick={() => removeMealItem(item.itemID)}>
                                x
                            </button>
                        </li>
                    ))}
                </ul>
            </div>
            < NutritionForm addMealItem={addMealItem} />
        </div>
    );
}

export default MealPanel;
import {useEffect, useState} from "react";
import NutritionForm from "./FoodEntry";
import { genericDataRequest } from "../utils/DataUtil";
import {ErrorState, LoadingState} from "../utils/DashboardUtil";

const API_URL = process.env.REACT_APP_API_URL;

function MealPanel({ mealId, time, date, dateId }) {
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
                    'Authorization': `Bearer ` + localStorage.getItem("jwtToken"),
                    'User-ID': localStorage.getItem("userId")
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
                    'Authorization': `Bearer ` + localStorage.getItem("jwtToken"),
                    'User-ID': localStorage.getItem("userId")
                },
                null
            );
            setMealItems(prevItems => prevItems.filter(item => item.itemID !== itemId));
            setError(null);
        } catch (err) {
            setError(err.message);
        }
    }

    const updateMealTime = async (mealTime) => {
        try {
            const parsableTime = mealTime + ':00'; // Ensure time is in HH:mm:ss format
            await genericDataRequest(
                `${API_URL}/data/meal/update-time?mealid=${mealId}&time=${parsableTime}`,
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

    useEffect(() => {
        if (!mealId) return;
        setLoading(true);

        const controller = new AbortController();
        const timeoutId = setTimeout(() => controller.abort(), 8000); // 8 seconds

        fetch(`${API_URL}/data/meal-item?mealid=${mealId}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'Authorization': `Bearer ` + localStorage.getItem("jwtToken"),
                'User-ID': localStorage.getItem("userId")
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

    if (loading) return <LoadingState />;
    if (error) return <ErrorState message={error} />;

    return (
        <div className="MealPanel">
            <div className="MealPanel-header">
                <p>Meal #{mealId}</p>
            </div>
            <div className={"MealPanel-date-header"}>
                <div className={"DateTime-wrapper"}>
                    <p className={"DateTime-text"}>{date}</p>
                    <input
                        className={"DateTime-input"}
                        type={"time"}
                        defaultValue={time}
                        onBlur={e => updateMealTime(e.target.value)}
                    />
                </div>
                <p>Add Meal Items</p>
            </div>
            <div className={"MealItemList-header"}>

            </div>
            <div className={"MealItemList"}>
                <ul className={"MealItemList-ul"}>
                    <div className={"MealItemList-li"}>
                        <p style={{marginLeft: '1vw', color: 'white'}}>Item Name</p>
                        <p></p>
                        <p style={{marginRight: '10vw', color: 'white'}}>Calories</p>
                        <p style={{marginRight: '5vw', color: 'white'}}>Protein</p>
                    </div>
                </ul>
                <ul className={"MealItemList-ul"}>
                    {mealItems.map(item => (
                        <li className={"MealItemList-li"} key={item.itemID}>
                            <p style={{marginLeft:'1vw'}}>{item.itemName}</p>
                            <p></p>
                            <p style={{marginRight:'10vw'}}>{item.itemCalories}</p>
                            <p style={{marginRight:'5vw'}}>{item.itemProtein}g</p>
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
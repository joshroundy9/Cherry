import {useEffect, useState} from "react";
import NutritionForm from "./MealItemEntry";
import {genericRequest, getDataHeaders, updateDateNutrition, updateMealNutrition} from "../utils/DashboardUtil";
import {ErrorState, LoadingState} from "./DashboardComponents";

const API_URL = process.env.REACT_APP_API_URL;

function MealPanel({switchPanel, mealName, mealId, time, date, dateId }) {
    const [mealItems, setMealItems] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const numberOfMealItems = () => {
        return mealItems.length;
    }

    const totalCalories = (itemList) => {
        return itemList.reduce((acc, item) => acc + item.itemCalories, 0);
    }

    const totalProtein = (itemList) => {
        return itemList.reduce((acc, item) => acc + item.itemProtein, 0);
    }

    const goBack = () => {
        localStorage.setItem('dateId', dateId);
        localStorage.setItem('date', date);
        switchPanel('date');
    }

    const addMealItem = async (itemName, itemCalories, itemProtein, setLocalError) => {
        try {
            const responseBody = await genericRequest(
                `${API_URL}/data/meal-item`,
                'POST',
                getDataHeaders(),
                `{
                "userID": "${localStorage.getItem("userId")}",
                "dateID": "${dateId}",
                "mealID": "${mealId}",
                "itemName": "${itemName}",
                "itemCalories": "${itemCalories}",
                "itemProtein": "${itemProtein}"
            }`
            );
            const newMealItems = [...mealItems, responseBody];
            setMealItems(newMealItems);
            await updateMealNutrition(mealId,
                totalCalories(newMealItems),
                totalProtein(newMealItems),
                setLocalError);
            setLocalError('');
        } catch (err) {
            setLocalError(err.message);
        }
    };

    const removeMealItem = async (itemId) => {
        try {
            await genericRequest(
                `${API_URL}/data/meal-item/delete?mealitemid=${itemId}`,
                'DELETE',
                getDataHeaders(),
                null
            );
            const newMealItems = mealItems.filter(item => item.itemID !== itemId);
            setMealItems(newMealItems);
            await updateMealNutrition(mealId,
                totalCalories(newMealItems),
                totalProtein(newMealItems),
                setError);
            setError('');
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
            headers: getDataHeaders(),
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
                setError('');
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
            <div className="Panel-header">
                {mealName}
            </div>
            <div className={"MealPanel-date-header"}>
                <div className={"DateTime-wrapper"}>
                    <div className={"DateTime-text"}>{date}</div>
                    <input
                        className={"DateTime-input"}
                        type={"time"}
                        defaultValue={time}
                        disabled={true}
                    />
                </div>
                <div className={"MealPanel-date-header-text"}>Add Meal Items</div>
            </div>
            <div className={"MealItemList-header"}>

            </div>
            <div className={"MealItemList"}>
                <ul className={"MealItemList-ul"}>
                    <li className={"MealItemList-li"}>
                        <div style={{color: 'white'}}>Food Description</div>
                        <div/>
                        <div style={{marginRight: '0vw', color: 'white'}}>Calories</div>
                        <div/>
                        <div style={{marginRight: '1.5vw', color: 'white'}}>Protein</div>
                        <button className={"Delete-button"} style={{visibility: 'hidden'}} type={"button"}>X</button>
                    </li>
                </ul>
                <ul className={"MealItemList-ul"}>
                    {mealItems.map(item => (
                        <li className={"MealItemList-li"} style={{paddingTop: '0.4vh', paddingBottom: '0.5vh'}} key={item.itemID}>
                            <div className={"Meal-name-wrapper Hover-expand"}>{item.itemName}</div>
                            <div/>
                            <div>{item.itemCalories}</div>
                            <div/>
                            <div style={{marginRight: '1.5vw'}}>{item.itemProtein}g</div>
                            <button className={"Delete-button Hover-expand"} type={"button"}
                                    onClick={() => removeMealItem(item.itemID)}>
                                X
                            </button>
                        </li>
                    ))}
                </ul>
            </div>
            < NutritionForm addMealItem={addMealItem} numberOfMealItems={numberOfMealItems} />
            <div className={"MealPanel-footer"}>
                <div className={"MealPanel-footer-text-container"}>
                    <div className={"MealPanel-footer-text"}>Total Calories: {totalCalories(mealItems)}</div>
                    <div className={"MealPanel-footer-text"}>Total Protein: {totalProtein(mealItems)}g</div>
                </div>
                <button className={"Panel-footer-button"} type={"button"} onClick={goBack}>Go Back</button>
            </div>
        </div>
    );
}

export default MealPanel;
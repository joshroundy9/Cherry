import {useEffect, useState} from "react";
import NutritionForm from "./FoodEntry";
import { useNavigate } from 'react-router-dom';
import {DailyWeightInput, genericDataRequest} from "../utils/DataUtil";
import {ErrorState, LoadingState} from "../utils/DashboardUtil";

const API_URL = process.env.REACT_APP_API_URL;

function DatePanel({ dateId, date }) {
    const [meals, setMeals] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const navigate = useNavigate();

    const numberOfMeals = () => {
        return meals.length;
    }

    const goBack = () => {
        navigate('/dashboard', {state: {flex: 'none'}});
    }

    const addMeal = async (mealName, mealTime) => {
        try {
            const responseBody = await genericDataRequest(
                `${API_URL}/data/meal`,
                'POST',
                {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ` + localStorage.getItem("jwtToken"),
                    'User-ID': localStorage.getItem("userId")
                },
                `{
                "userID": "${localStorage.getItem("userId")}",
                "dateID": "${dateId}",
                "mealName": "${mealName}",
                "time": "${mealTime}"
            }`
            );
            setMeals(prevItems => [
                ...prevItems,
                responseBody
            ]);
            setError('');
        } catch (err) {
            setError(err.message);
        }
    };

    const removeMeal = async (mealId) => {
        try {
            await genericDataRequest(
                `${API_URL}/data/meal/delete?mealid=${mealId}`, 'DELETE', {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ` + localStorage.getItem("jwtToken"),
                    'User-ID': localStorage.getItem("userId")
                },
                null
            );
            setMeals(prevItems => prevItems.filter(item => item.mealID !== mealId));
            setError(null);
        } catch (err) {
            setError(err.message);
        }
    }

    useEffect(() => {
        if (!dateId) return;
        setLoading(true);

        const controller = new AbortController();
        const timeoutId = setTimeout(() => controller.abort(), 8000); // 8 seconds

        fetch(`${API_URL}/data/meal?dateid=${dateId}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'Authorization': `Bearer ` + localStorage.getItem("jwtToken"),
                'User-ID': localStorage.getItem("userId")
            },
            signal: controller.signal
        })
            .then(res => {
                if (!res.ok) throw new Error("Failed to fetch meals");
                return res.json();
            })
            .then(data => {
                setMeals(data);
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
    }, [dateId]);

    if (loading) return <LoadingState />;
    if (error) return <ErrorState message={error} />;

    return (
        <div className="MealPanel">
            <div className="MealPanel-header">
                Date #{dateId}
            </div>
            <div className={"MealPanel-date-header"}>
                <div className={"DateTime-wrapper"}>
                    <div className={"DateTime-text"}>{date}</div>
                    <DailyWeightInput date={date} setError={setError} />
                </div>
                <div className={"DatePanel-date-header-text"}>Add Date Items</div>
            </div>
            <div className={"DateItemList-header"}>

            </div>
            <div className={"DateItemList"}>
                <ul className={"DateItemList-ul"}>
                    <li className={"DateItemList-li"}>
                        <div style={{color: 'white'}}>Meal</div>
                        <div/>
                        <div style={{marginRight: '0vw', color: 'white'}}>Time of Meal</div>
                        <div/>
                        <div style={{marginRight: '0vw', color: 'white'}}>Calories</div>
                        <div/>
                        <div style={{marginRight: '1.5vw', color: 'white'}}>Protein</div>
                        <button className={"Delete-button"} style={{visibility: 'hidden'}} type={"button"}>x</button>
                    </li>
                </ul>
                <ul className={"DateItemList-ul"}>
                    {meals.map(item => (
                        <li className={"DateItemList-li"} key={item.itemID}>
                            <div>{item.mealName}</div>
                            <div/>
                            <div>{item.itemCalories}</div>
                            <div/>
                            <div style={{marginRight: '1.5vw', color: '#19A9FA'}}>{item.itemProtein}g</div>
                            <button className={"Delete-button"} type={"button"}
                                    onClick={() => removeMeal(item.itemID)}>
                                X
                            </button>
                        </li>
                    ))}
                </ul>
            </div>
            < NutritionForm addDateItem={addMeal} numberOfDateItems={numberOfMeals} />
            <div className={"DatePanel-footer"}>
                <div className={"DatePanel-footer-text-container"}>
                    <div className={"DatePanel-footer-text"}>Total Calories: {meals.reduce((acc, item) => acc + item.itemCalories, 0)}</div>
                    <div className={"DatePanel-footer-text"}>Total Protein: {meals.reduce((acc, item) => acc + item.itemProtein, 0)}g</div>
                </div>
                <button className={"DatePanel-footer-button"} type={"button"} onClick={goBack}>Go Back</button>
            </div>
        </div>
    );
}

export default DatePanel;
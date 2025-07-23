import {useEffect, useState} from "react";
import {
    formatDateWithOrdinal, formatTimeTo12Hour,
    genericRequest,
    getDataHeaders,
    updateDateNutrition,
    validateTimeString
} from "../utils/DashboardUtil";
import {DailyWeightInput, ErrorState, LoadingState} from "./DashboardComponents";
import MealEntry from "./MealEntry";

const API_URL = process.env.REACT_APP_API_URL;

function DatePanel({ switchPanel, dateId, date, weight, setWeight }) {
    const [meals, setMeals] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const numberOfMeals = () => {
        return meals.length;
    }

    const totalCalories = (itemList) => {
        return itemList.reduce((acc, item) => acc + item.mealCalories, 0);
    }

    const totalProtein = (itemList) => {
        return itemList.reduce((acc, item) => acc + item.mealProtein, 0);
    }

    const goBack = () => {
        localStorage.setItem('dateId', '');
        localStorage.setItem('date', '');
        switchPanel('');
    }

    const goToMealPanel = (mealName, mealId, time) => {
        localStorage.setItem('mealName', mealName);
        localStorage.setItem('mealId', mealId);
        localStorage.setItem('time', time);
        localStorage.setItem('dateId', dateId);
        localStorage.setItem('date', date);
        switchPanel('meal');
    }

    const addMeal = async (mealName, mealTime, setLocalError) => {
        try {
            const validMealTime = validateTimeString(mealTime);
            const responseBody = await genericRequest(
                `${API_URL}/data/meal`,
                'POST',
                getDataHeaders(),
                `{
                "userID": "${localStorage.getItem("userId")}",
                "dateID": "${dateId}",
                "mealName": "${mealName}",
                "time": "${validMealTime}"
            }`
            );
            const newMeals = [...meals, responseBody];
            setMeals(newMeals);
            await updateDateNutrition(dateId,
                totalCalories(newMeals),
                totalProtein(newMeals),
                setLocalError);
            setLocalError('');
        } catch (err) {
            setLocalError(err.message);
        }
    };

    const removeMeal = async (mealId) => {
        try {
            await genericRequest(
                `${API_URL}/data/meal/delete?mealid=${mealId}`, 'DELETE', getDataHeaders(),
                null
            );
            const newMeals = meals.filter(item => item.mealID !== mealId)
            setMeals(newMeals);
            await updateDateNutrition(dateId,
                totalCalories(newMeals),
                totalProtein(newMeals),
                setError);
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
            headers: getDataHeaders(),
            signal: controller.signal
        })
            .then(res => {
                if (!res.ok) throw new Error("Failed to fetch meals");
                return res.json();
            })
            .then(data => {
                setMeals(data);
                const dailyCalories = totalCalories(data);
                const dailyProtein = totalProtein(data);
                if (dailyCalories !== localStorage.getItem('dailyCalories') ||
                    dailyProtein !== localStorage.getItem('dailyProtein')) {
                    updateDateNutrition(dateId, dailyCalories, dailyProtein, setError).then();
                }
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
                setError('');
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
            <div className={"MealPanel-container"}>
                <div className="Panel-header">
                    {formatDateWithOrdinal(date)}
                </div>
                <div className={"MealPanel-date-header"}>
                    <div className={"DateTime-wrapper"}>
                        <DailyWeightInput date={date} dateId={dateId} weight={weight} setWeight={setWeight}
                                          setError={setError}/>
                    </div>
                    <div className={"MealPanel-date-header-text"}>Add Meals</div>
                </div>
                <div className={"MealItemList"}>
                    <ul className={"MealItemList-ul"}>
                        <li className={"MealItemList-li"}>
                            <div style={{color: 'white'}}>Meal Name</div>
                            <div/>
                            <div style={{marginRight: '0vw', color: 'white'}}>Calories</div>
                            <div/>
                            <div style={{marginRight: '1.5vw', color: 'white'}}>Protein</div>
                            <button className={"Delete-button"} style={{visibility: 'hidden'}} type={"button"}>X
                            </button>
                        </li>
                    </ul>
                    <ul className={"MealItemList-ul"}>
                        {meals.map(item => (
                            <li className={"MealItemList-li"} style={{paddingTop: '1.5vh', paddingBottom: '1.5vh'}}
                                key={item.mealID}>
                                <button type={'button'}
                                        onClick={() => goToMealPanel(item.mealName, item.mealID, item.time)}
                                        className={'Meal-name-wrapper Hover-expand'}>{item.mealName}
                                    <div style={{color: 'white'}}>{formatTimeTo12Hour(item.time)}</div>
                                </button>
                                <div/>
                                <div>{item.mealCalories}</div>
                                <div/>
                                <div style={{marginRight: '1.5vw'}}>{item.mealProtein}g</div>
                                <button className={"Delete-button Hover-expand"} type={"button"}
                                        onClick={() => removeMeal(item.mealID)}>
                                    X
                                </button>
                            </li>
                        ))}
                    </ul>
                </div>
                < MealEntry addMeal={addMeal} numberOfMeals={numberOfMeals}/>
            </div>
            <div className={"MealPanel-footer-container"}>
                <div className={"MealPanel-footer"}>
                    <div className={"MealPanel-footer-text-container"}>
                        <div className={"MealPanel-footer-text"}>Total Calories: {totalCalories(meals)}</div>
                        <div className={"MealPanel-footer-text"}>Total Protein: {totalProtein(meals)}g</div>
                    </div>
                    <button className={"Panel-footer-button"} type={"button"} onClick={goBack}>Go Back</button>
                </div>
            </div>
        </div>
    );
}

export default DatePanel;
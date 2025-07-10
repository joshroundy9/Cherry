// webapp/src/Dashboard.js
import React, { useState } from 'react';
import '../style/Dashboard.css';
import MealPanel from "./MealPanel";

function Dashboard({ onDashboard }) {


    return (
        <div className={"Dashboard-background"}>
            <MealPanel mealId={"1"} dateId={"1"}/>
        </div>
    );
}

export default Dashboard;

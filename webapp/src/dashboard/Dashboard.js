// webapp/src/Dashboard.js
import React, { useState } from 'react';
import '../style/Dashboard.css';
import MealPanel from "./MealPanel";

function Dashboard({ onDashboard }) {


    return (
        <div className={"Dashboard-background"}>
            <MealPanel mealId={"1"} dateId={"1"} date={"07/10/2025"} time={"09:00"}/>
        </div>
    );
}

export default Dashboard;

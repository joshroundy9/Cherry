// webapp/src/Dashboard.js
import React, { useState } from 'react';
import '../style/Dashboard.css';
import MealPanel from "./MealPanel";
import {ErrorState} from "../utils/DashboardUtil";

function Dashboard({ onDashboard }) {


    return (
        <div className={"Dashboard-background"}>
            <div className={"Calendar-container"}>

            </div>
            <div className={"Flex-container"}>
                <MealPanel mealId={"1"} dateId={"1"} date={"07/10/2025"} time={"09:00"}/>
            </div>
        </div>
    );
}

export default Dashboard;

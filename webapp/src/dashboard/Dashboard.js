// webapp/src/Dashboard.js
import React, { useState } from 'react';
import {Link} from "react-router-dom";
import '../style/Dashboard.css';
import NutritionForm from "./FoodEntry";

function Dashboard({ onDashboard }) {


    return (
        <div className={"Dashboard-background"}>
            <NutritionForm />
        </div>
    );
}

export default Dashboard;

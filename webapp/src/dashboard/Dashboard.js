import React, { useState, useEffect } from 'react';
import '../style/Dashboard.css';
import MealPanel from "./MealPanel";
import DatePanel from "./DatePanel";
import Calendar from "./Calendar";
import {DashboardHome} from "./DashboardHome";
import {GraphWrapper} from "../Graphs/GraphWrapper";

function Dashboard({ onDashboard }) {
    const [selectedPanel, setSelectedPanel] = useState('');

    const [dateId, setDateId] = useState(localStorage.getItem('dateId') || '');
    const [date, setDate] = useState(localStorage.getItem('date') || '');
    const [weight, setWeight] = useState(localStorage.getItem('weight') || '');

    useEffect(() => {
        const savedPanel = localStorage.getItem('dashboardPanel');
        if (savedPanel) setSelectedPanel(savedPanel);
    }, []);

    const switchPanel = (panel) => {
        setSelectedPanel(panel);
        localStorage.setItem('dashboardPanel', panel);
    };

    let panelComponent;
    switch (selectedPanel) {
        case 'meal':
            panelComponent = <MealPanel switchPanel={switchPanel} mealName={localStorage.getItem('mealName')} mealId={localStorage.getItem('mealId')} dateId={localStorage.getItem('dateId')} date={localStorage.getItem('date')} time={localStorage.getItem('time')}/>;
            break;
        case 'date':
            panelComponent = <DatePanel switchPanel={switchPanel} date={date} dateId={dateId} weight={weight} setWeight={setWeight} />;
            break;
        case 'weekly-graph':
            panelComponent = <GraphWrapper graphTerm={'Weekly'}/>;
            break;
        case 'monthly-graph':
            panelComponent = <GraphWrapper graphTerm={'Monthly'}/>;
            break;
        case 'yearly-graph':
            panelComponent = <GraphWrapper graphTerm={'Yearly'}/>;
            break;
        default:
            panelComponent = <DashboardHome />;
            break;
    }

    return (
        <div className={"Dashboard-background"}>
            <div className={"Calendar-container"}>
                < Calendar switchPanel={switchPanel} setWeight={setWeight} setDate={setDate} setDateId={setDateId} />
            </div>
            <div className={"Flex-container"}>
                {panelComponent}
            </div>
        </div>
    );
}

export default Dashboard;
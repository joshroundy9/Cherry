import React, { useState, useEffect } from 'react';
import '../style/Dashboard.css';
import MealPanel from "./MealPanel";
import DatePanel from "./DatePanel";

function Dashboard({ onDashboard }) {
    const [selectedPanel, setSelectedPanel] = useState('generic');

    useEffect(() => {
        const savedPanel = localStorage.getItem('dashboardPanel');
        if (savedPanel) setSelectedPanel(savedPanel);
    }, []);

    const switchPanel = (panel) => {
        setSelectedPanel(panel);
        localStorage.setItem('dashboardPanel', panel);
    };

    let panelComponent;
    if (selectedPanel === 'meal') {
        panelComponent = <MealPanel switchPanel={switchPanel} mealName={localStorage.getItem('mealName')} mealId={localStorage.getItem('mealId')} dateId={localStorage.getItem('dateId')} date={localStorage.getItem('date')} time={localStorage.getItem('time')}/>;
    } else if (selectedPanel === 'date') {
        // TODO: UNCOMMENT THIS WHEN DatePanel IS READY
        // panelComponent = <DatePanel date={localStorage.getItem('date')} dateId={localStorage.getItem('dateId')} />;
        panelComponent = <DatePanel switchPanel={switchPanel} date={'2024-08-20'} dateId={'4002'} />;
    } else {
        panelComponent = <div>generic panel</div>;
    }

    return (
        <div className={"Dashboard-background"}>
            <div className={"Calendar-container"}>
                {/* Example buttons to switch panels */}
                <button onClick={() => switchPanel('generic')}>Generic</button>
                <button onClick={() => switchPanel('date')}>Date</button>
                <button onClick={() => switchPanel('meal')}>Meal</button>
            </div>
            <div className={"Flex-container"}>
                {panelComponent}
            </div>
        </div>
    );
}

export default Dashboard;
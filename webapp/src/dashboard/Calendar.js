import React, { useState } from 'react';
import {genericRequest, getDataHeaders} from "../utils/DashboardUtil";
import {Signature} from "./DashboardComponents";

const daysOfWeek = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];

function pad(num) {
    return num < 10 ? `0${num}` : num;
}

function formatDate(year, month, day) {
    return `${year}-${pad(month + 1)}-${pad(day)}`;
}

export function Calendar({switchPanel, setDate, setDateId, setWeight}) {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const API_URL = process.env.REACT_APP_API_URL;

    const today = new Date();
    const [currentMonth, setCurrentMonth] = useState(today.getMonth());
    const [currentYear, setCurrentYear] = useState(today.getFullYear());

    const firstDayOfMonth = new Date(currentYear, currentMonth, 1);
    const lastDayOfMonth = new Date(currentYear, currentMonth + 1, 0);
    const daysInMonth = lastDayOfMonth.getDate();
    const startDay = firstDayOfMonth.getDay();

    const validDate = (date) => {
        const d1 = (date instanceof Date) ? date : new Date(date);
        return d1 <= today;
    }

    const handlePrevMonth = () => {
        if (currentMonth === 0) {
            setCurrentMonth(11);
            setCurrentYear(currentYear - 1);
        } else {
            setCurrentMonth(currentMonth - 1);
        }
    };

    const handleNextMonth = () => {
        if (currentMonth === 11) {
            setCurrentMonth(0);
            setCurrentYear(currentYear + 1);
        } else {
            setCurrentMonth(currentMonth + 1);
        }
    };

    const handleDayClick = (day) => {
        if (loading) return; // Prevent multiple clicks while loading
        const selectedDate = formatDate(currentYear, currentMonth, day);
        setDate(selectedDate);
        getDateInfo(selectedDate).then(() => switchPanel('date'));
    };

    const getDateInfo = async (date) => {
        setLoading(true);

        try {
            const responseBody = await genericRequest(
                `${API_URL}/data/date/from-user-and-date?date=${date}`,
                'GET',
                getDataHeaders(),
                null);
            if (!responseBody.dailyWeight) {
                localStorage.setItem('weight', localStorage.getItem('weight'));
                setWeight(localStorage.getItem('weight'));
            } else {
                localStorage.setItem('weight', responseBody.dailyWeight);
                setWeight(responseBody.dailyWeight);
            }
            localStorage.setItem('dateId', responseBody.dateID);
            localStorage.setItem('date', responseBody.date);
            localStorage.setItem('dailyCalories', responseBody.dailyCalories);
            localStorage.setItem('dailyProtein', responseBody.dailyProtein);
            setDateId(responseBody.dateID);
        } catch (err) {
            setError('Failed to fetch date information');
        } finally {
            setLoading(false);
        }
    }

    // Generate calendar grid
    const calendarDays = [];
    for (let i = 0; i < startDay; i++) {
        calendarDays.push(<td key={`empty-${i}`}></td>);
    }
    for (let day = 1; day <= daysInMonth; day++) {
        if (validDate(new Date(currentYear, currentMonth, day))) {
            calendarDays.push(
                <td key={day}>
                    <button className={'Calendar-button'} onClick={() => handleDayClick(day)}>{day}</button>
                </td>
            );
        } else {
            calendarDays.push(
                <td key={day}>
                    <button className={'Calendar-button-disabled'} disabled>{day}</button>
                </td>
            );
        }
    }

    // Split days into weeks
    const weeks = [];
    for (let i = 0; i < calendarDays.length; i += 7) {
        weeks.push(<tr key={i}>{calendarDays.slice(i, i + 7)}</tr>);
    }

    const monthLabel = new Date(currentYear, currentMonth).toLocaleString('default', { month: 'long', year: 'numeric' });

    return (
        <div className="Calendar">
            <div className={"Panel-header"}>Calendar</div>
            <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', marginBottom: 8 }}>
                <button className={"Calendar-arrow-button"} onClick={handlePrevMonth}>&lt;</button>
                <span className={"Calendar-month"}>{monthLabel}</span>
                <button className={"Calendar-arrow-button"} onClick={handleNextMonth}>&gt;</button>
            </div>
            <table>
                <thead>
                <tr>
                    {daysOfWeek.map(d => <th key={d}>{d}</th>)}
                </tr>
                </thead>
                <tbody>
                {weeks}
                </tbody>
            </table>
            <div className="Calendar-footer">
                {error && <div className={"Error-message"}>{error}</div>}
                {loading && <div className={"Info-message"}>Loading...</div>}
            </div>
            <div>
                <div style={{fontSize: 'x-large'}}>View Calorie, Protein, and Weight Graphs For:</div>
                <div>
                    <button className={"Panel-footer-button"} onClick={() => switchPanel('week-graph')}>This Week</button>
                    <button className={"Panel-footer-button"} onClick={() => switchPanel('month-graph')}>This Month</button>
                    <button className={"Panel-footer-button"} onClick={() => switchPanel('year-graph')}>This Year</button>
                </div>
            </div>
            <Signature />
        </div>
    );
}

export default Calendar;
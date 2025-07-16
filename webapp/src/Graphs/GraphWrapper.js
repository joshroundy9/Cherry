import Graph from "./Graph";
import '../style/Dashboard.css';
import {useEffect, useState} from "react";
import {getDataHeaders} from "../utils/DashboardUtil";
import {ErrorState, LoadingState} from "../dashboard/DashboardComponents";

const API_URL = process.env.REACT_APP_API_URL;

export function GraphWrapper({graphTerm}) {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [dataSets, setDataSets] = useState([]);
    const [labels, setLabels] = useState([]);
    const [dateRange, setDateRange] = useState({ start: null, end: null });

    let daysBack = null;
    switch (graphTerm) {
        case "Monthly":
            daysBack = 30;
            break;
        case "Yearly":
            daysBack = 364;
            break;
        default:
            daysBack = 7; // Default to weekly
            break;
    }


    useEffect(() => {
        function getDateRange () {
            const endDate = new Date();
            endDate.setDate(endDate.getDate()); // Exclude today
            const startDate = new Date(endDate);
            startDate.setDate(endDate.getDate() - daysBack);
            return { start: startDate, end: endDate };
        }

        function getLabels () {
            const endDate = new Date();
            endDate.setDate(endDate.getDate()); // Exclude today
            const startDate = new Date(endDate);
            startDate.setDate(endDate.getDate() - daysBack);

            const dateList = [];
            const numIntervals = daysBack;
            const startTime = startDate.getTime();
            const endTime = endDate.getTime();
            const interval = (endTime - startTime) / (numIntervals - 1);

            for (let i = 0; i < numIntervals; i++) {
                const date = new Date(startTime + i * interval);
                const formatted = date.getFullYear() + '-' +
                    String(date.getMonth() + 1).padStart(2, '0') + '-' +
                    String(date.getDate()).padStart(2, '0');
                dateList.push(formatted);
            }
            return dateList;
        }
        function buildDataSets(startDate, endDate, payload) {
            const dateList = [];
            for (let d = new Date(startDate); d <= endDate; d.setDate(d.getDate() + 1)) {
                dateList.push(d.toISOString().split('T')[0]);
            }

            const findValue = (date, key) => {
                const entry = payload.find(e => e.date === date);
                if (!entry || !entry[key] || entry[key] === 0) return null;
                return entry[key];
            };

            return [
                {
                    label: "Calories",
                    data: dateList.map(date => findValue(date, "dailyCalories"))
                },
                {
                    label: "Protein",
                    data: dateList.map(date => findValue(date, "dailyProtein"))
                },
                {
                    label: "Weight",
                    data: dateList.map(date => findValue(date, "dailyWeight"))
                }
            ];
        }
        setLabels(getLabels());
        setDateRange(getDateRange());

        setLoading(true);

        fetch(`${API_URL}/graphs/data?daysback=${daysBack}`, {
            method: 'GET',
            headers: getDataHeaders(),
        })
            .then(res => {
                if (!res.ok) throw new Error("Failed to fetch graph data");
                return res.json();
            })
            .then(data => {
                setDataSets(buildDataSets(getDateRange().start, getDateRange().end, data));
            })
            .catch(err => {
                setError(err.message);
            })
            .finally(() => {
                setLoading(false);
            });
    }, [graphTerm, daysBack]); // or other dependencies

    if (loading) return < LoadingState />;
    if (error) return <ErrorState message={error} />;

    return (
        <div className={"Graph-wrapper"}>
            <div className={"Panel-header"}>{graphTerm} Nutrition and Weight Graph</div>
            <Graph
                dataSets={dataSets}
                labels={labels}
                dateRange={dateRange}
            />
            <div className={"Graph-footer"}>
                <div>TIP: More consistent tracking greatly improves these graphs</div>
            </div>
    </div>);
}
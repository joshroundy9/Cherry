import React from "react";
import { Line } from "react-chartjs-2";
import { Chart, LineElement, PointElement, LinearScale, TimeScale, Title, Tooltip, Legend, CategoryScale } from "chart.js";
import "chartjs-adapter-date-fns";

Chart.register(LineElement, PointElement, LinearScale, TimeScale, Title, Tooltip, Legend, CategoryScale);

const colors = ["#FF0606", "#36A2EB", "#FFCE56"];

export function Graph({ dataSets, dateRange, labels }) {
    // dataSets: [{ label: "Calories", data: [1200, ...], borderColor: "#FF6384" }, ...]
    // labels: [date1, date2, ...] (ISO strings or Date objects)
    // dateRange: { start: Date, end: Date }

    const data = {
        labels,
        datasets: dataSets.map((ds, i) => ({
            ...ds,
            fill: false,
            borderColor: ds.borderColor || colors[i % colors.length],
            backgroundColor: ds.borderColor || colors[i % colors.length],
            tension: 0.2,
            yAxisID: "y",
            spanGaps: true,
        })),
    };

    const options = {
        responsive: true,
        plugins: {
            legend: { display: true },
            title: { display: false },
        },
        scales: {
            x: {
                type: "time",
                time: {
                    unit: getTimeUnit(dateRange),
                },
                title: { display: true, text: "Date" },
            },
            y: {
                beginAtZero: false,
                title: { display: true, text: "Value" },
            },
        },
    };

    function getTimeUnit(range) {
        const diff = range.end - range.start;
        const day = 24 * 60 * 60 * 1000;
        if (diff <= 8 * day) return "day";
        if (diff <= 32 * day) return "week";
        if (diff <= 370 * day) return "month";
        return "year";
    }

    return <Line data={data} options={options} />;
}

export default Graph;
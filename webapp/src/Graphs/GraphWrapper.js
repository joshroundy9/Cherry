import Graph from "./Graph";
import '../style/Graph.css';

export function GraphWrapper({graphTerm}) {
    return (
        <div className={"Graph-wrapper"}>
            <div className={"Panel-header"}>{graphTerm} Nutrition and Weight Graph</div>
            <Graph
                dataSets={[
                    { label: "Calories", data: [2000, null, 1800, 2300, 1900, 2100, 2400] },
                    { label: "Protein", data: [120, null, 110, 150, 190, 170, 200] },
                    { label: "Weight", data: [186, null, 180, 182, 185, 187, 190] }
                ]}
                labels={["2024-06-01", "2024-06-02", "2024-06-03", "2024-06-04", "2024-06-05", "2024-06-06", "2024-06-07"]}
                dateRange={{ start: new Date("2024-06-01"), end: new Date("2024-06-07") }}
            />
            <div className={"Graph-footer"}>
                <div>TIP: More consistent tracking greatly improves these graphs</div>
            </div>
    </div>);
}
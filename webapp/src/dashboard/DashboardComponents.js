import {useEffect, useState} from "react";
import {genericRequest, getCurrentDate, getDataHeaders} from "../utils/DashboardUtil";

const API_URL = process.env.REACT_APP_API_URL;

export function LoadingState () {
    return (
    <div className="Loading-container">
        <div className="Spinner"></div>
    </div>
    );
}

export function ErrorState ({ message }) {
    return (
        <div className="Error-container">
            <div>{message}</div>
            <button className="Panel-footer-button" onClick={() => window.location.reload()}>Reload</button>
        </div>
    );
}

export function DailyWeightInput ({date, dateId, weight, setWeight, setError}) {
    const [loading, setLoading] = useState(false);

    const updateDailyWeight = async () => {
        setLoading(true);
        try {
            await genericRequest(
                `${API_URL}/data/date/update-weight?dateid=${dateId}&weight=${weight}`,
                'POST',
                getDataHeaders(),
                null);
            setWeight(weight);
            if (date === getCurrentDate()) {
                localStorage.setItem('weight', weight);
                await genericRequest(
                    `${API_URL}/data/user/update-weight?weight=${weight}`,
                    'POST',
                    getDataHeaders(),
                    null
                )
            }
        } catch (err) {
            setError('Failed to update daily weight');
        } finally {
            setLoading(false);
        }
    }

    if (loading) return <div className={"Info-message"}>Loading...</div>;
    return (
        <div className={"Daily-weight-container"}>
            <div style={{marginRight: '2px'}}>Daily Weight:</div>
            <input
                className={"Daily-weight-input"}
                type="number"
                value={weight}
                onChange={e => setWeight(e.target.value)}
                onBlur={updateDailyWeight}
                max={999}
                onKeyDown={e => {
                    if (e.key === "Enter") {
                        e.target.blur();
                    }
                }}
            />
            <div style={{marginLeft: '2px'}}>lbs</div>
        </div>
    );
}

export function Signature() {
    return <div className={"Dashboard-footer-text"} style={{marginTop: "auto", marginBottom: "20px"}}>
        <div>Built by <span className={"Red"}>Josh Roundy</span> in Summer 2025</div>
        <div style={{marginTop: '10px'}}>
            <a href="https://www.linkedin.com/in/joshroundy" target="_blank" rel="noopener noreferrer">
                <img src="/linkedinicon.png" alt="LinkedIn"
                     style={{width: 32, height: 32, marginRight: 48}}/>
            </a>
            <a href="https://github.com/joshroundy9" target="_blank" rel="noopener noreferrer">
                <img src="/githubicon.png" alt="GitHub"
                     style={{width: 32, height: 32, marginRight: 48}}/>
            </a>
            <a href="https://joshroundy.dev" target="_blank" rel="noopener noreferrer">
                <img src="/portfolioicon.png" alt="Portfolio"
                     style={{width: 32, height: 32}}/>
            </a>
        </div>
    </div>
}
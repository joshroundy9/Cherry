export function DashboardHome() {
    return (
        <div className="Dashboard-home">
            <div className={"Dashboard-home-header"}>Welcome to <span className={"Red"}>CHERRY
                    <img style={{ verticalAlign: 'middle', position: 'relative', top: '2px', paddingTop: '2px' }} className="Logo-img" src="/logo56.png" alt="Logo"/></span>
                {localStorage.getItem("username")}!
            </div>
            <div style={{alignItems: 'center', marginTop: '45px'}} className={"Dashboard-home-text"}>
                <div style={{marginTop: '10px', textAlign: 'start', width: '100%'}}>Seamlessly track your meals, weight, and nutrition data with <span className={"Red"}>CHERRY</span>.</div>
                <div style={{marginTop: '50px', textAlign: 'start'}}><span className={"Red"}>AI-powered</span> meal-entry effortlessly delivers
                    accurate nutritional insights for your meals, eliminating the
                    hassle of complicated search menus and makes your tracking experience faster, easier, and more enjoyable.</div>
            </div>
            <div className={"Dashboard-home-footer"}>
                <div className={"Dashboard-home-text"} style={{marginBottom: '80px', marginLeft: '5%'}}>
                    <div className={"Dashboard-home-arrow-bg"}/>
                    <div>Select a date on the <span className={"Red"} style={{textDecoration: 'underline'}}>Calendar</span> to start tracking.</div>
                </div>
            </div>
        </div>
    );
}
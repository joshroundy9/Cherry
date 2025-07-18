import {useNavigate} from "react-router-dom";
import '../style/Dashboard.css';
import {Signature} from "../dashboard/DashboardComponents";

const HomePage = ({ onHomePage }) => {
    const navigate = useNavigate();
    const navigateToRegister = () => {
        navigate('/register');
    }
    const navigateToLogin = () => {
        navigate('/login');
    }
    return (
        <div className="HomePage-background">
            <div className="HomePage-body-container">
                <div className="HomePage-content">
                    <div className={"Homepage-content-text"}>
                        <h1 className="HomePage-title">Your personal <span className={"Auth-text-header"}>AI-powered</span> nutrition tracker</h1>
                        <p className="HomePage-text">Track your meals,
                            monitor your
                            weight, and view your progress with ease.</p>
                    </div>
                    <div className={"HomePage-content-image-left"}>
                        <img className={"Hover-expand"} src="/cherrydemo.gif" alt="demo"
                             style={{maxWidth: '100%', width: '573px', height: 'auto'}}/>
                    </div>
                </div>
                <div className="HomePage-content HomePage-content-reverse">
                    <div className={"HomePage-content-image-right"}>
                        <img className={"Hover-expand"} src="/graphimage.png" alt="graph"
                             style={{maxWidth: '100%', width: '573px', height: 'auto'}}/>
                    </div>
                    <div className={"Homepage-content-text"}>
                        <h1 className="HomePage-title"><span className={"Auth-text-header"}>Visualize</span> your progress</h1>
                        <p className="HomePage-text">View your
                            Calorie, Protein, and Weight history over the past week, month, or year.</p>
                    </div>
                </div>
                <div className="HomePage-content">
                    <div className={"Homepage-content-text"}>
                        <h1 className="HomePage-title">Still not convinced?</h1>
                        <p className="HomePage-text">Watch our demo video to see how <span
                            className="Logo-red">CHERRY</span> can
                            transform your health journey!</p>
                    </div>
                    <div className={"HomePage-content-image-left"}>
                    <iframe style={{marginLeft: '5vw'}} width="560" height="315" src="https://www.youtube.com/embed/-GoDxL0FQ5U?si=SwsP4Herq345mLOF"
                            title="YouTube video player" frameBorder="0"
                            allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"
                            referrerPolicy="strict-origin-when-cross-origin" allowFullScreen></iframe>
                    </div>
                </div>
                <div className="HomePage-content" style={{textAlign: 'center'}}>
                    <p>New here? <button className="Form-button Hover-expand" style={{fontSize: 'xx-large'}} onClick={
                        navigateToRegister
                    }>Create Account</button></p>
                    <Signature/>
                </div>
            </div>
        </div>
    );
}

export default HomePage;
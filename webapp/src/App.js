// webapp/src/App.js
import './style/App.css';
import {Route, Routes, Link, useNavigate, useLocation} from 'react-router-dom';
import Login from './security/Login';
import Register from "./security/Register";
import Dashboard from "./dashboard/Dashboard";
import PrivateRoute from "./security/PrivateRoute";
import HomePage from "./HomePage/HomePage";
import AuthRoute from "./security/AuthRoute";
import {EmailVerify} from "./security/EmailVerify";
import {ForgotPassword} from "./security/ForgotPassword";
import ResetPassword from "./security/ResetPassword";
import PrivacyPolicy from "./security/PrivacyPolicy";

function App() {
    const navigate = useNavigate()
    const location = useLocation()
    const onSignOut = () => {
        localStorage.removeItem('jwtToken');
        navigate('/');
    };
  return (
    <div className="App">
        <header className="Main-header">
            {location.pathname === "/dashboard" && (
                <p className="Username-text" style={{color: '#FF0606'}}>
                    {localStorage.getItem('username') || 'Guest'}
                </p>
            )}
            <Link className="Logo-text" to="/">
                CHERRY
                <img className="Logo-img" src="/logo56.png" alt="Logo"/>
            </Link>
            {location.pathname === "/" && (<Link className="SignOut-button" to="/login">Sign In</Link>)}
            {location.pathname === "/dashboard" && (
                <button className="SignOut-button" onClick={onSignOut}>
                    Sign Out
                </button>
            )}
        </header>
        <Routes>
            {/* HOME ROUTE */}
            <Route path="/" element={<HomePage onHomePage={() => {}}/>}/>
            {/* AUTH ROUTES */}
            <Route path="/login" element={
                <AuthRoute>
                <Login onLogin={() => {}}/>
                </AuthRoute>
            }/>
            <Route path="/register" element={
                <AuthRoute>
                    <Register onRegister={() => {}}/>
                </AuthRoute>
            }/>
            <Route path="/verify" element={<EmailVerify onEmailVerify={() => {}}/>}/>
            <Route path="/forgot-password" element={<ForgotPassword onForgotPassword={() => {}}/>}/>
            <Route path="/reset-password" element={<ResetPassword onResetPassword={() => {}}/>}/>
            <Route path="/privacy" element={<PrivacyPolicy onPrivacyPolicy={() => {}}/>}/>

            {/* PROTECTED ROUTES */}
            <Route path="/dashboard" element={
                <PrivateRoute>
                <Dashboard onDashboard={() => {}}/>
                </PrivateRoute>
                }/>
        </Routes>
    </div>
  );
}

export default App;
// webapp/src/App.js
import './style/App.css';
import {Route, Routes, Link, useNavigate, useLocation} from 'react-router-dom';
import Login from './security/Login';
import Register from "./security/Register";
import Dashboard from "./dashboard/Dashboard";
import PrivateRoute from "./security/PrivateRoute";

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
            {location.pathname === "/dashboard" && (
                <button className="SignOut-button" onClick={onSignOut}>
                    Sign Out
                </button>
            )}
        </header>
        <Routes>
            {/* PUBLIC ROUTES */}
            <Route path="/" element={<Login onLogin={() => {}}/>}/>
            <Route path="/register" element={<Register onRegister={() => {}}/>}/>

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
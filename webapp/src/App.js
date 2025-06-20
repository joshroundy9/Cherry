// webapp/src/App.js
import logo from './logo.svg';
import './App.css';
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom';
import Login from './Login';
import Register from "./Register";

function App() {
  return (
      <Router>
        <div className="App">
            <header className="Main-header">
                <Link className="Logo-text" to="/">
                    CHERRY
                    <img className="Logo-img" src="/logo56.png" alt="Logo"/>
                </Link>
            </header>
            <Routes>
              <Route path="/" element={
                  <div className="App-header">
                      <img src={logo} className="App-logo" alt="logo"/>
                      <p>
                        Edit <code>src/App.js</code> and save to reload.
                      </p>
                      <Link className="App-link" to="/login">
                        Login
                      </Link>
                      <Link className="App-link" to="/register">
                          Register
                      </Link>
                  </div>
              }/>
              <Route path="/login" element={<Login onLogin={() => {}}/>}/>
                <Route path="/register" element={<Register onRegister={() => {}}/>}/>
            </Routes>
        </div>
      </Router>
  );
}

export default App;
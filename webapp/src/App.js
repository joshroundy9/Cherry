// webapp/src/App.js
import logo from './logo.svg';
import './App.css';
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom';
import Login from './Login';

function App() {
  return (
      <Router>
        <div className="App">
            <header className="Main-header">
                <Link className="Logo-text" to="/">
                    <h1>CHERRY</h1>
                    <img src="/logo56.png" alt="Logo"/>
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
                        Go to Login
                      </Link>
                  </div>
              }/>
              <Route path="/login" element={<Login onLogin={() => {
              }}/>}/>
            </Routes>
        </div>
      </Router>
  );
}

export default App;
import React from 'react'
import { BrowserRouter as Router, Navigate, Route, Routes } from 'react-router-dom'
import HeaderComponent from '../static/HeaderComponent.jsx'
import FooterComponent from '../static/FooterComponent.jsx'
import LoginComponent from '../authentication/LoginComponent.jsx'
import LogoutComponent from '../authentication/LogoutComponent.jsx'
import ListTodosComponent from './ListTodosComponent.jsx'
import WelcomeComponent from './WelcomeComponent.jsx'
import TicTacToeComponent from './tictactoe/TicTacToeComponent.jsx'
import RegisterComponent from '../authentication/RegisterComponent.jsx'
import { AuthProvider } from '../authentication/AuthProvider.tsx'
import 'bootstrap/dist/css/bootstrap.css';
import '../css/AppMain.css';
 
export default function AppMain(...props) 
{
  return (
    <div className="App">
      <AuthProvider>
        <Router>
          <HeaderComponent/>
          <Routes>
            <Route exact path="/" element={<Navigate to="/welcome" />} />
            <Route exact path="/welcome" element={<WelcomeComponent />} />
            <Route exact path="/tictactoe" element={<TicTacToeComponent />} />
            <Route exact path="/register" element={<RegisterComponent />} />
            <Route exact path="/login" element={<LoginComponent />} />
            <Route exact path="/logout" element={<LogoutComponent />} />
            <Route exact path="/todos" 
              element={
                <ListTodosComponent /> 
              }
            />
          </Routes>
          <FooterComponent/>
        </Router>
      </AuthProvider>
    </div>
  );
}


import React, {Component} from 'react'
import { BrowserRouter as Router, Navigate, Redirect, Route, Routes } from 'react-router-dom'
import HeaderComponent from '../static/HeaderComponent.jsx'
import FooterComponent from '../static/FooterComponent.jsx'
import LoginComponent from '../authentication/LoginComponent.jsx'
import LogoutComponent from '../authentication/LogoutComponent.jsx'
import ListTodosComponent from './ListTodosComponent.jsx'
import { AuthRoute } from '../authentication/AuthRoute.jsx'
import WelcomeComponent from './WelcomeComponent.jsx'
 
export default function AppMain(...props) 
{
  return (
    <>
    <Router>
      <HeaderComponent/>
      <Routes>
				<Route exact path="/" element={<Navigate to="/welcome" />} />
        <Route exact path="/welcome" element={<WelcomeComponent />} />
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
    </>
  );
}


import React, { Component } from 'react';
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom'

import withNavigation from './decorators/WithNavigation.jsx'
import withParams from './decorators/WithParams.jsx'
import AuthenticatedRoute from './decorators/AuthenticatedRoute.jsx'

import HeaderComponent from './interface/HeaderComponent.jsx'
import FooterComponent from './interface/FooterComponent.jsx'
import WelcomeComponent from './pages/WelcomeComponent.jsx'
import LoginComponent from './authentication/LoginComponent.jsx'
import LogoutComponent from './authentication/LogoutComponent.jsx'
import ListTodosComponent from './pages/ListTodosComponent.jsx'
import ErrorComponent from './pages/ErrorComponent.jsx'

export class FlashCardApp extends Component 
{
  render() 
  {
    const LoginComponentWithNavigation = withNavigation(LoginComponent);
    const HeaderComponentWithNavigation = withNavigation(HeaderComponent);
    const WelcomeComponentWithParams = withParams(WelcomeComponent);
    return (
      <div className="flashCardApp">
        <Router>
          <HeaderComponentWithNavigation/>
          <Routes>
            <Route path="/" element={<LoginComponentWithNavigation />} />
            <Route path="/login" element={<LoginComponentWithNavigation />} />
            <Route path="/logout" element={<LogoutComponent />} />
            <Route path="/welcome/:username" element={
              <AuthenticatedRoute> 
                <WelcomeComponentWithParams />
              </AuthenticatedRoute> 
              } />
            <Route path="/welcome/:username" element={
              <AuthenticatedRoute> 
                <WelcomeComponentWithParams />
              </AuthenticatedRoute> 
              } />
            <Route path="/todo" element={
              <AuthenticatedRoute> 
                <ListTodosComponent />
              </AuthenticatedRoute> 
              } />
            <Route path="*" element={<ErrorComponent />} />
          </Routes>
          <FooterComponent/>
        </Router>
      </div>
    );
  }
}

export default FlashCardApp;

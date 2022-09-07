import React, { Component } from 'react';
import {BrowserRouter as Routes, Link} from 'react-router-dom'
import AuthenticationService from '../decorators/AuthenticationService.jsx'

class HeaderComponent extends Component
{
  //<h1>Flash Card Application</h1>
  render() {
    const isUserLoggedIn = AuthenticationService.isUserLoggedIn();
    //console.log(isUserLoggedIn && "logged in")
    return (
      <div>
        <header>
          <nav className="navbar navbar-expand-md navbar-dark bg-dark">
            <div><a href="127.0.0.1" className="navbar-brand">Brand</a></div>
            <ul className="navbar-nav">
              {isUserLoggedIn && <li><Link className="nav-link" to="/welcome/username">Home</Link></li>}
              {isUserLoggedIn && <li><Link className="nav-link" to="/todo">Todo</Link></li>}
            </ul>
            <ul className="navbar-nav navbar-collapse justify-content-end">
              {!isUserLoggedIn && <li><Link className="nav-link" to="/login">Login</Link></li>}
              {isUserLoggedIn && <li><Link className="nav-link" onClick={AuthenticationService.logout} to="/logout">Logout</Link></li>}
            </ul>
          </nav>
        </header>
      </div>
    )
  }
}

export default HeaderComponent;

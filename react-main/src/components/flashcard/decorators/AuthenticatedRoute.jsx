import React, {Component} from 'react'
import AuthenticationService from './AuthenticationService.jsx'
//import {BrowserRouter as Router, Route, Routes, Link} from 'react-router-dom'
import { Navigate } from "react-router-dom";

class AuthenticatedRoute extends Component {
  render() {
    if (AuthenticationService.isUserLoggedIn()){
      return {...this.props.children}
    }
    else
    {
      return <Navigate to="/login" />
    }
  }
}

export default AuthenticatedRoute

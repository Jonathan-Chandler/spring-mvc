import React, {Component} from 'react'
import AuthenticationService from './AuthenticationService.jsx'
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

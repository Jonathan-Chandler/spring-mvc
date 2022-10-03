import React, {Component} from 'react'
//import { AuthenticationService } from "./AuthenticationService.jsx";
import AuthenticationService from '../authentication/AuthenticationService.jsx'

function LogoutComponent(props) {
    //const isUserLoggedIn = AuthenticationService.isUserLoggedIn();
    AuthenticationService.logout();

    return (
        <>
            <h1>You are logged out</h1>
            <div className="container">
                Thank You for Using Our Application.
            </div>
        </>
    )
}

export default LogoutComponent

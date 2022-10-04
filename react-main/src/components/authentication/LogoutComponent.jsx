import React from 'react'
import AuthenticationService from '../authentication/AuthenticationService.jsx'

function LogoutComponent(props) {
    AuthenticationService.logout();

    return (
        <>
            <h1>You are logged out</h1>
            <div className="container">
                (thanks for logging in)
            </div>
        </>
    )
}

export default LogoutComponent

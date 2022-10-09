import React, {useState, useEffect} from 'react'
import UseAuth, {useAuthDispatch} from './AuthContext.jsx'
import {AUTH_ACTIONS} from './AuthReducer.jsx'

function LogoutComponent(...props) {
    //AuthenticationService.logout();
    const auth = UseAuth();
    const authDispatch = useAuthDispatch();

    useEffect(() => {
        if (auth.username || auth.token)
        {
            // remove failed login warning
            authDispatch({
                type: AUTH_ACTIONS.LOGOUT
            })
        }

        // update login successful state and redirect if already logged in
        //setLoginSuccess(AuthenticationService.isUserLoggedIn())
    }, [auth.username, auth.token]);

    return (
        <>
            <h1>You are logged out</h1>
            <div className="container">
                (you are not logged in)
            </div>
        </>
    )
}

export default LogoutComponent

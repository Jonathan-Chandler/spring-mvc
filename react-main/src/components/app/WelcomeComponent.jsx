import React, {useState} from 'react'

import {Link } from 'react-router-dom'
//import useAuth from '../authentication/AuthContext.jsx'
import useAuth from "../authentication/AuthProvider.tsx";

export default function WelcomeComponent(...props)
{
    const { username, token, apiSession, loading, error, login, signUp, logout } = useAuth();
    //console.log("WelcomeComponent.auth: " + username);

    return (
        <>
            <h1>Welcome!</h1>
            <div className="container">
                Welcome you can manage your todos <Link to="/todos">here</Link>.
            </div>
        </>
    )        
}


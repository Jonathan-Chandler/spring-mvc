import React, {Component} from 'react'

import {Link } from 'react-router-dom'
//import UseAuth from '../authentication/AuthContext.jsx'
import useAuth from "../authentication/AuthProvider.tsx";

export default function IndexComponent(...props)
{
    const { username, token, apiSession, loading, error, login, signUp, logout } = useAuth();

    return (
        <>
            <h1>Welcome!</h1>
            <div className="container">
                Welcome index {username}
            </div>
        </>
    )        
}



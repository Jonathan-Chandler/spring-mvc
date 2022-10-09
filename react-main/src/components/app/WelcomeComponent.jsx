import React, {useEffect, useState} from 'react'
import { NavLink, Link } from 'react-router-dom'
import { useNavigate } from "react-router-dom";
import useAuth from "../authentication/AuthProvider.tsx";

export default function WelcomeComponent(...props)
{
    const { username, token, apiSession, loading, error, login, signUp, logout } = useAuth();
    const navigate = useNavigate();

    if (username!==null && username !== "")
    {
        return (
            <>
                <h1>Welcome!</h1>
                <div className="container">
                    Welcome {username}.
                    <div className="container">
                        <h2><p />Test: </h2>
                            {<NavLink className="nav-link" to="/welcome">Home</NavLink>}
                            {<NavLink className="nav-link" to="/todos">Todos</NavLink>}
                            {<NavLink className="nav-link" to="/logout">Logout</NavLink>}
                            {<NavLink className="nav-link" to="/login">Login</NavLink>}
                    </div>
                </div>
            </>
        )        
    }
    else
    {
        return (
            <>
                <h1>Welcome!</h1>
                <div className="container">
                    Welcome
                </div>
            </>
        )        
    }
}


import React, {useState, useEffect} from 'react'
import useAuth from "./AuthProvider.tsx";

export default function LogoutComponent(...props) {
    const { username, token, apiSession, loading, error, login, signUp, logout } = useAuth();

    useEffect(() => {
        logout();
    }, []);

    return (
        <>
            <h1>You are logged out</h1>
            <div className="container">
                (you are not logged in)
            </div>
        </>
    )
}


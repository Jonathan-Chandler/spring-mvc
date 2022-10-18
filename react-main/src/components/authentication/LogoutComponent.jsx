import React, {useEffect} from 'react'
import useAuth from "./AuthProvider.tsx";

export default function LogoutComponent(...props) {
    const { logout } = useAuth();

    useEffect(() => {
        logout();
    }, [logout]);

    return (
        <>
            <h1>You are logged out</h1>
            <div className="container">
                (you are not logged in)
            </div>
        </>
    )
}


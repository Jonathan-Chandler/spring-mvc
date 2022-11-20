import React, {useEffect} from 'react'
import useAuth from "./AuthProvider.tsx";

export default function LogoutComponent(...props) {
    const { logout, loading } = useAuth();

    useEffect(() => {
        logout();
    });

	if (!loading)
	{
		return (
			<>
				<h1>You are logged out</h1>
				<div className="container">
					(you are not logged in)
				</div>
			</>
		)
	}
}


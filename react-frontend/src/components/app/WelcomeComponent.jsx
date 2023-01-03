import React, {useEffect, useState} from 'react'
import { NavLink, Link } from 'react-router-dom'
//import { useNavigate } from "react-router-dom";
import useAuth from "../authentication/AuthProvider.tsx";

export default function WelcomeComponent(...props)
{
    const { isAuthenticated, username, loading } = useAuth();
    //const navigate = useNavigate();

    if (!loading) 
	{
		if (isAuthenticated())
		{
			return (
				<>
					<h1>Welcome!</h1>
					<div className="container">
						Welcome {username}.
						<div className="container">
							<h2><p />Test: </h2>
								{<NavLink className="nav-link" to="/welcome">Home</NavLink>}
								{<NavLink className="nav-link" to="/logout">Logout</NavLink>}
								{<NavLink className="nav-link" to="/login">Login</NavLink>}
								{<NavLink className="nav-link" to="/tictactoe/playerlist">tictactoeplayers</NavLink>}
							
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
						<div className="container">
							<h2><p />Test: </h2>
								{<NavLink className="nav-link" to="/welcome">Home</NavLink>}
								{<NavLink className="nav-link" to="/logout">Logout</NavLink>}
								{<NavLink className="nav-link" to="/login">Login</NavLink>}
						</div>
					</div>
				</>
			)        
		}
	}
	else
	{
		return (
			<>
				<h1>Welcome!</h1>
				<div className="container">
					(loading && "Loading...") 
				</div>
			</>
		)        
	}
}


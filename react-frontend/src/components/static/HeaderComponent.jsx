import React from 'react'
import { NavLink, Link } from 'react-router-dom'
import useAuth from "../authentication/AuthProvider.tsx";

export default function HeaderComponent(...params) {
    const { isAuthenticated } = useAuth();

    if (isAuthenticated())
	{
		return (
			<header>
				<nav className="navbar navbar-expand-md navbar-dark bg-dark">
					<div className="navbar-collapse">
						<ul className="navbar-nav mr-auto">
							<li className="nav-item active">
								<NavLink className="nav-link navbar-brand" href="/welcome">TIC TAC TOE DEMO</NavLink>
							</li>
							<li className="nav-item">
								<NavLink className="nav-link" to="/welcome">Home</NavLink>
							</li>
							<li className="nav-item">
								<NavLink className="nav-link" to="/tictactoe">Tic Tac Toe</NavLink>
							</li>
						</ul>
					</div>
					<div className="collapse navbar-collapse justify-content-end">
						<ul className="navbar-nav ml-auto">
							<li className="nav-item">
								<NavLink className="nav-link" to="/logout">Logout</NavLink>
							</li>
						</ul>
					</div>
				</nav>
			</header>
		)
	}
	else
	{
		return (
			<header>
				<nav className="navbar navbar-expand-md navbar-dark bg-dark">
					<div className="navbar-collapse">
						<ul className="navbar-nav mr-auto">
							<li className="nav-item active">
								<NavLink className="nav-link navbar-brand" href="/welcome">TIC TAC TOE DEMO</NavLink>
							</li>
							<li className="nav-item">
								<NavLink className="nav-link" to="/welcome">Home</NavLink>
							</li>
							<li className="nav-item">
								<NavLink className="nav-link" to="/tictactoe">Tic Tac Toe</NavLink>
							</li>
						</ul>
					</div>
					<div className="collapse navbar-collapse justify-content-end">
						<ul className="navbar-nav ml-auto">
							<li className="nav-item">
								<NavLink className="nav-link" to="/register">Register</NavLink>
							</li>
							<li className="nav-item">
								<NavLink className="nav-link" to="/login">Login</NavLink>
							</li>
						</ul>
					</div>
				</nav>
			</header>
		)
	}
}

//							<li className="nav-item">
//								<NavLink className="nav-link" to="/tictactoe/playerlist">Player List</NavLink>
//							</li>

//							<li className="nav-item">
//								<NavLink className="nav-link" to="/tictactoe/playerlist">Player List</NavLink>
//							</li>
//							<li className="nav-item">
//								<NavLink className="nav-link" to="/tictactoe/game/online">Online Game</NavLink>
//							</li>

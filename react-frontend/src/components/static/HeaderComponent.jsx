import React from 'react'
import { NavLink, Link } from 'react-router-dom'
import useAuth from "../authentication/AuthProvider.tsx";

export default function HeaderComponent(...params) {
    const { isAuthenticated } = useAuth();

//					<div className="collapse navbar-collapse">
    if (isAuthenticated())
	{
		return (
			<header>
				<nav className="navbar navbar-expand-md navbar-dark bg-dark">
					<div className="navbar-collapse">
						<ul className="navbar-nav mr-auto">
							<li className="nav-item active">
								<NavLink className="nav-link" href="/welcome">Something</NavLink>
							</li>
							<li className="nav-item">
								<NavLink className="nav-link" to="/welcome">Home</NavLink>
							</li>
							<li className="nav-item">
								<NavLink className="nav-link" to="/tictactoe">Tic-Tac-Toe</NavLink>
							</li>
							<li className="nav-item">
								<NavLink className="nav-link" to="/todos">Todos</NavLink>
							</li>
							<li className="nav-item">
								<NavLink className="nav-link" to="/tictactoe/amqp">AMQP</NavLink>
							</li>
							<li className="nav-item">
								<NavLink className="nav-link" to="/tictactoe/game/online">Online Game</NavLink>
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
								<NavLink className="nav-link" href="/welcome">Something</NavLink>
							</li>
							<li className="nav-item">
								<NavLink className="nav-link" to="/welcome">Home</NavLink>
							</li>
							<li className="nav-item">
								<NavLink className="nav-link" to="/tictactoe">Tic-Tac-Toe</NavLink>
							</li>
							<li className="nav-item">
								<NavLink className="nav-link" to="/tictactoe/amqp">AMQP test</NavLink>
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


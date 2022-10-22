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
                    <div><Link to="/" className="navbar-brand">Something</Link></div>
                    <ul className="navbar-nav">
                        {<li><NavLink className="nav-link" to="/welcome">Home</NavLink></li>}
                        {<li><NavLink className="nav-link" to="/todos">Todos</NavLink></li>}
                        {<li><NavLink className="nav-link" to="/tictactoe">TicTacToe</NavLink></li>}
                        {<li><NavLink className="nav-link" to="/logout">Logout</NavLink></li>}
                    </ul>
                </nav>
            </header>
        )        
    }
    else
    {
        return (
            <header>
                <nav className="navbar navbar-expand-md navbar-dark bg-dark">
                    <div><Link to="/" className="navbar-brand">Something</Link></div>
                    <ul className="navbar-nav">
                        {<li><NavLink className="nav-link" to="/welcome">Home</NavLink></li>}
                        {<li><NavLink className="nav-link" to="/register">Register</NavLink></li>}
                        {<li><NavLink className="nav-link" to="/login">Login</NavLink></li>}
                        {<li><NavLink className="nav-link" to="/tictactoe">TicTacToe</NavLink></li>}
                    </ul>
                </nav>
            </header>
        )        
    }
}


import React, { useState } from 'react';
import { NavLink, Link } from 'react-router-dom'
import useAuth from "../../authentication/AuthProvider.tsx";
import { useNavigate } from "react-router-dom";
import './TictactoeGameSelect.css';

export default function TictactoeGameSelect(...params) {
    const [loginRequired, setLoginRequired] = useState(false);
	const { isAuthenticated } = useAuth();
    const navigate = useNavigate();

    // attempt login on submit
	const handleOnlineGame = (e) => 
    {
		e.preventDefault();

		// only redirect if user has valid login
        if (isAuthenticated())
		{
			navigate("/tictactoe/playerlist");
		}
		else
		{
			setLoginRequired(true);
		}
    }

	const handleLocalGame = (e) => 
    {
		e.preventDefault();
		navigate("/tictactoe/game/local");
	}

	const handleAiGame = (e) => 
    {
		e.preventDefault();
		navigate("/tictactoe/game/ai");
	}

	if (isAuthenticated())
	{
		return (
				<div className="justify-content-center row">
					<div className="col-12 col-sm-6 content">
						<h1>Tic-Tac-Toe</h1>
						<p><button className="btn btn-primary btn-block col-lg-6" onClick={handleAiGame}>Play Against AI</button></p>
						<p><button className="btn btn-primary btn-block col-lg-6" onClick={handleLocalGame}>Play Against Local Player</button></p>
						<p><button className="btn btn-primary btn-block col-lg-6" onClick={handleOnlineGame}>Play Online Game</button></p>
					</div>
				</div>
		)
	}
	else
	{
		return (
				<div className="justify-content-center row">
					<div className="col-12 col-sm-6 content">
						<h1>Tic-Tac-Toe</h1>
						<p><button className="btn btn-primary btn-block col-lg-6" onClick={handleAiGame}>Play Against AI</button></p>
						<p><button className="btn btn-primary btn-block col-lg-6" onClick={handleLocalGame}>Play Against Local Player</button></p>
						<p><button className="btn btn-secondary btn-block col-lg-6" onClick={handleOnlineGame}>Play Online Game</button></p>
						{loginRequired && <div className="alert alert-warning">Please <a href="/login">login</a> to play online</div>}
						{!loginRequired && <div className="alert"> </div>}
					</div>
				</div>
		)        
	}
//	if (isAuthenticated())
//	{
//		return (
//			<div className="container-fluid">
//				<div className="justify-content-center row">
//					<div className="col-12 col-sm-6 content">
//						<h1>Tic-Tac-Toe</h1>
//						<p><button type="button" className="btn btn-primary" onclick={handleLocalGame}>Play Against Local Player</button></p>
//						<p><button type="button" className="btn btn-primary" onclick={handleAiGame}>Play Against AI</button></p>
//						<p><button type="button" className="btn btn-primary" onClick={handleOnlineGame}>Play Online Game</button></p>
//					</div>
//				</div>
//			</div>
//		)
//	}
//	else
//	{
//		return (
//			<div className="container-fluid">
//				<div className="justify-content-center row">
//					<div className="col-12 col-sm-6 content">
//						<h1>Tic-Tac-Toe</h1>
//						<div class="span6 content">
//						<p><button type="button" className="btn btn-primary btn-block" onClick={handleAiGame}>Play Against AI</button></p>
//						<p><button type="button" className="btn btn-primary btn-block" onClick={handleLocalGame}>Play Against Local Player</button></p>
//						<p><button type="button" className="btn btn-secondary btn-block" onClick={handleOnlineGame}>Play Online Game</button></p>
//						{loginRequired && <div className="alert alert-warning">Please <a href="/login">login</a> to play online</div>}
//						{!loginRequired && <div className="alert"> </div>}
//						</div>
//					</div>
//				</div>
//			</div>
//		)        
//	}
}


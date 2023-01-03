import moment from 'moment'
import React, { useEffect, useCallback, useState } from "react";
import { useNavigate } from "react-router-dom";
import useAuth from '../../authentication/AuthProvider.tsx';
import { PLAYER_LIST_API_URL } from '../../../Constants';

//import Stomp from 'stompjs'
//import useWebSocket, { ReadyState } from 'react-use-websocket';

import { Stomp, Client } from '@stomp/stompjs';
//import SockJS from 'sockjs-client';

export default function TictactoePlayerList()
{
	const { username, token, isAuthenticated, getStompSession, playerList, sendGameRequest, tictactoeGame } = useAuth();
    const navigate = useNavigate();

	// navigate to welcome if not logged in
	useEffect(() => 
	{
        if (!isAuthenticated())
        {
            navigate("/welcome");
        }
	}, [isAuthenticated, navigate]);

	useEffect(() =>
	{
		// navigate back to game in progress if it has not ended yet
		if (tictactoeGame.gameState === "X_PLAYER_MOVING" 
			|| tictactoeGame.gameState === "O_PLAYER_MOVING" 
			|| tictactoeGame.gameState === "STARTING")
		{
			//console.log("Joining Game (gameState = " + tictactoeGame.gameState + ")");
			navigate("/tictactoe/game/online")
		}
	},[tictactoeGame.gameState, navigate])

	const handleRequest = useCallback((name) => 
	{
		sendGameRequest(name);
	}, [sendGameRequest]);

	if (playerList && playerList.length >= 1)
	{
		return (
			<div>
				Player List
				<p />
				<div className="container">
					<table className="table">
						<tbody>
							<tr>
								<th>Name</th>
								<th>You Requested</th>
								<th>They Requested</th>
								<th>Send Request</th>
							</tr>
							{
								playerList.map((item) => (
									<tr key={item.username}>
										<td>{item.username}</td>
										<td>{item.myRequest ? "Yes" : "No"}</td>
										<td>{item.theirRequest ? "Yes" : "No"}</td>
										<td><button className="btn btn-success" onClick={() => handleRequest(item.username)}>Request</button></td>
									</tr>
								))
							}
						</tbody>
					</table>
				</div>
			</div>
		);
	}
	else
	{
		return (
			<div>
				Player List
				<p />
				<div className="container">
					<p />No other players are online.
				</div>
			</div>
		);
	}

//				<p /><button className="btn btn-success" onClick={() => sendRequest("test_user123")}>req test_user123</button>
//				<p /><button className="btn btn-success" onClick={() => sendRequest("test_user1234")}>req test_user1234</button>
//				<p /><button className="btn btn-success" onClick={() => sendRequestDto()}>send requestDto</button>
}

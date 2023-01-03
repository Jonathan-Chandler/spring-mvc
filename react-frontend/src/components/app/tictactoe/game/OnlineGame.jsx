import React, {useState, useEffect, useCallback} from 'react'
//import { BrowserRouter as Router, Navigate, Route, Routes } from 'react-router-dom'
import useAuth from '../../../authentication/AuthProvider.tsx';
import { useNavigate } from "react-router-dom";
import _ from "lodash";
import './Tictactoe.css';

export default function OnlineGame () {
	const { username, isAuthenticated, getStompSession, playerList, tictactoeGame, sendGameCheckIn, sendGameRefresh, sendGameMove, sendGameForfeit} = useAuth();

	const [movingPlayer, setMovingPlayer] = useState("");
	const [winningPlayer, setWinningPlayer] = useState("");
	const [boardSquares, setBoardSquares] = useState(Array(9).fill('_'));
	const [statusMessage, setStatusMessage] = useState("");
	const [gameOver, setGameOver] = useState(false);
	const [gameOverMessage, setGameOverMessage] = useState("");
	const [playerIsExiting, setPlayerIsExiting] = useState(false);

	const [gameBufferIsValid, setGameBufferIsValid] = useState(false)
	const [gameBuffer, setGameBuffer] = useState({
		xPlayerName: "",
		oPlayerName: "",
		xPlayerReady: false,
		oPlayerReady: false,
		gameState: "",
		gameBoard: "_________",
		gameOverMessage: ""
	});
    const navigate = useNavigate();

	// navigate to welcome if not logged in
	useEffect(() => 
	{
        if (!isAuthenticated())
        {
            navigate("/welcome");
        }
	}, [isAuthenticated, navigate]);

	// copy to buffer
	useEffect(() => {
		// create a copy of tictactoe game
		let newTictactoeGame = JSON.parse(JSON.stringify(tictactoeGame));

		// update only if values aren't the same
		if (!_.isEqual(newTictactoeGame, gameBuffer))
		{
			// update game state
			setBoardSquares(Array.from(tictactoeGame.gameBoard));
			setGameBuffer(newTictactoeGame);

			// update local game over state to stop sending refresh requests to backend
			if (tictactoeGame.gameState === "GAME_OVER_ERROR"
				|| tictactoeGame.gameState === "GAME_OVER_X_WINS"
				|| tictactoeGame.gameState === "GAME_OVER_O_WINS"
				|| tictactoeGame.gameState === "GAME_OVER_DRAW"
				|| tictactoeGame.gameState === "CLOSING"
			)
			{
				setGameOver(true);
			}
		}
	}, [tictactoeGame, gameBuffer])

	// set game over messages
	useEffect(() => {
		if (gameOver)
		{
			let endMessage = "";

			if (gameBuffer.gameState === "GAME_OVER_X_WINS")
			{
				setWinningPlayer("Winning Player: " + gameBuffer.xPlayerName);
				if (username === gameBuffer.xPlayerName)
				{
					endMessage = "You Win!";
				}
				else
				{
					endMessage = "You Lose!";
				}
				//endMessage = "Player '" + gameBuffer.xPlayerName + "' Wins!";
			}
			else if (gameBuffer.gameState === "GAME_OVER_O_WINS")
			{
				setWinningPlayer("Winning Player: " + gameBuffer.oPlayerName);
				if (username === gameBuffer.oPlayerName)
				{
					endMessage = "You Win!";
				}
				else
				{
					endMessage = "You Lose!";
				}
				//endMessage = "Player '" + gameBuffer.oPlayerName + "' Wins!";
			}
			else if (gameBuffer.gameState === "GAME_OVER_DRAW")
			{
				setWinningPlayer("DRAW!");
				endMessage = "Draw Game!";
			}
			else if (gameBuffer.gameState === "GAME_OVER_ERROR")
			{
				if (!gameBuffer.oPlayerReady && gameBuffer.xPlayerReady)
				{
					endMessage = "Player '" + gameBuffer.oPlayerName + "' failed to join game";
				}
				else if (!gameBuffer.xPlayerReady && gameBuffer.oPlayerReady)
				{
					endMessage = "Player '" + gameBuffer.xPlayerName + "' failed to join game";
				}
				else
				{
					endMessage = "Players failed to join game";
				}
				//endMessage = "Failed to start game"
			}

			setGameOverMessage(endMessage); 
		}
	}, [username, gameOver, sendGameCheckIn, gameBuffer])

	// set status messages
	useEffect(() => {
		if (!gameOver)
		{
			let gameStatus = "";
			if (gameBuffer.gameState === "STARTING")
			{
				// check in if this player has not yet
				if ((username === gameBuffer.oPlayerName && !gameBuffer.oPlayerReady)
					|| (username === gameBuffer.xPlayerName && !gameBuffer.xPlayerReady))
				{
					sendGameCheckIn();
				}

				if (!gameBuffer.oPlayerReady && gameBuffer.xPlayerReady)
				{
					gameStatus = "Waiting for '" + gameBuffer.oPlayerName + "' to join game";
				}
				else if (!gameBuffer.xPlayerReady && gameBuffer.oPlayerReady)
				{
					gameStatus = "Waiting for '" + gameBuffer.xPlayerName + "' to join game";
				}
				else
				{
					gameStatus = "Players failed to join game";
				}
			}
			else if (gameBuffer.gameState === "X_PLAYER_MOVING")
			{
				
				setMovingPlayer("Moving Player: " + gameBuffer.xPlayerName);
				if (username === gameBuffer.xPlayerName)
				{
					gameStatus = "Waiting for your move";
				}
				else
				{
					gameStatus = "Waiting for other player";
				}
			}
			else if (gameBuffer.gameState === "O_PLAYER_MOVING")
			{
				setMovingPlayer("Moving Player: " + gameBuffer.oPlayerName);
				if (username === gameBuffer.oPlayerName)
				{
					gameStatus = "Waiting for your move";
				}
				else
				{
					gameStatus = "Waiting for other player";
				}
			}
			else 
			{
				gameStatus = "Waiting for players to join";
			}

			setStatusMessage(gameStatus); 
		}
	}, [gameOver, setMovingPlayer, username, sendGameCheckIn, gameBuffer])

	const handleGameMove = useCallback((index) => {
		if ((gameBuffer.gameState === "X_PLAYER_MOVING" && username === gameBuffer.xPlayerName)
			|| (gameBuffer.gameState === "O_PLAYER_MOVING" && username === gameBuffer.oPlayerName))
		{
			sendGameMove(index);
		}
	},[username, sendGameMove, gameBuffer.gameState, gameBuffer.xPlayerName, gameBuffer.oPlayerName]);

	const displaySquare = (index) => {
		return (<div className="col-4 tic-box" onClick={() => handleGameMove(index)}>{boardSquares[index]}</div>);
	}

	const handleRefreshGame = useCallback(() => {
		sendGameRefresh();
	}, [sendGameRefresh]);

	const handleExitGame = useCallback(() => {
		// player wants to leave before end of game
		if (!gameOver)
		{
			sendGameForfeit();
		}

		// navigate back to playerlist after server receives forfeit message
		setPlayerIsExiting(true);
	}, [navigate, gameOver, sendGameForfeit])

	useEffect(() => 
	{
		if (playerIsExiting && gameOver)
		{
			navigate("/tictactoe/playerlist");
		}
	},[navigate, playerIsExiting, gameOver]);

	// update game state from server until game is over
	useEffect(() => {
		if (!gameOver)
		{
			const refreshInterval = setInterval(handleRefreshGame, 5000)
			return () => {
				clearInterval(refreshInterval);
			}
		}
	}, [gameOver, handleRefreshGame]);
	
	if (boardSquares && gameBuffer)
	{
		return (
			<div className="container-fluid">
				<div className="justify-content-center row">
					<div className="col-12 col-sm-6 content">
						<h1>Online Tic-Tac-Toe Game</h1>
						<p />
						<h2 className="gamestate">{gameOver && gameOverMessage}</h2>
						<h2 className="gamestate">{gameOver===false && statusMessage}</h2>
						<p />
						<div className="container-fluid tic-container">
							<div className="row tic-row">
								{displaySquare(0)}
								{displaySquare(1)}
								{displaySquare(2)}
							</div>
							<div className="row tic-row">
								{displaySquare(3)}
								{displaySquare(4)}
								{displaySquare(5)}
							</div>
							<div className="row tic-row">
								{displaySquare(6)}
								{displaySquare(7)}
								{displaySquare(8)}
							</div>
						</div>
						<div className="playerName">X Player Name: {gameBuffer.xPlayerName}</div>
						<div className="playerName">O Player Name: {gameBuffer.oPlayerName}</div>
						<p />
						<div className="status">{gameOver===false && movingPlayer}</div>
						<div className="status">{gameOver && winningPlayer}</div>
						<p />
						<button className="exit" onClick={handleExitGame}>Leave Game</button>
					</div>
				</div>
			</div>
		);
	}
	else
	{
		return (
			<div>
				Invalid Game
			</div>
		);
	}
}

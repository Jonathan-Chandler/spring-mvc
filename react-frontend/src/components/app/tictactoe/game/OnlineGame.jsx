import React, {useState, useEffect, useCallback} from 'react'
//import { BrowserRouter as Router, Navigate, Route, Routes } from 'react-router-dom'
import useAuth from '../../../authentication/AuthProvider.tsx';
import { useNavigate } from "react-router-dom";
import './Tictactoe.css';

export default function OnlineGame () {
	const { username, token, isAuthenticated, getStompSession, playerList, tictactoeGame } = useAuth();
	//const {movingPlayer, setMovingPlayer} = useState("");
	//const {gameOver, setGameOver} = useState(false);
	const [boardSquares, setBoardSquares] = useState(Array(9).fill('_'));
	const [state, setState] = useState({
		gameOver: false,
		movingPlayer: "None",
	});
    const navigate = useNavigate();

	//const updateMovingPlayer = (game) =>
	//{
	//	if (game)
	//	{
	//		if (game.gameState === "X_PLAYER_MOVING")
	//		{
	//			setMovingPlayer(game.xPlayerName);
	//		}

	//		if (game.gameState === "O_PLAYER_MOVING")
	//		{
	//			setMovingPlayer(game.oPlayerName);
	//		}
	//	}

	//	setMovingPlayer("");
	//}

	useEffect(() =>
	{
		// update game state
		setBoardSquares(Array.from(tictactoeGame.gameBoard));
	},[tictactoeGame.gameBoard])

	useEffect(() => 
	{
		if (!state.gameOver)
		{
			console.log("gamestate: " + tictactoeGame.gameState);
			if (tictactoeGame.gameState === "X_PLAYER_MOVING")
			{
				setState({...state, movingPlayer: tictactoeGame.xPlayerName});
			}

			if (tictactoeGame.gameState === "O_PLAYER_MOVING")
			{
				setState({...state, movingPlayer: tictactoeGame.oPlayerName});
			}

			//if (gameStateIsActive(tictactoeGame.gameState))
			//{
			//	console.log("Game is active (gameState = " + tictactoeGame.gameState + ")");
			//}

			if (tictactoeGame.gameState === "GAME_OVER_ERROR"
				|| tictactoeGame.gameState === "GAME_OVER_X_WINS"
				|| tictactoeGame.gameState === "GAME_OVER_O_WINS"
				|| tictactoeGame.gameState === "GAME_OVER_DRAW"
				|| tictactoeGame.gameState === "CLOSING"
			)
			{
				setState({...state, gameOver: true});
				console.log("Game ended (gameState = " + tictactoeGame.gameState + ")");
				navigate("/tictactoe/amqp");
			}

			// exit to playerlist if in error state
			if (tictactoeGame.gameState === "GAME_OVER_ERROR")
			{
				console.log("Navigate to playerlist");
				navigate("/tictactoe/amqp");
			}
		}

	}, [state, tictactoeGame.gameState])

	useEffect(() => 
	{
		if ((username === tictactoeGame.oPlayerName && !tictactoeGame.oPlayerReady)
			|| (username === tictactoeGame.xPlayerName && !tictactoeGame.xPlayerReady))
		{
			sendCheckIn();
		}
	}, [username, tictactoeGame.xPlayerReady, tictactoeGame.oPlayerReady])

		//{
		//	return game.xPlayerReady;
		//}
		//else if (game.oPlayerName === username)
		//{
		//	return game.oPlayerReady;
		//}
		//if (tictactoeGame.gameState === "X_PLAYER_MOVING")
		//{
		//	setMovingPlayer(tictactoeGame.xPlayerName);
		//}

		//if (tictactoeGame.gameState === "O_PLAYER_MOVING")
		//{

	const sendCheckIn = () => 
	{
		let stompSession = getStompSession();
		const data = {requestType: 2, requestedUser: username, moveLocation: -1}
		console.log("send request: " + JSON.stringify(data))

		let pass = token.split(' ')[1]
		const msg_destination = "/topic/from.user." + username
		stompSession.publish({
		  destination: msg_destination,
		  body: JSON.stringify(data),
		  headers: {
			  login: username, 
			  passcode: pass, 
			  "content-type":"application/json", 
			  "content-encoding":"UTF-8", 
			  "__TypeId__":"com.jonathan.web.frontend.RequestDto"
		  }
		});
	};

	const sendMove = (index) => 
	{
		console.log("requesting move index: " + index);
		let stompSession = getStompSession();
		const data = {requestType: 3, requestedUser: username, moveLocation: index}
		console.log("send request: " + JSON.stringify(data))

		let pass = token.split(' ')[1]
		const msg_destination = "/topic/from.user." + username
		stompSession.publish({
		  destination: msg_destination,
		  body: JSON.stringify(data),
		  headers: {
			  login: username, 
			  passcode: pass, 
			  "content-type":"application/json", 
			  "content-encoding":"UTF-8", 
			  "__TypeId__":"com.jonathan.web.frontend.RequestDto"
		  }
		});
	};

	const sendForfeit = () => 
	{
		console.log("requesting forfeit for user: " + username);
		let stompSession = getStompSession();
		const data = {requestType: 5, requestedUser: username, moveLocation: -1}
		console.log("send request: " + JSON.stringify(data))

		let pass = token.split(' ')[1]
		const msg_destination = "/topic/from.user." + username
		stompSession.publish({
		  destination: msg_destination,
		  body: JSON.stringify(data),
		  headers: {
			  login: username, 
			  passcode: pass, 
			  "content-type":"application/json", 
			  "content-encoding":"UTF-8", 
			  "__TypeId__":"com.jonathan.web.frontend.RequestDto"
		  }
		});

		// return to player list
		navigate("/tictactoe/amqp");
	};

	const sendRefreshGame = () => 
	{
		console.log("requesting refresh");
		let stompSession = getStompSession();
		const data = {requestType: 4, requestedUser: username, moveLocation: -1}
		console.log("send request: " + JSON.stringify(data))

		let pass = token.split(' ')[1]
		const msg_destination = "/topic/from.user." + username
		stompSession.publish({
		  destination: msg_destination,
		  body: JSON.stringify(data),
		  headers: {
			  login: username, 
			  passcode: pass, 
			  "content-type":"application/json", 
			  "content-encoding":"UTF-8", 
			  "__TypeId__":"com.jonathan.web.frontend.RequestDto"
		  }
		});
	};

	const displaySquare = (index) => {
		return (<div className="col-4 tic-box" onClick={() => sendMove(index)}>{boardSquares[index]}</div>);
	}

	const handleExitGame = () => {
		navigate("/tictactoe/amqp");
	}

	if (boardSquares && tictactoeGame)
	{
		return (
			<div className="container-fluid">
				<div className="justify-content-center row">
					<div className="col-12 col-sm-6 content">
						<h1>Online Tic-Tac-Toe Game</h1>
						<h2>X Player: {tictactoeGame.xPlayerName}</h2>
						<h2>O Player: {tictactoeGame.oPlayerName}</h2>
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
						<div className="status">Moving Player: {state.movingPlayer}</div>
						<div className="gameoverstate">{state.gameOver && tictactoeGame.gameState}</div>
						<button className="forfeit" onClick={state.gameOver && sendForfeit}>Forfeit Game</button>
						<button className="exit" onClick={state.gameOver && handleExitGame}>Exit Game</button>
						<button className="refresh" onClick={sendRefreshGame}>Refresh Game</button>
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
	//<div className="Winner">Winning Player: {winner}</div>
	//<button className="restart" onClick={handleRestart}>Restart Game</button>
}
//	const [boardSquares, setBoardSquares] = useState(Array(9).fill('_'));
//	const [winner, setWinner] = useState(false)
//	const [moveCount, setMoveCount] = useState(0)
//
//	const displaySquare = (index) => {
//		return (<div className="col-4 tic-box" onClick={() => handleClick(index)}>{boardSquares[index]}</div>);
//	}
//
//	// reset game state
//	const handleRestart = () => {
//		setWinner(false);
//		setMoveCount(0);
//
//		setBoardSquares(Array(9).fill('_'));
//	}
//
//	// add player's move and check if won
//	const handleClick = useCallback((index) => {
//		const currentBoard = [...boardSquares]
//		const currentPlayer = (moveCount % 2 === 0) ? 'X' : 'O'
//		if (currentBoard[index] !== '_' || winner)
//			return
//
//		// update board state
//		currentBoard[index] = currentPlayer;
//		setBoardSquares(currentBoard)
//	}, [moveCount, boardSquares, winner]);
//
//	// update winner
//	useEffect(() => 
//		{
//			let tempCount = 0;
//
//			for (let i = 0; i < 9; i++)
//			{
//				if (boardSquares[i] !== '_')
//					tempCount += 1;
//			}
//
//			// check 3 columns to see if either player won
//			for (let i = 0; i < 3; i++)
//			{
//				if (boardSquares[i] !== '_'
//					&& boardSquares[i] === boardSquares[i+3]
//					&& boardSquares[i] === boardSquares[i+6]
//				)
//				{
//					setWinner(boardSquares[i])
//					return
//				}
//			}
//
//			// player won by filling entire row
//			for (let i = 0; i <= 6; i+=3)
//			{
//				if (boardSquares[i] !== '_'
//					&& boardSquares[i] === boardSquares[i+1]
//					&& boardSquares[i] === boardSquares[i+2]
//				)
//				{
//					setWinner(boardSquares[i])
//					return;
//				}
//			}
//
//			// player won diagonally
//			if (boardSquares[0] !== '_'
//				&& boardSquares[0] === boardSquares[4]
//				&& boardSquares[0] === boardSquares[8]
//			)
//			{
//				setWinner(boardSquares[0])
//				return;
//			}
//			if (boardSquares[2] !== '_'
//				&& boardSquares[2] === boardSquares[4]
//				&& boardSquares[2] === boardSquares[6]
//			)
//			{
//				setWinner(boardSquares[2])
//				return;
//			}
//
//			if (tempCount === 9)
//				setWinner("none")
//
//			// update move count
//			setMoveCount(tempCount);
//
//		}, [winner, moveCount, boardSquares]);
//
//	return (
//		<div className="container-fluid">
//			<div className="justify-content-center row">
//				<div className="col-12 col-sm-6 content">
//					<h1>Tic-Tac-Toe</h1>
//					<div className="container-fluid tic-container">
//						<div className="row tic-row">
//							{displaySquare(0)}
//							{displaySquare(1)}
//							{displaySquare(2)}
//						</div>
//						<div className="row tic-row">
//							{displaySquare(3)}
//							{displaySquare(4)}
//							{displaySquare(5)}
//						</div>
//						<div className="row tic-row">
//							{displaySquare(6)}
//							{displaySquare(7)}
//							{displaySquare(8)}
//						</div>
//					</div>
//					<div className="status">Current Player: {(moveCount % 2 === 0) ? 'X' : 'O'}</div>
//					<div className="Winner">Winning Player: {winner}</div>
//					<button className="restart" onClick={handleRestart}>Restart Game</button>
//				</div>
//			</div>
//		</div>
//	);
//}


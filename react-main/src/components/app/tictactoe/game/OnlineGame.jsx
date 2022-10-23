import React, {useState, useEffect, useCallback} from 'react'
//import { BrowserRouter as Router, Navigate, Route, Routes } from 'react-router-dom'
import './Tictactoe.css';

export default function OnlineGame () {
	const [boardSquares, setBoardSquares] = useState(Array(9).fill('_'));
	const [winner, setWinner] = useState(false)
	const [moveCount, setMoveCount] = useState(0)

	const displaySquare = (index) => {
		return (<div className="col-4 tic-box" onClick={() => handleClick(index)}>{boardSquares[index]}</div>);
	}

	// reset game state
	const handleRestart = () => {
		setWinner(false);
		setMoveCount(0);

		setBoardSquares(Array(9).fill('_'));
	}

	// add player's move and check if won
	const handleClick = useCallback((index) => {
		const currentBoard = [...boardSquares]
		const currentPlayer = (moveCount % 2 === 0) ? 'X' : 'O'
		if (currentBoard[index] !== '_' || winner)
			return

		// update board state
		currentBoard[index] = currentPlayer;
		setBoardSquares(currentBoard)
	}, [moveCount, boardSquares, winner]);

	// update winner
	useEffect(() => 
		{
			let tempCount = 0;

			for (let i = 0; i < 9; i++)
			{
				if (boardSquares[i] !== '_')
					tempCount += 1;
			}

			// check 3 columns to see if either player won
			for (let i = 0; i < 3; i++)
			{
				if (boardSquares[i] !== '_'
					&& boardSquares[i] === boardSquares[i+3]
					&& boardSquares[i] === boardSquares[i+6]
				)
				{
					setWinner(boardSquares[i])
					return
				}
			}

			// player won by filling entire row
			for (let i = 0; i <= 6; i+=3)
			{
				if (boardSquares[i] !== '_'
					&& boardSquares[i] === boardSquares[i+1]
					&& boardSquares[i] === boardSquares[i+2]
				)
				{
					setWinner(boardSquares[i])
					return;
				}
			}

			// player won diagonally
			if (boardSquares[0] !== '_'
				&& boardSquares[0] === boardSquares[4]
				&& boardSquares[0] === boardSquares[8]
			)
			{
				setWinner(boardSquares[0])
				return;
			}
			if (boardSquares[2] !== '_'
				&& boardSquares[2] === boardSquares[4]
				&& boardSquares[2] === boardSquares[6]
			)
			{
				setWinner(boardSquares[2])
				return;
			}

			if (tempCount === 9)
				setWinner("none")

			// update move count
			setMoveCount(tempCount);

		}, [winner, moveCount, boardSquares]);

	return (
		<div className="container-fluid">
			<div className="justify-content-center row">
				<div className="col-12 col-sm-6 content">
					<h1>Tic-Tac-Toe</h1>
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
					<div className="status">Current Player: {(moveCount % 2 === 0) ? 'X' : 'O'}</div>
					<div className="Winner">Winning Player: {winner}</div>
					<button className="restart" onClick={handleRestart}>Restart Game</button>
				</div>
			</div>
		</div>
	);
}


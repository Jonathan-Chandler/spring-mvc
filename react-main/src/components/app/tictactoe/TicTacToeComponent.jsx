import React, {useState} from 'react'
//import { BrowserRouter as Router, Navigate, Route, Routes } from 'react-router-dom'
import '../../css/TicTacToe.css';

export default function TicTacToeComponent () {
  const [boardSquares, setBoardSquares] = useState(Array(9).fill('_'));
  const [currentPlayer, setCurrentPlayer] = useState('X')
  const [winner, setWinner] = useState(false)
  const [moveCount, setMoveCount] = useState(0)
  const [remainingMoves, setRemainingMoves] = useState(Array(9).fill('_'));

  const displaySquare = (index) => {
    return (<div className="col-4 tic-box" onClick={() => handleClick(index)}>{boardSquares[index]}</div>);
  }

  // reset game state
  const handleRestart = () => {
    setCurrentPlayer('X');
    setWinner(false);
    setMoveCount(0);

    setBoardSquares(Array(9).fill('_'));
  }

  // set winner
  const updateWinner = (currentBoard) =>
  {
    let moveCount = 0;

    // check 3 columns to see if either player won
    for (let i = 0; i < 3; i++)
    {
      if (currentBoard[i] !== '_'
          && currentBoard[i] === currentBoard[i+3]
          && currentBoard[i] === currentBoard[i+6]
         )
      {
        setWinner(currentPlayer)
        return
      }

      // add 3 if all spaces aren't empty (to check if game is a draw)
      if (currentBoard[i] !== '_'
          && currentBoard[i+3] !== '_'
          && currentBoard[i+6] !== '_'
         )
         moveCount += 3
    }

    // player won by filling entire row
    for (let i = 0; i <= 6; i+=3)
    {
      if (currentBoard[i] !== '_'
          && currentBoard[i] === currentBoard[i+1]
          && currentBoard[i] === currentBoard[i+2]
         )
      {
         setWinner(currentPlayer)
         return;
      }
    }

    // player won diagonally
    if (currentBoard[0] !== '_'
        && currentBoard[0] === currentBoard[4]
        && currentBoard[0] === currentBoard[8]
       )
    {
       setWinner(currentPlayer)
       return;
    }
    if (currentBoard[2] !== '_'
        && currentBoard[2] === currentBoard[4]
        && currentBoard[2] === currentBoard[6]
       )
    {
       setWinner(currentPlayer)
       return;
    }

    // board is full then neither player won
    if (moveCount === 9)
        setWinner("None")
  }

  // add player's move and check if won
  const handleClick = (index) => {
      const currentBoard = [...boardSquares]
      if (currentBoard[index] !== '_' || winner)
        return

      console.log("click index: " + index)
      currentBoard[index] = currentPlayer;

      // update board state
      setBoardSquares(currentBoard)

      // update winning player
      updateWinner(currentBoard)

      // switch active player
      setCurrentPlayer(currentPlayer === 'X' ? 'O' : 'X');
  }

  const makeAiMove = () =>
  {
  }

  //useEffect(() => {
  //    // get AI move if not playing against local player
  //    if (currentPlayer === 'O')
  //    {
  //      makeAiMove()
  //    }
  //}, []);

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
            <div className="status">Current Player: {currentPlayer}</div>
            <div className="Winner">Winning Player: {winner}</div>
            <button className="restart" onClick={handleRestart}>Restart Game</button>
          </div>
        </div>
      </div>
  );
}

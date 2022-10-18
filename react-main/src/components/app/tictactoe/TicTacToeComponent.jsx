import React, {useState} from 'react'
//import { BrowserRouter as Router, Navigate, Route, Routes } from 'react-router-dom'
import '../../css/TicTacToe.css';

export default function TicTacToeComponent () {
  const [boardSquares, setBoardSquares] = useState(Array(9).fill('_'));
  const [currentPlayer, setCurrentPlayer] = useState('X')
  const [winner, setWinner] = useState(false)
  const [moveCount, setMoveCount] = useState(0)
  const [remainingMoves, setRemainingMoves] = useState(Array(9).fill('_'));

  // for (let i = array.length - 1; i > 0; i--) {
  //   const j = math.floor(Math.random() * (i + 1))
  //   [array[i], array[j]] = [array[j], array[i]]
  // }

//  const displaySquare = (index) => {
//    return (
//      <button type="button" 
//              className="btn btn-lg btn-outline-primary" 
//              onClick={() => handleClick(index)}
//      >
//        <u>_{boardSquares[index]}_</u>
//      </button>
//    )
//  }

  //const displaySquare = (index) => {
  //  return (
  //    <button type="button" 
  //            className="btn btn-lg btn-outline-primary" 
  //            onClick={() => handleClick(index)}
  //    >
  //      <u>_{boardSquares[index]}_</u>
  //    </button>
  //  )
  //}
//          <td className="square h"></td>
//          <td className="square v h"></td>
//          <td className="square h"></td>
//        </tr>
//        <tr id="row3">
//          <td className="square"></td>
//          <td className="square v"></td>
//          <td className="square"></td>
// 1 = v
// 3,4,5 = h
// 4 = v
// 7 = v
// 2 5 8
//
  const displaySquare = (index) => {
    let classname = <div className="col-4 tic-box" onClick={() => handleClick(index)}>{boardSquares[index]}</div>;
    //let classname = <div className="col-4 tic-box" onClick={() => handleClick(index)}><u>_{boardSquares[index]}_</u></div>;


    //if (index === 1)
    //  classname = <div className="col-4 tic-box" onClick={() => handleClick(index)}><u>_{boardSquares[index]}_</u></div>;
    //else if (index === 3 || index === 5)
    //  classname = <div className="col-4 tic-box" onClick={() => handleClick(index)}><u>_{boardSquares[index]}_</u></div>;
    //else if (index === 4)
    //  classname = <div className="col-4 " onClick={() => handleClick(index)}><u>_{boardSquares[index]}_</u></div>;
    //else if (index === 7)
    //  classname = <div className="col-4 v" onClick={() => handleClick(index)}><u>_{boardSquares[index]}_</u></div>;

    //if (index == 1)
    //{
    //}
    return classname;
  }
//        <u>_{boardSquares[index]}_</u>

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
    // columns
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

      if (currentBoard[i] !== '_'
          && currentBoard[i+3] !== '_'
          && currentBoard[i+6] !== '_'
         )
         moveCount += 3
    }

    // rows
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

    // diagonals
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

    if (moveCount === 9)
        setWinner("None")
  }

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
        <div class="justify-content-center row">
          <div class="col-12 col-sm-6 content">
          <h1>Tic-Tac-Toe</h1>
            <div class="container-fluid tic-container">
              <div class="row tic-row">
                {displaySquare(0)}
                {displaySquare(1)}
                {displaySquare(2)}
              </div>
              <div class="row tic-row">
                {displaySquare(3)}
                {displaySquare(4)}
                {displaySquare(5)}
              </div>
              <div class="row tic-row">
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
//  return (
//    <div className="board">
//      <h1>Tic-Tac-Toe</h1>
//      <div className="board-row">
//        {displaySquare(0)}
//        {displaySquare(1)}
//        {displaySquare(2)}
//      </div>
//      <div className="board-row">
//        {displaySquare(3)}
//        {displaySquare(4)}
//        {displaySquare(5)}
//      </div>
//      <div className="board-row">
//        {displaySquare(6)}
//        {displaySquare(7)}
//        {displaySquare(8)}
//      </div>
//      <div className="status">Current Player: {currentPlayer}</div>
//      <div className="Winner">Winning Player: {winner}</div>
//      <button className="restart" onClick={handleRestart}>Restart Game</button>
//    </div>
//  )
}

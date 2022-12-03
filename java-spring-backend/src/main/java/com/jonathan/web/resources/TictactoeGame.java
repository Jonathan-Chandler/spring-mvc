package com.jonathan.web.resources;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Size;
import javax.validation.constraints.NotEmpty;
import org.springframework.lang.NonNull;
import org.slf4j.LoggerFactory;

public class TictactoeGame 
{
	public enum GameState 
	{
		STARTING,
		X_PLAYER_MOVING,
		O_PLAYER_MOVING,
		GAME_OVER_ERROR,
		GAME_OVER_X_WINS,
		GAME_OVER_O_WINS,
		GAME_OVER_DRAW,
	}

	public enum PlayerSymbol 
	{
		X_PLAYER,
		O_PLAYER,
		NONE,
	}

	final Logger logger = LoggerFactory.getLogger(this.getClass());

	public static final int BOARD_SIZE = 9;

	// game is starting/player is moving/player won/draw
	private GameState gameState;

	// user names of players
	private String xPlayerName;
	private String oPlayerName;

	// if user has checked in
	private boolean xPlayerReady;
	private boolean oPlayerReady;

	// 3x3 tictactoe game board (row = index/3; col = row%3)
	private char gameBoard[];

	// milliseconds before game times out if no action taken
	public static final long ACTION_TIMEOUT_MS = 15000;

	// system time (in milliseconds) that last allowed move was received from user
	private long lastMoveTimeMs;

	// reason the game ended
	private String gameOverMessage;

	public TictactoeGame(long gameStartTimeMs, @NonNull String xPlayer, @NonNull String oPlayer)
	{
		// initialize board
		gameBoard = new char[BOARD_SIZE];
		Arrays.fill(gameBoard, '_');

		// game is still starting
		gameState = GameState.STARTING;
		
		// record time as last
		lastMoveTimeMs = gameStartTimeMs;

		// player client has responded with waiting to play message
		xPlayerReady = false;
		oPlayerReady = false;

		// record player names
		xPlayerName = xPlayer;
		oPlayerName = oPlayer;

		gameOverMessage = "Unknown error";
	}

	// player failed to join the game
	private void setGameFailedToStartMessage()
	{
		// both players failed to join
		if (xPlayerReady == false && oPlayerReady == false)
		{
			gameOverMessage = "Players " + xPlayerName + " and " + oPlayerName + " failed to join.";
		}
		else if (xPlayerReady == false)
		{
			gameOverMessage = "Player " + xPlayerName + " failed to join.";
		}
		else if (oPlayerReady == false)
		{
			gameOverMessage = "Player " + oPlayerName + " failed to join.";
		}
		else
		{
			gameOverMessage = "Unknown game start failure: " + xPlayerName + " vs " + oPlayerName;
		}

		logger.error(gameOverMessage);
	}

	// player took too long
	private void setGameTimeoutMessage(String playerName)
	{
		gameOverMessage = "Player " + playerName + " took too long to move.";
		logger.error("Game timeout: " + xPlayerName + " vs " + oPlayerName);
		logger.error(gameOverMessage);
	}

	public Long getLastMoveTime()
	{
		return lastMoveTimeMs;
	}

	public void setLastMoveTime(Long time)
	{
		lastMoveTimeMs = time;
	}

	// check if timeout and return game state
	public GameState getGameState(long currentTimeMs)
	{
		// game already ended
		if (gameState.ordinal() >= GameState.GAME_OVER_ERROR.ordinal())
		{
			return gameState;
		}

		// game timed out
		if ((currentTimeMs - lastMoveTimeMs) > ACTION_TIMEOUT_MS)
		{
			handleTimeout();
		}

		return gameState;
	}
	
	public PlayerSymbol getPlayerSymbol(@NonNull String playerName)
	{
		if (playerName.equals(xPlayerName))
			return PlayerSymbol.X_PLAYER;

		if (playerName.equals(oPlayerName))
			return PlayerSymbol.O_PLAYER;

		// player is not in this game
		return PlayerSymbol.NONE;
	}

	public boolean handlePlayerMove(long currentTimeMs, PlayerSymbol symbol, int location)
	{
		// requested move is out of range
		if (location > 0 && location < BOARD_SIZE)
		{
			// space is empty
			if (gameBoard[location] == '_')
			{
				// handle move
				if ((symbol == PlayerSymbol.X_PLAYER && gameState == GameState.X_PLAYER_MOVING) || 
					(symbol == PlayerSymbol.O_PLAYER && gameState == GameState.O_PLAYER_MOVING))
				{
					// update last message receive time
					lastMoveTimeMs = currentTimeMs;

					// update board state
					gameBoard[location] = (symbol == PlayerSymbol.X_PLAYER) ? 'X' : 'O';

					// update current player's turn
					gameState = (gameState == GameState.X_PLAYER_MOVING) ? GameState.O_PLAYER_MOVING : GameState.X_PLAYER_MOVING;

					// check if player won
					checkWinCondition();

					return true;
				}
			}
		}

		// move was not valid
		return false;
	}

	private void setWinner(PlayerSymbol symbol)
	{
		if (symbol == PlayerSymbol.X_PLAYER)
		{
			gameState = GameState.GAME_OVER_X_WINS;
			gameOverMessage = "Player " + xPlayerName + " wins!";
			logger.error("Game ends X player: " + xPlayerName + " wins against " + oPlayerName);
		}
		if (symbol == PlayerSymbol.O_PLAYER)
		{
			gameState = GameState.GAME_OVER_O_WINS;
			gameOverMessage = "Player " + oPlayerName + " wins!";
			logger.error("Game ends O player: " + oPlayerName + " wins against " + xPlayerName);
		}
		else 
		{
			gameState = GameState.GAME_OVER_DRAW;
			gameOverMessage = "Draw!";
			logger.error("Game ends in a draw: " + xPlayerName + " vs " + oPlayerName);
		}
	}

	void checkWinCondition()
	{
		int tempCount = 0;

		for (int i = 0; i < 9; i++)
		{
			if (gameBoard[i] != '_')
				tempCount += 1;
		}

		// entire column matches
		for (int i = 0; i < 3; i++)
		{
			if (gameBoard[i] != '_'
					&& gameBoard[i] == gameBoard[i+3]
					&& gameBoard[i] == gameBoard[i+6]
			   )
			{
				setWinner(gameBoard[i] == 'X' ? PlayerSymbol.X_PLAYER : PlayerSymbol.O_PLAYER);
				return;
			}
		}

		// entire row matches
		for (int i = 0; i <= 6; i+=3)
		{
			if (gameBoard[i] != '_'
					&& gameBoard[i] == gameBoard[i+1]
					&& gameBoard[i] == gameBoard[i+2]
			   )
			{
				setWinner(gameBoard[i] == 'X' ? PlayerSymbol.X_PLAYER : PlayerSymbol.O_PLAYER);
				return;
			}
		}

		// top left -> bottom right diagonals match
		if (gameBoard[0] != '_'
				&& gameBoard[0] == gameBoard[4]
				&& gameBoard[0] == gameBoard[8]
		   )
		{
			setWinner(gameBoard[0] == 'X' ? PlayerSymbol.X_PLAYER : PlayerSymbol.O_PLAYER);
			return;
		}
		// bottom left -> top right diagonals match
		if (gameBoard[2] != '_'
				&& gameBoard[2] == gameBoard[4]
				&& gameBoard[2] == gameBoard[6]
		   )
		{
			setWinner(gameBoard[2] == 'X' ? PlayerSymbol.X_PLAYER : PlayerSymbol.O_PLAYER);
			return;
		}

		// draw
		if (tempCount == 9)
		{
			setWinner(PlayerSymbol.NONE);
		}
	}

	public void handleTimeout()
	{
		GameState newGameState;

		// game timed out
		switch (gameState)
		{
			case STARTING:
				// player(s) failed to join
				setGameFailedToStartMessage();
				gameState = GameState.GAME_OVER_ERROR;
				newGameState = GameState.GAME_OVER_ERROR;
				break;
			case X_PLAYER_MOVING:
				// player X took too long
				newGameState = GameState.GAME_OVER_O_WINS;
				setGameTimeoutMessage(oPlayerName);
				break;
			case O_PLAYER_MOVING:
				// player O took too long
				newGameState = GameState.GAME_OVER_X_WINS;
				setGameTimeoutMessage(xPlayerName);
				break;
			default:
				gameOverMessage = new String("Unknown game state: " + gameState.ordinal());
				logger.error(gameOverMessage);
				newGameState = GameState.GAME_OVER_ERROR;
		}

		gameState = newGameState;
	}

	public String getGameOverMessage()
	{
		return gameOverMessage;
	}

	public String getWinner()
	{
		if (gameState == GameState.GAME_OVER_X_WINS)
		{
			return xPlayerName;
		}
		if (gameState == GameState.GAME_OVER_O_WINS)
		{
			return oPlayerName;
		}

		return "Draw";
	}

	//@Override
    //public String toString() {
    //    return String.format("{usernames:%s}", usernames);
    //}
}


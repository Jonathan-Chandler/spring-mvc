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
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
//import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.BeforeEach;
import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.Profile;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;


@ActiveProfiles("test")
//@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TestTictactoeGameStarted
{
	private TictactoeGame game;
	private static long GAME_START_TIME = 30000;
	private static String PLAYER_X_NAME = "xName";
	private static String PLAYER_O_NAME = "oName";

	@BeforeEach
	public void initGame()
	{
		game = new TictactoeGame(GAME_START_TIME, PLAYER_X_NAME, PLAYER_O_NAME);

		// both players ready
		game.setPlayerReadyBySymbol(GAME_START_TIME, TictactoeGame.PlayerSymbol.X_PLAYER);
		game.setPlayerReadyBySymbol(GAME_START_TIME, TictactoeGame.PlayerSymbol.O_PLAYER);
	}

	@Test
	public void playerCanOnlyMoveOnTheirTurn()
	{
		// O tries to move on X turn
		assertFalse(game.handlePlayerMove(GAME_START_TIME, TictactoeGame.PlayerSymbol.O_PLAYER, 0));

		// X tries to move on X turn
		assertTrue(game.handlePlayerMove(GAME_START_TIME, TictactoeGame.PlayerSymbol.X_PLAYER, 0));

		// X tries to move on O turn
		assertFalse(game.handlePlayerMove(GAME_START_TIME, TictactoeGame.PlayerSymbol.X_PLAYER, 1));

		// O tries to move on O turn
		assertTrue(game.handlePlayerMove(GAME_START_TIME, TictactoeGame.PlayerSymbol.O_PLAYER, 1));
	}

	@Test
	public void boardIsUpdatedWhenPlayerMoves()
	{
		String expectedBoardState = "X________";

		// board displays X at top left corner
		game.handlePlayerMove(GAME_START_TIME, TictactoeGame.PlayerSymbol.X_PLAYER, 0);
		assertEquals('X', game.getBoardTile(0));
		assertEquals(expectedBoardState, game.getBoard());

		// board displays O in top middle column
		expectedBoardState = "XO_______";
		assertTrue(game.handlePlayerMove(GAME_START_TIME, TictactoeGame.PlayerSymbol.O_PLAYER, 1));
		assertEquals('O', game.getBoardTile(1));
		assertEquals(expectedBoardState, game.getBoard());
	}

	@Test
	public void playerWinDetectedInTopRow()
	{
		//  X | X | X
		// -----------
		//  O | O | 
		// -----------
		//    |   |
		String expectedBoardState = "XXXOO____";
		String expectedGameOverMessage = "Player " + PLAYER_X_NAME + " wins!";
		String expectedWinningPlayer = PLAYER_X_NAME;

		// board displays X at top left corner
		game.handlePlayerMove(GAME_START_TIME, TictactoeGame.PlayerSymbol.X_PLAYER, 0);
		game.handlePlayerMove(GAME_START_TIME, TictactoeGame.PlayerSymbol.O_PLAYER, 3);
		game.handlePlayerMove(GAME_START_TIME, TictactoeGame.PlayerSymbol.X_PLAYER, 1);
		game.handlePlayerMove(GAME_START_TIME, TictactoeGame.PlayerSymbol.O_PLAYER, 4);
		game.handlePlayerMove(GAME_START_TIME, TictactoeGame.PlayerSymbol.X_PLAYER, 2);

		// board displays all X in top row
		assertEquals(expectedBoardState, game.getBoard());

		// game state shows that X player won
		assertEquals(TictactoeGame.GameState.GAME_OVER_X_WINS, game.getGameState(GAME_START_TIME));

		// message shows that X player won
		assertEquals(expectedGameOverMessage, game.getGameOverMessage());
		assertEquals(game.getWinner(), expectedWinningPlayer);
	}

	@Test
	public void playerWinDetectedInLastColumn()
	{
		//  X | X | O
		// -----------
		//  X |   | O
		// -----------
		//    |   | O
		String expectedBoardState = "XXOX_O__O";
		String expectedGameOverMessage = "Player " + PLAYER_O_NAME + " wins!";
		String expectedWinningPlayer = PLAYER_O_NAME;

		// set up game
		game.handlePlayerMove(GAME_START_TIME, TictactoeGame.PlayerSymbol.X_PLAYER, 0);
		game.handlePlayerMove(GAME_START_TIME, TictactoeGame.PlayerSymbol.O_PLAYER, 2);
		game.handlePlayerMove(GAME_START_TIME, TictactoeGame.PlayerSymbol.X_PLAYER, 1);
		game.handlePlayerMove(GAME_START_TIME, TictactoeGame.PlayerSymbol.O_PLAYER, 5);
		game.handlePlayerMove(GAME_START_TIME, TictactoeGame.PlayerSymbol.X_PLAYER, 3);
		game.handlePlayerMove(GAME_START_TIME, TictactoeGame.PlayerSymbol.O_PLAYER, 8);

		// board displays O in right column
		assertEquals(expectedBoardState, game.getBoard());

		// game state shows that O player won
		assertEquals(TictactoeGame.GameState.GAME_OVER_O_WINS, game.getGameState(GAME_START_TIME));

		// message shows that O player won
		assertEquals(expectedGameOverMessage, game.getGameOverMessage());
		assertEquals(game.getWinner(), expectedWinningPlayer);
	}

	@Test
	public void playerWinDetectedInTopLeftDiagonal()
	{
		//  X | O | O
		// -----------
		//    | X | 
		// -----------
		//    |   | X
		String expectedBoardState = "XOO_X___X";
		String expectedGameOverMessage = "Player " + PLAYER_X_NAME + " wins!";
		String expectedWinningPlayer = PLAYER_X_NAME;

		// board displays X at top left corner
		game.handlePlayerMove(GAME_START_TIME, TictactoeGame.PlayerSymbol.X_PLAYER, 0);
		game.handlePlayerMove(GAME_START_TIME, TictactoeGame.PlayerSymbol.O_PLAYER, 1);
		game.handlePlayerMove(GAME_START_TIME, TictactoeGame.PlayerSymbol.X_PLAYER, 4);
		game.handlePlayerMove(GAME_START_TIME, TictactoeGame.PlayerSymbol.O_PLAYER, 2);
		game.handlePlayerMove(GAME_START_TIME, TictactoeGame.PlayerSymbol.X_PLAYER, 8);

		// board displays all X in top row
		assertEquals(expectedBoardState, game.getBoard());

		// game state shows that X player won
		assertEquals(TictactoeGame.GameState.GAME_OVER_X_WINS, game.getGameState(GAME_START_TIME));

		// message shows that X player won
		assertEquals(expectedGameOverMessage, game.getGameOverMessage());
		assertEquals(game.getWinner(), expectedWinningPlayer);
	}

	@Test
	public void playerWinDetectedInTopRightDiagonal()
	{
		//  X | X | O
		// -----------
		//  X | O |  
		// -----------
		//  O |   |  
		String expectedBoardState = "XXOXO_O__";
		String expectedGameOverMessage = "Player " + PLAYER_O_NAME + " wins!";
		String expectedWinningPlayer = PLAYER_O_NAME;

		// set up game
		game.handlePlayerMove(GAME_START_TIME, TictactoeGame.PlayerSymbol.X_PLAYER, 0);
		game.handlePlayerMove(GAME_START_TIME, TictactoeGame.PlayerSymbol.O_PLAYER, 2);
		game.handlePlayerMove(GAME_START_TIME, TictactoeGame.PlayerSymbol.X_PLAYER, 1);
		game.handlePlayerMove(GAME_START_TIME, TictactoeGame.PlayerSymbol.O_PLAYER, 4);
		game.handlePlayerMove(GAME_START_TIME, TictactoeGame.PlayerSymbol.X_PLAYER, 3);
		game.handlePlayerMove(GAME_START_TIME, TictactoeGame.PlayerSymbol.O_PLAYER, 6);

		// board displays O in right column
		assertEquals(expectedBoardState, game.getBoard());

		// game state shows that O player won
		assertEquals(TictactoeGame.GameState.GAME_OVER_O_WINS, game.getGameState(GAME_START_TIME));

		// message shows that O player won
		assertEquals(expectedGameOverMessage, game.getGameOverMessage());
		assertEquals(game.getWinner(), expectedWinningPlayer);
	}

	@Test
	public void tiedGameIsDetected()
	{
		//  X | O | X
		// -----------
		//  X | O | X
		// -----------
		//  O | X | O
		String expectedGameOverMessage = "Draw!";
		String expectedWinningPlayer = "Draw";

		// set up game
		game.handlePlayerMove(GAME_START_TIME, TictactoeGame.PlayerSymbol.X_PLAYER, 0);
		game.handlePlayerMove(GAME_START_TIME, TictactoeGame.PlayerSymbol.O_PLAYER, 1);
		game.handlePlayerMove(GAME_START_TIME, TictactoeGame.PlayerSymbol.X_PLAYER, 2);
		game.handlePlayerMove(GAME_START_TIME, TictactoeGame.PlayerSymbol.O_PLAYER, 4);
		game.handlePlayerMove(GAME_START_TIME, TictactoeGame.PlayerSymbol.X_PLAYER, 3);
		game.handlePlayerMove(GAME_START_TIME, TictactoeGame.PlayerSymbol.O_PLAYER, 6);
		game.handlePlayerMove(GAME_START_TIME, TictactoeGame.PlayerSymbol.X_PLAYER, 5);
		game.handlePlayerMove(GAME_START_TIME, TictactoeGame.PlayerSymbol.O_PLAYER, 8);
		game.handlePlayerMove(GAME_START_TIME, TictactoeGame.PlayerSymbol.X_PLAYER, 7);

		// game state shows that game is a draw
		assertEquals(TictactoeGame.GameState.GAME_OVER_DRAW, game.getGameState(GAME_START_TIME));
		assertEquals(game.getGameOverMessage(), expectedGameOverMessage);
		assertEquals(game.getWinner(), expectedWinningPlayer);
	}

	@Test
	public void playerCantMoveIfGameOver()
	{
		//  X | X | O
		// -----------
		//  X | O |  
		// -----------
		//  O |   |  

		// set up game
		game.handlePlayerMove(GAME_START_TIME, TictactoeGame.PlayerSymbol.X_PLAYER, 0);
		game.handlePlayerMove(GAME_START_TIME, TictactoeGame.PlayerSymbol.O_PLAYER, 2);
		game.handlePlayerMove(GAME_START_TIME, TictactoeGame.PlayerSymbol.X_PLAYER, 1);
		game.handlePlayerMove(GAME_START_TIME, TictactoeGame.PlayerSymbol.O_PLAYER, 4);
		game.handlePlayerMove(GAME_START_TIME, TictactoeGame.PlayerSymbol.X_PLAYER, 3);
		game.handlePlayerMove(GAME_START_TIME, TictactoeGame.PlayerSymbol.O_PLAYER, 6);

		// game state shows that O player won
		assertEquals(TictactoeGame.GameState.GAME_OVER_O_WINS, game.getGameState(GAME_START_TIME));

		// both players can't move
		assertFalse(game.handlePlayerMove(GAME_START_TIME, TictactoeGame.PlayerSymbol.X_PLAYER, 8));
		assertFalse(game.handlePlayerMove(GAME_START_TIME, TictactoeGame.PlayerSymbol.O_PLAYER, 7));
		assertEquals(TictactoeGame.GameState.GAME_OVER_O_WINS, game.getGameState(GAME_START_TIME));
	}
}


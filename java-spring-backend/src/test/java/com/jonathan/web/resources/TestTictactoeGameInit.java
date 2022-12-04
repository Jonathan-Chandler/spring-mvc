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
public class TestTictactoeGameInit
{
	private TictactoeGame game;
	private static long GAME_START_TIME = 30000;
	private static String PLAYER_X_NAME = "xName";
	private static String PLAYER_O_NAME = "oName";

	@BeforeEach
	public void initGame()
	{
		game = new TictactoeGame(GAME_START_TIME, PLAYER_X_NAME, PLAYER_O_NAME);
	}

	@Test
	public void playerNamesShouldMatchSymbols()
	{
		// return expected symbols given player name
		assertEquals(TictactoeGame.PlayerSymbol.X_PLAYER, game.getPlayerSymbol(PLAYER_X_NAME));
		assertEquals(TictactoeGame.PlayerSymbol.O_PLAYER, game.getPlayerSymbol(PLAYER_O_NAME));

		// return NONE PlayerSymbol given invalid player name
		assertEquals(TictactoeGame.PlayerSymbol.NONE, game.getPlayerSymbol("notPlaying"));
		assertEquals(TictactoeGame.PlayerSymbol.NONE, game.getPlayerSymbol(""));
	}

	@Test
	public void gameStateReturnsTimeoutOnStartup()
	{
		long beforeTimeout = GAME_START_TIME + TictactoeGame.ACTION_TIMEOUT_MS;
		long afterTimeout = GAME_START_TIME + TictactoeGame.ACTION_TIMEOUT_MS + 1;
		//System.out.println("beforeTimeout: " + beforeTimeout);
		//System.out.println("afterTimeout: " + afterTimeout);

		// game times out after failing to start
		assertEquals(TictactoeGame.GameState.STARTING, game.getGameState(beforeTimeout));
		assertEquals(TictactoeGame.GameState.GAME_OVER_ERROR, game.getGameState(afterTimeout));
		String expectGameOverMessage = "Players " + PLAYER_X_NAME + " and " + PLAYER_O_NAME + " failed to join.";
		assertEquals(expectGameOverMessage, game.getGameOverMessage());
	}

	@Test
	public void gameStateTimeoutIfPlayerFailsToJoin()
	{
		long afterTimeout = GAME_START_TIME + TictactoeGame.ACTION_TIMEOUT_MS + 1;

		// O player is ready
		game.setPlayerReadyBySymbol(GAME_START_TIME, TictactoeGame.PlayerSymbol.O_PLAYER);

		// game times out after failing to start
		assertEquals(TictactoeGame.GameState.GAME_OVER_ERROR, game.getGameState(afterTimeout));

		// message matches timed out player
		String expectGameOverMessage = "Player " + PLAYER_X_NAME + " failed to join.";
		assertEquals(expectGameOverMessage, game.getGameOverMessage());
	}
}



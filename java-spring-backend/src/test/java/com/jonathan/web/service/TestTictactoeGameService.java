package com.jonathan.web.service;

import com.jonathan.web.service.TictactoeGameService;
import com.jonathan.web.service.TictactoeGameServiceImpl;
import com.jonathan.web.service.TictactoePlayerListService;
import com.jonathan.web.service.TictactoePlayerListServiceImpl;

import java.util.List;
import java.util.ArrayList;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import com.jonathan.web.resources.TictactoeGame;
import com.jonathan.web.resources.TictactoePlayerListDto;
import com.jonathan.web.resources.OnlineUserDto;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import com.jonathan.web.resources.TictactoePlayer;
import org.springframework.lang.NonNull;

import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.jonathan.web.resources.TictactoeRequestDto;

@ActiveProfiles("test")
//@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TestTictactoeGameService
{
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	private static long CURRENT_TIME = 60000;
	private static long EXPIRE_TIMEOUT = 30000;
	private static long MOVE_TIME = 10000;
	private static long DELETE_TIMEOUT = 30000;
	private static String[] PLAYER_NAMES = {"player0", "player1", "player2", "player3"};

	@Test
	public void playersCanGetGameState()
	{
		TictactoeGameService gameService = new TictactoeGameServiceImpl();
		TictactoePlayerListService playerListService = new TictactoePlayerListServiceImpl(gameService);
		TictactoeRequestDto response; 
		TictactoePlayerListDto playerListDto;
		List<String> availableUsers;
		TictactoeGame.GameState gameState;

		// both players are in the player list
		playerListDto = playerListService.getPlayerList(CURRENT_TIME, PLAYER_NAMES[0]);
		playerListDto = playerListService.getPlayerList(CURRENT_TIME, PLAYER_NAMES[1]);

		// player0 requests a match against player1
		response = playerListService.addPlayerRequest(CURRENT_TIME, PLAYER_NAMES[0], PLAYER_NAMES[1]);
		response = playerListService.addPlayerRequest(CURRENT_TIME, PLAYER_NAMES[1], PLAYER_NAMES[0]);
		assertEquals(TictactoeRequestDto.ResponseType.START_GAME, response.getResponseType());

		// game state is starting until both players check in
		gameState = gameService.getGameStateByPlayerName(CURRENT_TIME, PLAYER_NAMES[0]);
		assertEquals(TictactoeGame.GameState.STARTING, gameState);

		// x player should be next to move after both players check in
		gameState = gameService.getGameStateByPlayerName(CURRENT_TIME, PLAYER_NAMES[1]);
		assertEquals(TictactoeGame.GameState.X_PLAYER_MOVING, gameState);
	}

	@Test
	public void playersAlternateTurns()
	{
		TictactoeGameService gameService = new TictactoeGameServiceImpl();
		TictactoePlayerListService playerListService = new TictactoePlayerListServiceImpl(gameService);
		TictactoeRequestDto response; 
		TictactoePlayerListDto playerListDto;
		List<String> availableUsers;
		TictactoeGame.GameState gameState;
		TictactoeGameService.GameServiceResponse gameResponse;

		playerListDto = playerListService.getPlayerList(CURRENT_TIME, PLAYER_NAMES[1]);
		response = playerListService.addPlayerRequest(CURRENT_TIME, PLAYER_NAMES[0], PLAYER_NAMES[1]);
		response = playerListService.addPlayerRequest(CURRENT_TIME, PLAYER_NAMES[1], PLAYER_NAMES[0]);

		// game state is starting until both players check in
		gameState = gameService.getGameStateByPlayerName(CURRENT_TIME, PLAYER_NAMES[0]);
		gameState = gameService.getGameStateByPlayerName(CURRENT_TIME, PLAYER_NAMES[1]);

		gameResponse = gameService.sendTictactoeMove(CURRENT_TIME, PLAYER_NAMES[1], 0);
		assertEquals(TictactoeGameService.GameServiceResponse.SUCCESS, gameResponse);
		
		gameResponse = gameService.sendTictactoeMove(CURRENT_TIME, PLAYER_NAMES[0], 1);
		assertEquals(TictactoeGameService.GameServiceResponse.SUCCESS, gameResponse);

		gameResponse = gameService.sendTictactoeMove(CURRENT_TIME, PLAYER_NAMES[1], 2);
		assertEquals(TictactoeGameService.GameServiceResponse.SUCCESS, gameResponse);
	}

	@Test
	public void gameTimesOut()
	{
		TictactoeGameService gameService = new TictactoeGameServiceImpl();
		TictactoePlayerListService playerListService = new TictactoePlayerListServiceImpl(gameService);
		TictactoeRequestDto response; 
		TictactoePlayerListDto playerListDto;
		List<String> availableUsers;
		TictactoeGame.GameState gameState;
		TictactoeGameService.GameServiceResponse gameResponse;
		TictactoeGame gameCopy;

		playerListDto = playerListService.getPlayerList(CURRENT_TIME, PLAYER_NAMES[1]);
		response = playerListService.addPlayerRequest(CURRENT_TIME, PLAYER_NAMES[0], PLAYER_NAMES[1]);
		response = playerListService.addPlayerRequest(CURRENT_TIME, PLAYER_NAMES[1], PLAYER_NAMES[0]);

		// game state is starting until both players check in
		gameState = gameService.getGameStateByPlayerName(CURRENT_TIME, PLAYER_NAMES[0]);
		gameState = gameService.getGameStateByPlayerName(CURRENT_TIME, PLAYER_NAMES[1]);

		gameResponse = gameService.sendTictactoeMove(CURRENT_TIME, PLAYER_NAMES[1], 0);
		assertEquals(TictactoeGameService.GameServiceResponse.SUCCESS, gameResponse);
		
		gameResponse = gameService.sendTictactoeMove(CURRENT_TIME+(MOVE_TIME), PLAYER_NAMES[0], 1);
		assertEquals(TictactoeGameService.GameServiceResponse.SUCCESS, gameResponse);

		gameResponse = gameService.sendTictactoeMove(CURRENT_TIME+(MOVE_TIME*2), PLAYER_NAMES[1], 2);
		assertEquals(TictactoeGameService.GameServiceResponse.SUCCESS, gameResponse);

		// player O moves after timeout
		gameResponse = gameService.sendTictactoeMove(1+CURRENT_TIME+(MOVE_TIME*2)+EXPIRE_TIMEOUT, PLAYER_NAMES[0], 2);
		assertEquals(TictactoeGameService.GameServiceResponse.INVALID_MOVE, gameResponse);

		// player X wins by timeout
		gameCopy = gameService.getGameCopyByPlayerName(1+CURRENT_TIME+(MOVE_TIME*2)+EXPIRE_TIMEOUT, PLAYER_NAMES[0]);
		assertEquals(TictactoeGame.GameState.GAME_OVER_X_WINS, gameCopy.getGameState(1+CURRENT_TIME+(MOVE_TIME*2)+EXPIRE_TIMEOUT));

		// game moves to closing state
		gameCopy = gameService.getGameCopyByPlayerName(1+CURRENT_TIME+(MOVE_TIME*2)+EXPIRE_TIMEOUT+DELETE_TIMEOUT, PLAYER_NAMES[0]);
		assertEquals(TictactoeGame.GameState.CLOSING, gameCopy.getGameState(1+CURRENT_TIME+(MOVE_TIME*2)+EXPIRE_TIMEOUT+DELETE_TIMEOUT));
	}
}

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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import com.jonathan.web.resources.TictactoeRequestDto;
import com.jonathan.web.resources.TictactoeGameDto;

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

	@Autowired
	TictactoeGameService gameService;

	@Autowired
	TictactoePlayerListService playerListService;

	@Test
	public void playersCanGetGameState()
	{
		//TictactoeGameService gameService = new TictactoeGameServiceImpl();
		playerListService.reset();
		gameService.reset();
		//TictactoePlayerListService playerListService = new TictactoePlayerListServiceImpl(gameService);
		TictactoeRequestDto response; 
		TictactoePlayerListDto playerListDto;
		List<String> availableUsers;
		TictactoeGameDto gameCopy;
		TictactoeGame.GameState gameState;
		long currentTime = CURRENT_TIME;

		// both players are in the player list
		playerListDto = playerListService.getPlayerList(currentTime, PLAYER_NAMES[0]);
		playerListDto = playerListService.getPlayerList(currentTime, PLAYER_NAMES[1]);

		// player0 requests a match against player1
		response = playerListService.addPlayerRequest(currentTime, PLAYER_NAMES[0], PLAYER_NAMES[1]);
		response = playerListService.addPlayerRequest(currentTime, PLAYER_NAMES[1], PLAYER_NAMES[0]);
		assertEquals(TictactoeRequestDto.ResponseType.START_GAME, response.getResponseType());

		// game state is starting until both players check in
		gameCopy = gameService.getGameCopyByPlayerName(currentTime, PLAYER_NAMES[0]);
		gameState = gameCopy.getGameState();
		assertEquals(TictactoeGame.GameState.STARTING, gameState);

		// both players check in
		gameService.checkInPlayer(currentTime, PLAYER_NAMES[0]);
		gameService.checkInPlayer(currentTime, PLAYER_NAMES[1]);

		// x player should be next to move after both players check in
		gameCopy = gameService.getGameCopyByPlayerName(currentTime, PLAYER_NAMES[1]);
		gameState = gameCopy.getGameState();
		assertEquals(TictactoeGame.GameState.X_PLAYER_MOVING, gameState);
	}

	@Test
	public void playersAlternateTurns()
	{
		//TictactoeGameService gameService = new TictactoeGameServiceImpl();
		playerListService.reset();
		gameService.reset();
		//TictactoePlayerListService playerListService = new TictactoePlayerListServiceImpl(gameService);
		TictactoeRequestDto response; 
		TictactoePlayerListDto playerListDto;
		List<String> availableUsers;
		TictactoeGame.GameState gameState;
		TictactoeGameService.GameServiceResponse gameResponse;
		TictactoeGameDto gameCopy;
		long currentTime = CURRENT_TIME;

		playerListDto = playerListService.getPlayerList(currentTime, PLAYER_NAMES[1]);
		response = playerListService.addPlayerRequest(currentTime, PLAYER_NAMES[0], PLAYER_NAMES[1]);
		response = playerListService.addPlayerRequest(currentTime, PLAYER_NAMES[1], PLAYER_NAMES[0]);

		// game state is starting until both players check in
		gameService.checkInPlayer(currentTime, PLAYER_NAMES[0]);
		gameService.checkInPlayer(currentTime, PLAYER_NAMES[1]);
		
		gameService.getGameCopyByPlayerName(currentTime, PLAYER_NAMES[0]);
		gameCopy = gameService.getGameCopyByPlayerName(currentTime, PLAYER_NAMES[1]);

		gameResponse = gameService.sendTictactoeMove(currentTime, PLAYER_NAMES[1], 0);
		assertEquals(TictactoeGameService.GameServiceResponse.SUCCESS, gameResponse);
		
		gameResponse = gameService.sendTictactoeMove(currentTime, PLAYER_NAMES[0], 1);
		assertEquals(TictactoeGameService.GameServiceResponse.SUCCESS, gameResponse);

		gameResponse = gameService.sendTictactoeMove(currentTime, PLAYER_NAMES[1], 2);
		assertEquals(TictactoeGameService.GameServiceResponse.SUCCESS, gameResponse);
	}

	@Test
	public void gameTimesOut()
	{
		//TictactoeGameService gameService = new TictactoeGameServiceImpl();
		playerListService.reset();
		gameService.reset();
		//TictactoePlayerListService playerListService = new TictactoePlayerListServiceImpl(gameService);
		TictactoeRequestDto response; 
		TictactoePlayerListDto playerListDto;
		List<String> availableUsers;
		TictactoeGame.GameState gameState;
		TictactoeGameService.GameServiceResponse gameResponse;
		TictactoeGameDto gameCopy;
		long currentTime = CURRENT_TIME;
		long gameIdPlayer0;
		long gameIdPlayer1;
		long playerListGameIdPlayer0;
		long playerListGameIdPlayer1;

		// xPlayer: player[1]
		// oPlayer: player[0]
		playerListDto = playerListService.getPlayerList(currentTime, PLAYER_NAMES[1]);
		response = playerListService.addPlayerRequest(currentTime, PLAYER_NAMES[0], PLAYER_NAMES[1]);
		response = playerListService.addPlayerRequest(currentTime, PLAYER_NAMES[1], PLAYER_NAMES[0]);

		// game state is starting until both players check in
		gameService.checkInPlayer(currentTime, PLAYER_NAMES[0]);
		gameService.checkInPlayer(currentTime, PLAYER_NAMES[1]);
		
		gameCopy = gameService.getGameCopyByPlayerName(currentTime, PLAYER_NAMES[0]);
		gameCopy = gameService.getGameCopyByPlayerName(currentTime, PLAYER_NAMES[1]);

		gameResponse = gameService.sendTictactoeMove(currentTime, PLAYER_NAMES[1], 0);
		assertEquals(TictactoeGameService.GameServiceResponse.SUCCESS, gameResponse);
		
		currentTime += MOVE_TIME;
		gameResponse = gameService.sendTictactoeMove(currentTime, PLAYER_NAMES[0], 1);
		assertEquals(TictactoeGameService.GameServiceResponse.SUCCESS, gameResponse);

		currentTime += MOVE_TIME;
		gameResponse = gameService.sendTictactoeMove(currentTime, PLAYER_NAMES[1], 2);
		assertEquals(TictactoeGameService.GameServiceResponse.SUCCESS, gameResponse);

		// moves are recorded
		gameCopy = gameService.getGameCopyByPlayerName(currentTime, PLAYER_NAMES[0]);
		String boardAsString = new String(gameCopy.getBoard());
		assertEquals("XOX______", boardAsString);
		//gameCopy.printBoard();

		// players aren't in list of available players
		playerListDto = playerListService.getPlayerList(currentTime, PLAYER_NAMES[0]);
		playerListDto = playerListService.getPlayerList(currentTime, PLAYER_NAMES[1]);
		availableUsers = playerListDto.getAvailableUsers();
		assertEquals(0, availableUsers.size());

		// game id matches for both players
		gameIdPlayer0 = gameService.getPlayerGameId(currentTime, PLAYER_NAMES[0]);
		gameIdPlayer1 = gameService.getPlayerGameId(currentTime, PLAYER_NAMES[1]);
		assertNotEquals(gameIdPlayer0, -1);
		assertEquals(gameIdPlayer0, gameIdPlayer1);

		// getting player list returns with already in game
		playerListDto = playerListService.getPlayerList(currentTime, PLAYER_NAMES[0]);
		assertEquals(TictactoePlayerListDto.ServiceResponse.PLAYER_IN_GAME, playerListDto.getServiceResponse());
		playerListDto = playerListService.getPlayerList(currentTime, PLAYER_NAMES[1]);
		assertEquals(TictactoePlayerListDto.ServiceResponse.PLAYER_IN_GAME, playerListDto.getServiceResponse());

		// player O moves after timeout
		currentTime += EXPIRE_TIMEOUT;
		gameResponse = gameService.sendTictactoeMove(currentTime, PLAYER_NAMES[0], 3);
		assertEquals(TictactoeGameService.GameServiceResponse.INVALID_MOVE, gameResponse);

		// player X moves after already won
		gameResponse = gameService.sendTictactoeMove(currentTime, PLAYER_NAMES[1], 3);
		assertEquals(TictactoeGameService.GameServiceResponse.INVALID_MOVE, gameResponse);

		// player X won by timeout
		gameCopy = gameService.getGameCopyByPlayerName(currentTime, PLAYER_NAMES[0]);
		assertEquals(TictactoeGame.GameState.GAME_OVER_X_WINS, gameCopy.getGameState());

		// players can join player list after game ends
		playerListDto = playerListService.getPlayerList(currentTime, PLAYER_NAMES[0]);
		playerListDto = playerListService.getPlayerList(currentTime, PLAYER_NAMES[1]);
		availableUsers = playerListDto.getAvailableUsers();
		gameService.printGameIds();
		assertEquals(1, availableUsers.size());

		// game is erased
		currentTime += DELETE_TIMEOUT;
		gameCopy = gameService.getGameCopyByPlayerName(currentTime, PLAYER_NAMES[0]);
		gameService.printGameIds();
		assertEquals(TictactoeGame.GameState.GAME_OVER_ERROR, gameCopy.getGameState());
	}

	//@Test
	//public void gameTimesOut()
	//{
	//	TictactoeGameService gameService = new TictactoeGameServiceImpl();
	//	TictactoePlayerListService playerListService = new TictactoePlayerListServiceImpl(gameService);
	//	TictactoeRequestDto response; 
	//	TictactoePlayerListDto playerListDto;
	//	List<String> availableUsers;
	//	TictactoeGame.GameState gameState;
	//	TictactoeGameService.GameServiceResponse gameResponse;
	//	TictactoeGame gameCopy;

	//	playerListDto = playerListService.getPlayerList(CURRENT_TIME, PLAYER_NAMES[1]);
	//	response = playerListService.addPlayerRequest(CURRENT_TIME, PLAYER_NAMES[0], PLAYER_NAMES[1]);
	//	response = playerListService.addPlayerRequest(CURRENT_TIME, PLAYER_NAMES[1], PLAYER_NAMES[0]);

	//	// game state is starting until both players check in
	//	gameState = gameService.getGameStateByPlayerName(CURRENT_TIME, PLAYER_NAMES[0]);
	//	gameState = gameService.getGameStateByPlayerName(CURRENT_TIME, PLAYER_NAMES[1]);

	//	gameResponse = gameService.sendTictactoeMove(CURRENT_TIME, PLAYER_NAMES[1], 0);
	//	assertEquals(TictactoeGameService.GameServiceResponse.SUCCESS, gameResponse);
	//	
	//	gameResponse = gameService.sendTictactoeMove(CURRENT_TIME+(MOVE_TIME), PLAYER_NAMES[0], 1);
	//	assertEquals(TictactoeGameService.GameServiceResponse.SUCCESS, gameResponse);

	//	gameResponse = gameService.sendTictactoeMove(CURRENT_TIME+(MOVE_TIME*2), PLAYER_NAMES[1], 2);
	//	assertEquals(TictactoeGameService.GameServiceResponse.SUCCESS, gameResponse);

	//	// player O moves after timeout
	//	gameResponse = gameService.sendTictactoeMove(1+CURRENT_TIME+(MOVE_TIME*2)+EXPIRE_TIMEOUT, PLAYER_NAMES[0], 2);
	//	assertEquals(TictactoeGameService.GameServiceResponse.INVALID_MOVE, gameResponse);

	//	// player X wins by timeout
	//	gameCopy = gameService.getGameCopyByPlayerName(1+CURRENT_TIME+(MOVE_TIME*2)+EXPIRE_TIMEOUT, PLAYER_NAMES[0]);
	//	assertEquals(TictactoeGame.GameState.GAME_OVER_X_WINS, gameCopy.getGameState(1+CURRENT_TIME+(MOVE_TIME*2)+EXPIRE_TIMEOUT));

	//	// game moves to closing state
	//	gameCopy = gameService.getGameCopyByPlayerName(1+CURRENT_TIME+(MOVE_TIME*2)+EXPIRE_TIMEOUT+DELETE_TIMEOUT, PLAYER_NAMES[0]);
	//	assertEquals(TictactoeGame.GameState.CLOSING, gameCopy.getGameState(1+CURRENT_TIME+(MOVE_TIME*2)+EXPIRE_TIMEOUT+DELETE_TIMEOUT));
	//}

}

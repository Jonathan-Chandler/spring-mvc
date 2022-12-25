package com.jonathan.web.service;

import com.jonathan.web.service.TictactoePlayerListService;
import com.jonathan.web.service.TictactoePlayerListServiceImpl;

import java.util.List;
import java.util.ArrayList;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import com.jonathan.web.resources.TictactoeGame;
import com.jonathan.web.resources.TictactoePlayerListDto;
import com.jonathan.web.resources.OnlineUserDto;
import com.jonathan.web.resources.TictactoeRequestDto;
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

import org.springframework.test.context.junit4.SpringRunner;
import org.junit.runner.RunWith;


@ActiveProfiles("test")
//@ExtendWith(SpringExtension.class)
//@SpringBootTest(classes = Application.class,
//    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SpringBootTest
@RunWith(SpringRunner.class )
public class TestTictactoePlayerListService
{
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	private static long CURRENT_TIME = 60000;
	private static long EXPIRE_TIMEOUT = 30000;
	private static String[] PLAYER_NAMES = {"player0", "player1", "player2", "player3"};

	//@Autowired
	//private TictactoePlayerListServiceImpl playerListService;

	@Autowired
	TictactoePlayerListService playerListService;

	@Autowired
	TictactoeGameService gameService;

	public void printPlayers(List<String> playerList)
	{
		for (int i = 0; i < playerList.size(); i++)
		{
			logger.info("PlayerList[" + i + "]: " + playerList.get(i));
		}
	}

	@Test
	public void getPlayerListAddsPlayers()
	{
		//TictactoePlayerListService playerListService = new TictactoePlayerListServiceImpl(new TictactoeGameServiceImpl());
		playerListService.reset();
		gameService.reset();

		playerListService.getPlayerList(CURRENT_TIME, PLAYER_NAMES[0]);

		TictactoePlayerListDto playerListDto;
		List<String> availableUsers;

		// list of players should return no other players for the first to join
		playerListDto = playerListService.getPlayerList(CURRENT_TIME, PLAYER_NAMES[0]);
		availableUsers = playerListDto.getAvailableUsers();
		assertEquals(0, availableUsers.size());

		// list of players should return 1 other player if 2 join
		playerListDto = playerListService.getPlayerList(CURRENT_TIME, PLAYER_NAMES[1]);
		assertEquals(1, playerListDto.getAvailableUsersCount());
		availableUsers = playerListDto.getAvailableUsers();
		assertEquals(PLAYER_NAMES[0], availableUsers.get(0));
		playerListDto = playerListService.getPlayerList(CURRENT_TIME, PLAYER_NAMES[0]);
		assertEquals(1, playerListDto.getAvailableUsersCount());
		availableUsers = playerListDto.getAvailableUsers();
		assertEquals(PLAYER_NAMES[1], availableUsers.get(0));

		// list of players should return 3 other players if 4 join
		playerListDto = playerListService.getPlayerList(CURRENT_TIME, PLAYER_NAMES[0]);
		playerListDto = playerListService.getPlayerList(CURRENT_TIME, PLAYER_NAMES[1]);
		playerListDto = playerListService.getPlayerList(CURRENT_TIME, PLAYER_NAMES[2]);
		playerListDto = playerListService.getPlayerList(CURRENT_TIME, PLAYER_NAMES[3]);
		assertEquals(3, playerListDto.getAvailableUsersCount());
		availableUsers = playerListDto.getAvailableUsers();
		Collections.sort(availableUsers);
		assertEquals(PLAYER_NAMES[0], availableUsers.get(0));
		assertEquals(PLAYER_NAMES[1], availableUsers.get(1));
		assertEquals(PLAYER_NAMES[2], availableUsers.get(2));

		playerListDto = playerListService.getPlayerList(CURRENT_TIME, PLAYER_NAMES[0]);
		availableUsers = playerListDto.getAvailableUsers();
		Collections.sort(availableUsers);
		assertEquals(PLAYER_NAMES[1], availableUsers.get(0));
		assertEquals(PLAYER_NAMES[2], availableUsers.get(1));
		assertEquals(PLAYER_NAMES[3], availableUsers.get(2));
	}

	@Test
	public void getPlayerListRemovesPlayersAfterTimeout()
	{
		//TictactoePlayerListService playerListService = new TictactoePlayerListServiceImpl(new TictactoeGameServiceImpl());
		playerListService.reset();
		gameService.reset();
		playerListService.getPlayerList(CURRENT_TIME, PLAYER_NAMES[0]);
		TictactoePlayerListDto playerListDto;
		List<String> availableUsers;

		// list of players should return no other players for the first to join
		playerListDto = playerListService.getPlayerList(CURRENT_TIME, PLAYER_NAMES[0]);
		playerListDto = playerListService.getPlayerList(CURRENT_TIME, PLAYER_NAMES[1]);
		assertEquals(1, playerListDto.getAvailableUsersCount());
		availableUsers = playerListDto.getAvailableUsers();
		assertEquals(PLAYER_NAMES[0], availableUsers.get(0));

		// check in again after the other player's checkin expired (remove out of date player information)
		playerListDto = playerListService.getPlayerList(CURRENT_TIME+EXPIRE_TIMEOUT, PLAYER_NAMES[0]);
		assertEquals(0, playerListDto.getAvailableUsersCount());

		// check in players 0/1/2 and expire players 0/1
		playerListDto = playerListService.getPlayerList(CURRENT_TIME, PLAYER_NAMES[0]);
		playerListDto = playerListService.getPlayerList(CURRENT_TIME, PLAYER_NAMES[1]);
		playerListDto = playerListService.getPlayerList(CURRENT_TIME, PLAYER_NAMES[3]);
		assertEquals(2, playerListDto.getAvailableUsersCount());
		playerListDto = playerListService.getPlayerList(CURRENT_TIME+EXPIRE_TIMEOUT, PLAYER_NAMES[2]);
		playerListDto = playerListService.getPlayerList(CURRENT_TIME+EXPIRE_TIMEOUT, PLAYER_NAMES[3]);
		
		// expect player 3 to only see player 2 and vice versa
		availableUsers = playerListDto.getAvailableUsers();
		Collections.sort(availableUsers);
		//printPlayers(availableUsers);
		assertEquals(1, playerListDto.getAvailableUsersCount());
		assertEquals(PLAYER_NAMES[2], availableUsers.get(0));
		playerListDto = playerListService.getPlayerList(CURRENT_TIME+EXPIRE_TIMEOUT, PLAYER_NAMES[2]);
		availableUsers = playerListDto.getAvailableUsers();
		Collections.sort(availableUsers);
		assertEquals(1, playerListDto.getAvailableUsersCount());
		assertEquals(PLAYER_NAMES[3], availableUsers.get(0));
	}

	@Test
	public void getGameStateReturnsNewMatchWhenTwoPlayersRequestToPlayTheCompetitiveTictactoeGame()
	{
		//TictactoePlayerListService playerListService = new TictactoePlayerListServiceImpl(new TictactoeGameServiceImpl());
		TictactoePlayerListDto playerListDto;
		List<String> availableUsers;
		TictactoeRequestDto response; 
		playerListService.reset();
		gameService.reset();

		// both players are in the player list
		playerListDto = playerListService.getPlayerList(CURRENT_TIME, PLAYER_NAMES[0]);
		playerListDto = playerListService.getPlayerList(CURRENT_TIME, PLAYER_NAMES[1]);

		// player0 requests a match against player1
		response = playerListService.addPlayerRequest(CURRENT_TIME, PLAYER_NAMES[0], PLAYER_NAMES[1]);
		assertEquals(TictactoeRequestDto.ResponseType.SUCCESS, response.getResponseType());

		// player1 requests a match against player0 and service responds with START_GAME
		response = playerListService.addPlayerRequest(CURRENT_TIME, PLAYER_NAMES[1], PLAYER_NAMES[0]);
		assertEquals(TictactoeRequestDto.ResponseType.START_GAME, response.getResponseType());

		// add another player to check playerlist
		playerListDto = playerListService.getPlayerList(CURRENT_TIME+EXPIRE_TIMEOUT, PLAYER_NAMES[2]);

		// getPlayerList should not show player0/player1
		assertEquals(0, playerListDto.getAvailableUsersCount());
	}

	@Test
	public void playersCantJoinMultipleGames()
	{
		//TictactoePlayerListService playerListService = new TictactoePlayerListServiceImpl(new TictactoeGameServiceImpl());
		TictactoePlayerListDto playerListDto;
		List<String> availableUsers;
		TictactoeRequestDto response; 
		playerListService.reset();
		gameService.reset();

		// both players are in the player list
		playerListDto = playerListService.getPlayerList(CURRENT_TIME, PLAYER_NAMES[0]);
		playerListDto = playerListService.getPlayerList(CURRENT_TIME, PLAYER_NAMES[1]);

		// player0 requests a match against player1
		response = playerListService.addPlayerRequest(CURRENT_TIME, PLAYER_NAMES[0], PLAYER_NAMES[1]);
		response = playerListService.addPlayerRequest(CURRENT_TIME, PLAYER_NAMES[1], PLAYER_NAMES[0]);
		assertEquals(TictactoeRequestDto.ResponseType.START_GAME, response.getResponseType());

		// add another player to check playerlist
		playerListDto = playerListService.getPlayerList(CURRENT_TIME, PLAYER_NAMES[2]);

		// getPlayerList should not list players already in game
		assertEquals(0, playerListDto.getAvailableUsersCount());
		assertEquals(0, playerListDto.getRequestedUsersCount());
		assertEquals(0, playerListDto.getRequestingUsersCount());

		// request should be rejected if players are already in game
		response = playerListService.addPlayerRequest(CURRENT_TIME, PLAYER_NAMES[2], PLAYER_NAMES[0]);
		assertEquals(TictactoeRequestDto.ResponseType.ERROR_VERSUS_PLAYER_IS_IN_GAME, response.getResponseType());

		response = playerListService.addPlayerRequest(CURRENT_TIME, PLAYER_NAMES[0], PLAYER_NAMES[2]);
		assertEquals(TictactoeRequestDto.ResponseType.ERROR_CURRENT_PLAYER_IS_IN_GAME, response.getResponseType());

		response = playerListService.addPlayerRequest(CURRENT_TIME, PLAYER_NAMES[2], PLAYER_NAMES[1]);
		assertEquals(TictactoeRequestDto.ResponseType.ERROR_VERSUS_PLAYER_IS_IN_GAME, response.getResponseType());
	}

	@Test
	public void playersCanRejoinGames()
	{
		//TictactoePlayerListService playerListService = new TictactoePlayerListServiceImpl(new TictactoeGameServiceImpl());
		TictactoeRequestDto response; 
		TictactoePlayerListDto playerListDto;
		List<String> availableUsers;
		playerListService.reset();
		gameService.reset();

		// both players are in the player list
		playerListDto = playerListService.getPlayerList(CURRENT_TIME, PLAYER_NAMES[0]);
		playerListDto = playerListService.getPlayerList(CURRENT_TIME, PLAYER_NAMES[1]);

		// player0 requests a match against player1
		response = playerListService.addPlayerRequest(CURRENT_TIME, PLAYER_NAMES[0], PLAYER_NAMES[1]);
		response = playerListService.addPlayerRequest(CURRENT_TIME, PLAYER_NAMES[1], PLAYER_NAMES[0]);
		assertEquals(TictactoeRequestDto.ResponseType.START_GAME, response.getResponseType());

		// service responds with player already in game if trying to get player list
		playerListDto = playerListService.getPlayerList(CURRENT_TIME, PLAYER_NAMES[0]);
		assertEquals(TictactoePlayerListDto.ServiceResponse.PLAYER_IN_GAME, playerListDto.getServiceResponse());
		playerListDto = playerListService.getPlayerList(CURRENT_TIME, PLAYER_NAMES[1]);
		assertEquals(TictactoePlayerListDto.ServiceResponse.PLAYER_IN_GAME, playerListDto.getServiceResponse());
	}
}



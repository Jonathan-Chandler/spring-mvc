package com.jonathan.web.service;

import java.util.List;
import java.util.ArrayList;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import com.jonathan.web.resources.TictactoePlayerListDto;
import com.jonathan.web.resources.OnlineUserDto;
import com.jonathan.web.service.TictactoeService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import com.jonathan.web.resources.TictactoePlayer;
import com.jonathan.web.resources.TictactoeGame;
import org.springframework.lang.NonNull;
import java.util.Collections;
//import java.util.Collections.synchronizedMap;
import java.util.HashMap;


@Service
public class TictactoeGameServiceImpl implements TictactoeGameService
{
	final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RabbitTemplate template;

	// id of next created game
	private static long gameIdCounter = 1;

	// list of games
	private Map<Long, TictactoeGame> gameList;

	public TictactoeGameServiceImpl()
	{
		// synchronized list of games in progress (2 player names mapped to TictactoeGame)
		gameList = Collections.synchronizedMap(new HashMap<Long, TictactoeGame>());
	}

	public GameServiceResponse addGame(long currentTime, String xPlayerName, String oPlayerName)
	{
		return GAME_CREATED;
	}

	public TictactoePlayerListDto getGameList(long currentTime, String thisPlayerName);

	public TictactoeServiceResponse addPlayerRequest(long currentTime, String thisPlayerName, String versusPlayerName)
	{
		// don't add nonexistant players to versus requests
		if (!playerList.containsKey(versusPlayerName))
		{
			return TictactoeServiceResponse.ERROR_VERSUS_PLAYER_DOES_NOT_EXIST;
		}

		// add to list if list does not contain this playername
		if (!playerList.containsKey(thisPlayerName))
		{
			playerList.put(thisPlayerName, new TictactoePlayer(currentTime));
			//return TictactoeServiceResponse.ERROR_CURRENT_PLAYER_DOES_NOT_EXIST;
		}

		// get information for both players
		TictactoePlayer thisPlayer = playerList.get(thisPlayerName);
		TictactoePlayer versusPlayer = playerList.get(versusPlayerName);

		// requesting player must be in lobby
		TictactoePlayer.PlayerState thisPlayerState = thisPlayer.getState();
		if (thisPlayerState != TictactoePlayer.PlayerState.IN_LOBBY)
		{
			logger.error("Player " + thisPlayerName + " not in lobby: " + thisPlayerState);
			return TictactoeServiceResponse.ERROR_CURRENT_PLAYER_IS_IN_GAME;
		}

		// versusPlayer must be in lobby
		TictactoePlayer.PlayerState versusPlayerState = versusPlayer.getState();
		if (versusPlayerState != TictactoePlayer.PlayerState.IN_LOBBY)
		{
			logger.error("Player " + versusPlayerName + " not in lobby: " + versusPlayerState);
			return TictactoeServiceResponse.ERROR_VERSUS_PLAYER_IS_IN_GAME;
		}

		// update time of last received message for requesting player
		thisPlayer.setCheckinTime(currentTime);

		// other player has already requested and both should join a game
		if (versusPlayer.hasRequestedUser(thisPlayerName))
		{
			// add new game id to hashmap and both players' state
			long newGameId = createGameId();
			gameList.put(newGameId, new TictactoeGame(currentTime, thisPlayerName, versusPlayerName));

			// update hash for both players with ID of new game and reset timer
			thisPlayer.joinGame(currentTime, newGameId);
			versusPlayer.joinGame(currentTime, newGameId);
			playerList.put(thisPlayerName, thisPlayer);
			playerList.put(versusPlayerName, versusPlayer);

			// return response indicating that caller needs to start the game
			return TictactoeServiceResponse.START_GAME;
		}
		else
		{
			// add to the list of thisPlayer's match requests
			if (TictactoePlayer.PlayerResponse.FAILED_PLAYER_REQUEST_EXISTS == thisPlayer.addRequestedUser(versusPlayerName))
			{
				// let caller know that the user already requested to play against versusPlayerName
				return TictactoeServiceResponse.ERROR_REQUEST_EXISTS;
			}

			// successfully added to player match requests
			return TictactoeServiceResponse.SUCCESS;
		}
	}

	private TictactoeGame getTictactoeGameByPlayerName(long currentTime, @NonNull String thisPlayerName)
	{
		TictactoePlayer thisPlayer;
		TictactoeGame currentGame;
		
		// player did not exist or has already timed out from previous game
		if (!playerList.containsKey(thisPlayerName))
		{
			// return game in error state
			currentGame = new TictactoeGame(currentTime);
			return currentGame.getGameState(currentTime);
		}
		else
		{
			// get the player from list and game ID
			thisPlayer = playerList.get(thisPlayerName);
			thisGameId = thisPlayer.getGameId();
		}

		// game ID was not in list
		if (!gameList.containsKey(thisGameId))
		{
			// return game in error state
			currentGame = new TictactoeGame(currentTime);
			return currentGame.getGameState(currentTime);
		}
		else
		{
			// get tictactoe game
			currentGame = gameList.get(thisGameId);
		}

		return currentGame;
	}

	public TictactoeGame.GameState getGameState(long currentTime, @NonNull String thisPlayerName)
	{
		TictactoeGame currentGame = getTictactoeGameByPlayerName(currentTime, thisPlayerName);

		// player is ready if getting game state
		return currentGame.setPlayerReadyByName(currentTime, thisPlayerName);
	}

	public TictactoeGame.GameState sendGameMove(long currentTime, @NonNull String thisPlayerName)
	{
		TictactoeGame currentGame = getTictactoeGameByPlayerName(currentTime, thisPlayerName)
		if (currentGame.handlePlayerMove(currentTime, symbol, int location))
		{
		}
	}

	//public TictactoeGame.GameState sendGameRequest(String thisPlayerName)
	//{
	//}

	public static synchronized long createGameId()
	{
		return gameIdCounter++;
	}
}


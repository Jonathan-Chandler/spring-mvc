package com.jonathan.web.service;

import java.util.List;
import java.util.ArrayList;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import com.jonathan.web.resources.TictactoePlayerListDto;
import com.jonathan.web.resources.OnlineUserDto;
import com.jonathan.web.service.TictactoePlayerListService;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Primary;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
import com.jonathan.web.resources.TictactoePlayer;
import com.jonathan.web.resources.TictactoeGame;
import com.jonathan.web.resources.TictactoeRequestDto;
import org.springframework.lang.NonNull;
import java.util.Collections;
//import java.util.Collections.synchronizedMap;
import java.util.HashMap;
import java.util.Set;


@Service
@Primary
public class TictactoePlayerListServiceImpl implements TictactoePlayerListService
{
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	// time in ms between updating player list (every 5 seconds)
	public static final long PLAYER_LIST_UPDATE_INTERVAL = 5000;

	// time of last update request
	private static long lastUpdateTime;

	// list of players
	private static Map<String, TictactoePlayer> playerList;

    //@Autowired
    //private RabbitTemplate template;

    @Autowired
    private TictactoeGameService gameService;
    //private TictactoeGameServiceImpl gameService;

	@Autowired
	//public TictactoePlayerListServiceImpl(TictactoeGameService tictactoeGameService)
	public TictactoePlayerListServiceImpl()
	{
		//gameService = tictactoeGameService;

		// time that service was started
		lastUpdateTime = -1;

		// synchronized list of players
		playerList = Collections.synchronizedMap(new HashMap<String, TictactoePlayer>());
	}

	private void refreshPlayerList(long currentTime)
	{
		// players already playing the game that should stay on active list
		List<String> playersInGame = gameService.getPlayersInActiveGames(currentTime);

		// games requests sent by this player
		List<String> deletedPlayers = new ArrayList<String>();

		// don't remove names on first update
		if (lastUpdateTime < 0)
		{
			lastUpdateTime = currentTime;
			return;
		}

		// player status should be checked every 5 seconds
		if ((currentTime - lastUpdateTime) >= PLAYER_LIST_UPDATE_INTERVAL)
		{
			lastUpdateTime = currentTime;

			// check list for players that have not checked in
			for ( String key : playerList.keySet() ) 
			{
				TictactoePlayer player = playerList.get(key);

				// remove the player from active list
				if (!player.isActive(currentTime))
				{
					deletedPlayers.add(key);
				}
			}

			// remove out of date players
			for ( String key : deletedPlayers ) 
			{
				// don't remove players if they are in an active game
				if (!playersInGame.contains(key))
				{
					playerList.remove(key);
				}
			}
		}
	}

	// used by sender to get information for each player
	public List<String> getAllPlayers(long currentTime)
	{
		refreshPlayerList(currentTime);

		return new ArrayList<>(playerList.keySet());
	}

	public TictactoePlayerListDto getPlayerList(long currentTime, @NonNull String thisPlayerName)
	{
		TictactoePlayer thisPlayer;

		// updated list of players not including this player
		List<String> currentPlayerList = new ArrayList<String>();

		// games requests sent by this player
		List<String> currentPlayerRequests = new ArrayList<String>();

		// games requested against this player
		List<String> currentPlayerRequested = new ArrayList<String>();

		// players already playing the game that shouldn't be displayed
		List<String> playersInGame = gameService.getPlayersInActiveGames(currentTime);

		// player should rejoin their game in progress if they are on list of players in game
		if (playersInGame.contains(thisPlayerName))
		{
			TictactoePlayerListDto playerListDto = 
				new TictactoePlayerListDto(TictactoePlayerListDto.ServiceResponse.PLAYER_IN_GAME);
			return playerListDto;
		}

		// add to list if list does not contain this player's name
		if (!playerList.containsKey(thisPlayerName))
		{
			//logger.error("Add player " + thisPlayerName + " time: " + currentTime);
			playerList.put(thisPlayerName, new TictactoePlayer(currentTime));
		}

		// get player information
		thisPlayer = playerList.get(thisPlayerName);

		// update last checkin time for user with name matching thisPlayerName
		thisPlayer.setCheckinTime(currentTime);

		// get list of players that this user already requested to play against
		currentPlayerRequests = thisPlayer.getRequestedUsers();

		// update active players
		refreshPlayerList(currentTime);

		// get list of all players / players requested / players requesting a game
		for ( String key : playerList.keySet() ) 
		{
			TictactoePlayer currentPlayer = playerList.get(key);

			// skip inactive players
			if (currentPlayer.isActive(currentTime))
			{
				// only add to player list if user name isn't this player
				if (!key.equals(thisPlayerName))
				{
					// only add player to list if they aren't in a game already
					if (!playersInGame.contains(key))
					{
						// add this player to list of all players
						currentPlayerList.add(key);

						// add to list if this player requested a game against thisPlayerName
						if (currentPlayer.hasRequestedUser(thisPlayerName))
						{
							currentPlayerRequested.add(key);
						}
					}
				}
			}
		}

		// return 3 lists: all players, this user's requests, other user requested games
		TictactoePlayerListDto playerListDto = new TictactoePlayerListDto(currentPlayerList, currentPlayerRequests, currentPlayerRequested);

		return playerListDto;
	}

	public TictactoeRequestDto addPlayerRequest(long currentTime, @NonNull String thisPlayerName, @NonNull String versusPlayerName)
	{
		List<String> playersInGame;

		// don't add nonexistant players to versus requests
		if (!playerList.containsKey(versusPlayerName))
		{
			return new TictactoeRequestDto(TictactoeRequestDto.ResponseType.ERROR_VERSUS_PLAYER_DOES_NOT_EXIST, thisPlayerName, versusPlayerName);
		}

		// add to list if list does not contain this playername
		if (!playerList.containsKey(thisPlayerName))
		{
			playerList.put(thisPlayerName, new TictactoePlayer(currentTime));
			//return TictactoePlayerListService.ERROR_CURRENT_PLAYER_DOES_NOT_EXIST;
		}

		// update time of last received message for requesting player
		TictactoePlayer thisPlayer = playerList.get(thisPlayerName);
		thisPlayer.setCheckinTime(currentTime);

		// update player list adding request
		refreshPlayerList(currentTime);

		// get list of players already in game
		playersInGame = gameService.getPlayersInActiveGames(currentTime);

		// requesting player must be in lobby
		if (playersInGame.contains(thisPlayerName))
		{
			long gameId = gameService.getPlayerGameId(currentTime, thisPlayerName);
			logger.error("Player " + thisPlayerName + " is already in a game ID " + gameId);
			return new TictactoeRequestDto(TictactoeRequestDto.ResponseType.ERROR_CURRENT_PLAYER_IS_IN_GAME, gameId, thisPlayerName, versusPlayerName);
		}

		// versusPlayer must be in lobby
		if (playersInGame.contains(versusPlayerName))
		{
			//long gameId = getPlayerGameId(currentTime, versusPlayerName);
			//logger.error("Player " + versusPlayerName + " is already in game ID " + gameId);
			logger.error("Player " + versusPlayerName + " is already in game");
			return new TictactoeRequestDto(TictactoeRequestDto.ResponseType.ERROR_VERSUS_PLAYER_IS_IN_GAME, thisPlayerName, versusPlayerName);
			//return new TictactoeRequestDto(TictactoeRequestDto.ResponseType.ERROR_UNKNOWN);
		}

		// other player has already requested and both should join a game
		TictactoePlayer versusPlayer = playerList.get(versusPlayerName);
		if (versusPlayer.hasRequestedUser(thisPlayerName))
		{
			// add new game id to GameService
			long newGameId = gameService.createNewGame(currentTime, thisPlayerName, versusPlayerName);

			// update hash for both players with ID of new game and reset timer
			thisPlayer.joinGame(currentTime, newGameId);
			versusPlayer.joinGame(currentTime, newGameId);

			// return response that both players should join game with given id
			return new TictactoeRequestDto(TictactoeRequestDto.ResponseType.START_GAME, newGameId, thisPlayerName, versusPlayerName);
			//return new TictactoeRequestDto(TictactoeRequestDto.ResponseType.ERROR_UNKNOWN);
		}
		else
		{
			// add to the list of thisPlayer's match requests
			if (TictactoePlayer.PlayerResponse.FAILED_PLAYER_REQUEST_EXISTS == thisPlayer.addRequestedUser(versusPlayerName))
			{
				// let caller know that the user already requested to play against versusPlayerName
				return new TictactoeRequestDto(TictactoeRequestDto.ResponseType.ERROR_REQUEST_EXISTS, thisPlayerName, versusPlayerName);
			}

			// successfully added to player match requests
			return new TictactoeRequestDto(TictactoeRequestDto.ResponseType.SUCCESS, thisPlayerName, versusPlayerName);
		}
	}

	public void reset()
	{
		// time that the game list was last updated
		lastUpdateTime = -1;

		synchronized(playerList)
		{
			playerList.clear();
		}

		logger.error("Cleared active players: " + playerList.keySet());
	}

	private void debugPrintList(String listName, List<String> printList)
	{
		int index = 0;
		for ( String key : printList ) 
		{
			logger.error(listName + "[" + index + "]: " + key);
			index += 1;
		}
	}
}


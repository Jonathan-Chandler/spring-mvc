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
public class TictactoeServiceImpl implements TictactoeService
{
	final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RabbitTemplate template;

	// list of players
	private Map<String, TictactoePlayer> playerList;

	// id of next created game
	private static long gameIdCounter = 1;

	// list of games
	private Map<Long, TictactoeGame> gameList;

	public TictactoeServiceImpl()
	{
		// synchronized list of players
		playerList = Collections.synchronizedMap(new HashMap<String, TictactoePlayer>());

		// synchronized list of games in progress (2 player names mapped to TictactoeGame)
		gameList = Collections.synchronizedMap(new HashMap<Long, TictactoeGame>());
	}

	public TictactoePlayerListDto getPlayerList(long currentTime, String thisPlayerName)
	{
		// expired player names scheduled to be deleted
		List<String> deletedPlayers = new ArrayList<String>();

		// updated list of players not including this player
		List<String> currentPlayerList = new ArrayList<String>();

		// games requests sent by this player
		List<String> currentPlayerRequests = new ArrayList<String>();

		// games requested against this player
		List<String> currentPlayerRequested = new ArrayList<String>();

		// add to list if list does not contain this player's name
		if (!playerList.containsKey(thisPlayerName))
		{
			playerList.put(thisPlayerName, new TictactoePlayer(currentTime));
		}

		// get list of all players / players requested / requesting a game
		for ( String key : playerList.keySet() ) 
		{
			TictactoePlayer currentPlayer = playerList.get(key);
			if (!currentPlayer.isActive(currentTime))
			{
				// remove players if they have not checked in within the last 15 seconds (TictactoePlayer.PLAYER_TIMEOUT_MS)
				deletedPlayers.add(key);
			}
			else
			{
				// only add to player list if user name isn't this player
				if (!key.equals(thisPlayerName))
				{
					// only add player to list if they aren't in a game already
					if (currentPlayer.getState() == TictactoePlayer.PlayerState.IN_LOBBY)
					{
						currentPlayerList.add(key);

						// add to list if this player requested a game against thisPlayerName
						if (currentPlayer.hasRequestedUser(thisPlayerName))
						{
							currentPlayerRequested.add(key);
						}
					}
				}
				else
				{
					// update last checkin time for user with name matching thisPlayerName
					currentPlayer.setCheckinTime(currentTime);
					currentPlayerRequests = currentPlayer.getRequestedUsers();
				}
			}
		}

		// delete timed out players from playerList map
		for ( String deletedPlayer : deletedPlayers ) 
		{
			playerList.remove(deletedPlayer);
		}

		// return 3 lists: all players, this user's requests, other user requested games
		TictactoePlayerListDto playerListDto = new TictactoePlayerListDto(currentPlayerList, currentPlayerRequests, currentPlayerRequested);

		return playerListDto;
	}

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

	public TictactoeGame.GameState getGameState(String thisPlayerName)
	{
		long currentTime = System.currentTimeMillis();
		TictactoeGame currentGame;
		TictactoePlayer thisPlayer;
		long thisGameId;

		if (!playerList.containsKey(thisPlayerName))
		{
			// player is joining a game now
			currentGame = new TictactoeGame(currentTime);
			return currentGame.getGameState(currentTime);
		}
		else
		{
			// player is already in game
			thisPlayer = playerList.get(thisPlayerName);
			thisGameId = thisPlayer.getGameId();
		}

		if (!gameList.containsKey(thisGameId))
		{
			// return error state game if not in the list
			currentGame = new TictactoeGame(currentTime);
		}
		else
		{
			// return tictactoe game
			currentGame = gameList.get(thisGameId);
		}

		return currentGame.getGameState(currentTime);
	}

	public static synchronized long createGameId()
	{
		return gameIdCounter++;
	}

	//public TictactoePlayerListDto getPlayerList()
	//{
	//	long timeNow = System.currentTimeMillis();

	//	// drop players after 30 seconds with no response
	//	long checkinTimeout = 30*1000;
	//	ArrayList<String> activePlayers = new ArrayList<String>();

	//	ArrayList<String> deletedPlayers = new ArrayList<String>();
	//	for ( String key : onlinePlayerList.keySet() ) 
	//	{
	//		long playerCheckinTime = onlinePlayerList.get(key);

	//		if ((timeNow - playerCheckinTime > checkinTimeout))
	//		{
	//			logger.error("Remove player from list: " + key);
	//			deletedPlayers.add(key);
	//		}
	//		else
	//			activePlayers.add(key);
	//	}

	//	for ( String deletedPlayer : deletedPlayers ) 
	//	{
	//		onlinePlayerList.remove(deletedPlayer);
	//	}

	//	logger.error("player list: " + activePlayers);
	//	return (new TictactoePlayerListDto(activePlayers.toArray(new String[0])));
	//}

	//public void userCheckIn(String playerName)
	//{
	//	long lastCheckin = System.currentTimeMillis();
    //    logger.error("checked in player: " + playerName + " at time: " + lastCheckin);

	//	// check if player is already checked in
	//	onlinePlayerList.put(playerName, lastCheckin);

	//	if (playerList.get(playerName) == null)
	//	{
	//		playerList.put(playerName, new TictactoePlayer(lastCheckin));
	//		logger.error("new player: " + playerName);
	//	}
	//}

	//public void userRequestMatch(@NonNull String requestFrom, @NonNull String requestTo)
	//{
	//	logger.error("requestFrom: " + requestFrom);
	//	logger.error("requestTo: " + requestTo);

	//	if (requestFrom.equals("") || requestTo.equals("") || requestFrom.equals(requestTo))
	//	{
	//		logger.error("Bad request");
	//		return;
	//	}

	//	TictactoePlayer requestToPlayer = playerList.get(requestTo);
	//	TictactoePlayer requestFromPlayer = playerList.get(requestFrom);

	//	// player is not in list
	//	if (playerList.get(requestTo) == null || playerList.get(requestFrom) == null)
	//	{
	//		logger.error("could not find player");
	//		return;
	//	}

	//	// player is already in a game
	//	if (requestToPlayer.isInLobby() || requestFromPlayer.isInLobby())
	//	{
	//		logger.error("Player already in game");
	//		return;
	//	}

	//	// if other user requested a match against this player
	//	if (requestToPlayer.hasRequestedUser(requestFrom))
	//	{
	//		String topic = "amq.topic";
	//		String requestToPlayerKey = "to.user." + requestTo;
	//		String requestFromPlayerKey = "to.user." + requestFrom;
	//		String message = "join game with " + requestToPlayer + " and " + requestFromPlayer;

	//		// clear request list for both users
	//		requestToPlayer.clearRequests();
	//		requestFromPlayer.clearRequests();

	//		// send both users to game
	//		template.convertAndSend(topic, requestToPlayerKey, message);
	//		template.convertAndSend(topic, requestFromPlayerKey, message);
	//	}
	//	else
	//	{
	//		// no match, add to request list
	//		requestFromPlayer.addRequestedUser(requestTo);
	//	}
	//}

	//public void userRequestMatch(String requestFrom, String requestTo)
	//{
	//	TictactoePlayer requestToPlayer = playerList.get(requestTo);
	//	TictactoePlayer requestFromPlayer = playerList.get(requestFrom);

	//	if (playerList.get(requestTo) == null || playerList.get(requestFrom) == null)
	//	{
	//		logger.error("could not find player");
	//		return;
	//	}

	//	// start a new game
	//	if (requestToPlayer.hasRequestedUser(requestFrom))
	//	{
	//		String topic = "amq.topic";
	//		String requestToPlayerKey = "to.user." + requestTo;
	//		String requestFromPlayerKey = "to.user." + requestFrom;
	//		String message = "join game with " + requestToPlayer + " and " + requestFromPlayer;
	//		template.convertAndSend(topic, requestToPlayerKey, message);
	//		template.convertAndSend(topic, requestFromPlayerKey, message);
	//	}
	//	else
	//	{
	//		requestFromPlayer.addRequestedUser(requestTo);
	//	}
	//}
}


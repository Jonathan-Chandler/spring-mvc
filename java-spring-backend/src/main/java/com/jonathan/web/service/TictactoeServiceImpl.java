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

	public List<String> getPlayerList(String thisPlayerName)
	{
		long currentTime = System.currentTimeMillis();
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
				deletedPlayers.add(key);
			}
			else
			{
				if (!key.equals(thisPlayerName))
				{
					// only add to player list if user name isn't this player
					currentPlayerList.add(key);

					// add to list if this player requested a game against thisPlayerName
					if (currentPlayer.hasRequestedUser(thisPlayerName))
					{
						currentPlayerRequested.add(key);
					}
				}
				else
				{
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

		return currentPlayerList;
	}

	public void addPlayerRequest(String thisPlayerName, String versusPlayerName)
	{
		long currentTime = System.currentTimeMillis();

		// don't add nonexistant users
		if (!playerList.containsKey(versusPlayerName))
		{
			return;
		}

		// add to list if list does not contain this playername
		if (!playerList.containsKey(thisPlayerName))
		{
			playerList.put(thisPlayerName, new TictactoePlayer(currentTime));
		}

		// get information for both players
		TictactoePlayer thisPlayer = playerList.get(thisPlayerName);
		TictactoePlayer versusPlayer = playerList.get(versusPlayerName);

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
		}
		else
		{
			// add to list of thisPlayer's requests and return if other user did not request
			thisPlayer.addRequestedUser(versusPlayerName);
		}
	}

	public ArrayList<String> getGameState(String playerName)
	{
		long currentTime = System.currentTimeMillis();
		ArrayList<String> deletedPlayers = new ArrayList<String>();
		ArrayList<String> currentPlayerList = new ArrayList<String>();

		for ( String key : playerList.keySet() ) 
		{
			TictactoePlayer currentPlayer = playerList.get(key);
			if (!currentPlayer.isActive(currentTime))
			{
				deletedPlayers.add(key);
			}
			else
			{
				currentPlayerList.add(key);
			}
		}

		// delete timed out players from playerList map
		for ( String deletedPlayer : deletedPlayers ) 
		{
			playerList.remove(deletedPlayer);
		}

		return currentPlayerList;
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


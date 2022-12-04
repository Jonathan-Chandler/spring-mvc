package com.jonathan.web.service;

import java.util.List;
import java.util.ArrayList;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import com.jonathan.web.entities.TictactoeGame;
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
import org.springframework.lang.NonNull;


@Service
public class TestTictactoeService
{
//	final Logger logger = LoggerFactory.getLogger(this.getClass());
//
//	//@Autowired
//	//Logger logger;
//
//    @Autowired
//    private RabbitTemplate template;
//
//	@Autowired
//	private UserService userService;
//
//	private static long gameIdCounter = 1;
//
//	public TictactoeServiceImpl()
//	{
//		onlinePlayerList = new ConcurrentHashMap<String, Long>();
//		playerList = new ConcurrentHashMap<String, TictactoePlayer>();
//	}
//
//	ConcurrentMap<String, Long> onlinePlayerList;
//	ConcurrentMap<String, TictactoePlayer> playerList;
//
//	//public List<TictactoePlayerListDto> getPlayerList()
//	public TictactoePlayerListDto getPlayerList()
//	{
//		//List<OnlineUserDto> onlineUsers = userService.getOnlineUsers();
//		//List<TictactoePlayerListDto> tttPlayerList = new ArrayList<TictactoePlayerListDto>();
//		long timeNow = System.currentTimeMillis();
//
//		// drop players after 30 seconds with no response
//		long checkinTimeout = 30*1000;
//		ArrayList<String> activePlayers = new ArrayList<String>();
//
//		//playerList.removeIf(entry -> (entry.getValue() - timeNow > checkinTimeout));
//
//		ArrayList<String> deletedPlayers = new ArrayList<String>();
//		for ( String key : onlinePlayerList.keySet() ) 
//		{
//			long playerCheckinTime = onlinePlayerList.get(key);
//
//			if ((timeNow - playerCheckinTime > checkinTimeout))
//			{
//				logger.error("Remove player from list: " + key);
//				deletedPlayers.add(key);
//			}
//			else
//				activePlayers.add(key);
//		}
//
//		for ( String deletedPlayer : deletedPlayers ) 
//		{
//			onlinePlayerList.remove(deletedPlayer);
//		}
//
//		logger.error("player list: " + activePlayers);
//		return (new TictactoePlayerListDto(activePlayers.toArray(new String[0])));
//	}
//
//	public void userCheckIn(String playerName)
//	{
//		long lastCheckin = System.currentTimeMillis();
//        logger.error("checked in player: " + playerName + " at time: " + lastCheckin);
//
//		onlinePlayerList.put(playerName, lastCheckin);
//
//		if (playerList.get(playerName) == null)
//		{
//			playerList.put(playerName, new TictactoePlayer(lastCheckin));
//			logger.error("new player: " + playerName);
//		}
//	}
//
//	public void userRequestMatch(@NonNull String requestFrom, @NonNull String requestTo)
//	{
//		logger.error("requestFrom: " + requestFrom);
//		logger.error("requestTo: " + requestTo);
//
//		if (requestFrom.equals("") || requestTo.equals("") || requestFrom.equals(requestTo))
//		{
//			logger.error("Bad request");
//			return;
//		}
//
//		TictactoePlayer requestToPlayer = playerList.get(requestTo);
//		TictactoePlayer requestFromPlayer = playerList.get(requestFrom);
//
//		// player is not in list
//		if (playerList.get(requestTo) == null || playerList.get(requestFrom) == null)
//		{
//			logger.error("could not find player");
//			return;
//		}
//
//		// player is already in a game
//		if (requestToPlayer.isInLobby() || requestFromPlayer.isInLobby())
//		{
//			logger.error("Player already in game");
//			return;
//		}
//
//		// if other user requested a match against this player
//		if (requestToPlayer.hasRequestedUser(requestFrom))
//		{
//			String topic = "amq.topic";
//			String requestToPlayerKey = "to.user." + requestTo;
//			String requestFromPlayerKey = "to.user." + requestFrom;
//			String message = "join game with " + requestToPlayer + " and " + requestFromPlayer;
//
//			// clear request list for both users
//			requestToPlayer.clearRequests();
//			requestFromPlayer.clearRequests();
//
//			// send both users to game
//			template.convertAndSend(topic, requestToPlayerKey, message);
//			template.convertAndSend(topic, requestFromPlayerKey, message);
//		}
//		else
//		{
//			// no match, add to request list
//			requestFromPlayer.addRequestedUser(requestTo);
//		}
//	}
//
//	public void createNewMatch(String player1, String oPlayer)
//	{
//
//		public static synchronized String createID()
//		{
//			return String.valueOf(idCounter++);
//		}
//	}
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



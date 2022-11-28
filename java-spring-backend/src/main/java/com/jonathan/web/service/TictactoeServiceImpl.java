package com.jonathan.web.service;

import java.util.List;
import java.util.ArrayList;

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

@Service
public class TictactoeServiceImpl implements TictactoeService
{
	@Autowired
	Logger logger;

	@Autowired
	private UserService userService;

	public TictactoeServiceImpl()
	{
		playerList = new ConcurrentHashMap<String, Long>();
	}

	//Map<Integer, Object> table = new ConcurrentHashMap<>();

	//Map<Integer, String> map = new ConcurrentHashMap<>();
	//Map m = Collections.synchronizedMap(new HashMap(...));

	ConcurrentMap<String, Long> playerList;

	//public List<TictactoePlayerListDto> getPlayerList()
	public TictactoePlayerListDto getPlayerList()
	{
		//List<OnlineUserDto> onlineUsers = userService.getOnlineUsers();
		//List<TictactoePlayerListDto> tttPlayerList = new ArrayList<TictactoePlayerListDto>();
		long timeNow = System.currentTimeMillis();

		// drop players after 30 seconds with no response
		long checkinTimeout = 30*1000;
		ArrayList<String> activePlayers = new ArrayList<String>();

		//playerList.removeIf(entry -> (entry.getValue() - timeNow > checkinTimeout));

		ArrayList<String> deletedPlayers = new ArrayList<String>();
		for ( String key : playerList.keySet() ) 
		{
			long playerCheckinTime = playerList.get(key);

			if ((timeNow - playerCheckinTime > checkinTimeout))
			{
				logger.error("Remove player from list: " + key);
				deletedPlayers.add(key);
			}
			else
				activePlayers.add(key);
		}

		for ( String deletedPlayer : deletedPlayers ) 
		{
			playerList.remove(deletedPlayer);
		}

		logger.error("player list: " + activePlayers);

		return (new TictactoePlayerListDto(activePlayers.toArray(new String[0])));
	}

	public void userCheckIn(String playerName)
	{
		long lastCheckin = System.currentTimeMillis();
        logger.error("checked in player: " + playerName + " at time: " + lastCheckin);

		playerList.put(playerName, lastCheckin);
	}
}


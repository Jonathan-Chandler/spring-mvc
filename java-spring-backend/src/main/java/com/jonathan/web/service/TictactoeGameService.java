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
public class TictactoeGameService
{
	public enum GameServiceResponse 
	{
		GAME_CREATED,
		PLAYER_IN_GAME,
	}

	// list of games
	private Map<Long, TictactoeGame> gameList;

	public TictactoeServiceResponse addPlayerRequest(long currentTime, String thisPlayerName, String versusPlayerName)

	private TictactoeGame getTictactoeGameByPlayerName(long currentTime, @NonNull String thisPlayerName)

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


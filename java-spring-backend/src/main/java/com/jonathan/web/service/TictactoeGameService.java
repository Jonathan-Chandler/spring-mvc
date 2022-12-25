package com.jonathan.web.service;

import java.util.List;
import java.util.ArrayList;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import com.jonathan.web.resources.TictactoePlayerListDto;
import com.jonathan.web.resources.OnlineUserDto;
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

public interface TictactoeGameService
{
	public enum GameServiceResponse 
	{
		SUCCESS,
		GAME_CREATED,
		PLAYER_IN_GAME,
		INVALID_MOVE,
	}

	// create new game and return ID of created game
	public long createNewGame(long currentTime, String xPlayerName, String oPlayerName);

	// get list of players that are in an active game and remove expired games
	public List<String> getPlayersInActiveGames(long currentTime);

	// get list of players that are in ended and active game and remove expired games
	public List<String> getPlayersInAllGames(long currentTime);

	// get a copy of the tictactoe game
	public TictactoeGame getGameCopyByPlayerName(long currentTime, @NonNull String playerName);

	// player wants to add a move to the board
	public GameServiceResponse sendTictactoeMove(long currentTime, @NonNull String thisPlayerName, int location);

	public long getPlayerGameId(long currentTime, String playerName);

	public void printGameIds();

	public void reset();
}


package com.jonathan.web.service;

import java.util.List;
import java.util.ArrayList;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import com.jonathan.web.resources.TictactoePlayerListDto;
import com.jonathan.web.resources.OnlineUserDto;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Component;
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


//@Service
@Component
public class TictactoeGameServiceImpl implements TictactoeGameService
{
	final Logger logger = LoggerFactory.getLogger(this.getClass());

    //@Autowired
    //private RabbitTemplate template;

	// id of next created game
	private static long gameIdCounter = 1;

	// time in ms between updating game state (every 5 seconds)
	public static final long GAME_UPDATE_INTERVAL = 5000;

	// time that the game list was last updated
	private long lastUpdateTime;

	// list of games that are not in end state
	private Map<Long, TictactoeGame> activeGameList;

	// list of games that ended but not queued to be deleted
	private Map<Long, TictactoeGame> endingGameList;

	public TictactoeGameServiceImpl()
	{
		// set start time
		lastUpdateTime = -1;

		// synchronized list of games in progress (2 player names mapped to TictactoeGame)
		activeGameList = Collections.synchronizedMap(new HashMap<Long, TictactoeGame>());

		// synchronized list of games that have ended
		endingGameList = Collections.synchronizedMap(new HashMap<Long, TictactoeGame>());
	}

	// create new game and return the ID of the game
	public long createNewGame(long currentTime, String xPlayerName, String oPlayerName)
	{
		long newGameId = createGameId();

		activeGameList.put(newGameId, new TictactoeGame(currentTime, xPlayerName, oPlayerName));

		return newGameId;
	}

	// update maps of active and ending games
	private void refreshGameList(long currentTime)
	{
		// games requests sent by this player
		List<Long> endingGames = new ArrayList<Long>();
		List<Long> deletedGames = new ArrayList<Long>();

		// don't remove games on first update
		if (lastUpdateTime < 0)
		{
			lastUpdateTime = currentTime;
			return;
		}

		// game status should be checked every 5 seconds
		if ((currentTime - lastUpdateTime) >= GAME_UPDATE_INTERVAL)
		{
			lastUpdateTime = currentTime;

			// update list of active games to see if ended
			for ( Long key : activeGameList.keySet() ) 
			{
				TictactoeGame game = activeGameList.get(key);

				// remove the game from active list
				if (game.getGameState(currentTime).ordinal() >= TictactoeGame.GameState.GAME_OVER_ERROR.ordinal())
				{
					endingGames.add(key);
				}
			}

			// move the ending games to other list
			for ( Long key : endingGames ) 
			{
				endingGameList.put(key, activeGameList.get(key));
				activeGameList.remove(key);
				logger.error("Remove game ID " + key + " from activeGameList");
			}

			// update list of ending games
			for ( Long key : endingGameList.keySet() ) 
			{
				TictactoeGame game = endingGameList.get(key);

				// remove the game from ending games list
				if (game.getGameState(currentTime) == TictactoeGame.GameState.CLOSING)
				{
					logger.error("Deleting game ID " + key + " from endingGameList");
					deletedGames.add(key);
				}
			}

			// remove expired games
			for ( Long key : deletedGames ) 
			{
				logger.error("Deleted game ID " + key + " from endingGameList");
				endingGames.remove(key);
			}
		}
	}

	// get list of players that are in an active game and remove expired games
	public List<String> getPlayersInGame(long currentTime)
	{
		List<String> playerList = new ArrayList<String>();

		// update state of games before checking active players
		refreshGameList(currentTime);

		// add active players from each tictactoe game
		for ( Long key : activeGameList.keySet() ) 
		{
			TictactoeGame game = activeGameList.get(key);

			// names of players in the game with given ID
			List<String> playersInCurrentGame = game.getPlayerNames();

			// add to list of players in active games
			playerList.addAll(playersInCurrentGame);
		}

		return playerList;
	}

	// get id of game that player is currently in
	public long getPlayerGameId(long currentTime, String playerName)
	{
		// update state of games before checking active players
		refreshGameList(currentTime);

		// all active games
		for ( Long key : activeGameList.keySet() ) 
		{
			TictactoeGame game = activeGameList.get(key);

			// return the id for the game if player is x/o
			if (game.playerIsInThisGame(playerName))
			{
				return key;
			}
		}

		// all ending games
		for ( Long key : endingGameList.keySet() ) 
		{
			TictactoeGame game = endingGameList.get(key);

			// return the id for the game if player is x/o
			if (game.playerIsInThisGame(playerName))
			{
				return key;
			}
		}

		// player was not found in any game
		return -1;
	}

	public TictactoeGame.GameState getGameStateByPlayerName(long currentTime, @NonNull String playerName)
	{
		// get tictactoe game
		TictactoeGame currentGame = getGameByPlayerName(currentTime, playerName);
		
		// return state of game
		return currentGame.getGameState(currentTime);
	}

	public TictactoeGame getGameCopyByPlayerName(long currentTime, @NonNull String playerName)
	{
		// don't return by reference for outside requests
		return new TictactoeGame(getGameByPlayerName(currentTime, playerName));
	}

	private TictactoeGame getGameByPlayerName(long currentTime, @NonNull String playerName)
	{
		long gameId = getPlayerGameId(currentTime, playerName);
		
		// player was not found
		if (gameId < 0)
		{
			// return a new game in error state if not found
			return new TictactoeGame(currentTime);
		}

		// update list
		refreshGameList(currentTime);
		
		// found in active games
		if (activeGameList.containsKey(gameId))
		{
			TictactoeGame activeGame = activeGameList.get(gameId);

			// player has joined game if requesting
			activeGame.setPlayerReadyByName(currentTime, playerName);

			// return copy, not reference to map object
			return activeGame;
		}

		// found in ending games
		if (endingGameList.containsKey(gameId))
		{
			// return copy, not reference to map object
			return endingGameList.get(gameId);
		}

		// game id was not found in any list, return game in error state
		return new TictactoeGame(currentTime);
	}

	// player wants to add a move to the board
	public TictactoeGameService.GameServiceResponse sendTictactoeMove(long currentTime, @NonNull String thisPlayerName, int location)
	{
		TictactoeGame currentGame = getGameByPlayerName(currentTime, thisPlayerName);

		if (currentGame.handlePlayerMoveByPlayername(currentTime, thisPlayerName, location))
		{
			return TictactoeGameService.GameServiceResponse.SUCCESS;
		}

		return TictactoeGameService.GameServiceResponse.INVALID_MOVE;
	}

	public static synchronized long createGameId()
	{
		return gameIdCounter++;
	}

	public void printGameLists()
	{
		logger.error("Active Games: " + activeGameList.keySet());
		logger.error("Ending Games: " + endingGameList.keySet());
	}
}

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
	private static long lastUpdateTime;

	// list of all active and ending games
	private static Map<Long, TictactoeGame> gameList;

	public TictactoeGameServiceImpl()
	{
		// set start time
		lastUpdateTime = -1;

		// list of all active and ending games
		gameList = Collections.synchronizedMap(new HashMap<Long, TictactoeGame>());
	}

	public static synchronized long createGameId()
	{
		return gameIdCounter++;
	}

	// remove games containing player names
	public void removeGamesByPlayerName(String xPlayerName, String oPlayerName)
	{
		// games requests sent by this player
		List<Long> deletedGames = new ArrayList<Long>();

		synchronized(gameList)
		{
			// remove the any games the players are members of
			for ( Long key : gameList.keySet() ) 
			{
				TictactoeGame game = gameList.get(key);

				synchronized(game)
				{
					// remove the game from active list
					if (game.playerIsInThisGame(xPlayerName) || game.playerIsInThisGame(oPlayerName))
					{
						deletedGames.add(key);
					}
				}
			}


			// move the ending games to other list
			for ( Long key : deletedGames ) 
			{
				gameList.remove(key);
				logger.error("Remove game ID " + key + " from gameList");
			}
		}
	}

	// create new game and return the ID of the game (remove players from existing games)
	public long createNewGame(long currentTime, String xPlayerName, String oPlayerName)
	{
		long newGameId = createGameId();

		// wait for list before adding
		synchronized(gameList)
		{
			removeGamesByPlayerName(xPlayerName, oPlayerName);

			gameList.put(newGameId, new TictactoeGame(currentTime, xPlayerName, oPlayerName));
			logger.error("add new game ID " + newGameId + " for " 
					+ xPlayerName + "(xPlayer) and " 
					+ oPlayerName + "(oPlayer)");
		}

		return newGameId;
	}

	// update map of games
	private void refreshGameList(long currentTime)
	{
		// games requests sent by this player
		List<Long> deletedGames = new ArrayList<Long>();

		// don't remove games on first update
		if (lastUpdateTime < 0)
		{
			lastUpdateTime = currentTime;
			return;
		}

		// lock out while gameList is being updated with new states
		synchronized(gameList)
		{
			// game status should be checked every 5 seconds
			if ((currentTime - lastUpdateTime) >= GAME_UPDATE_INTERVAL)
			{
				lastUpdateTime = currentTime;

				// update list of active games to see if closing
				for ( Long key : gameList.keySet() ) 
				{
					TictactoeGame game = gameList.get(key);

					// synchronize game object access
					synchronized(game)
					{
						// remove the game from active list
						if (game.getGameState(currentTime) == TictactoeGame.GameState.CLOSING)
						{
							deletedGames.add(key);
						}
					}
				}
			}

			// move the ending games to other list
			for ( Long key : deletedGames ) 
			{
				gameList.remove(key);
				logger.error("Remove game ID " + key + " from gameList");
			}
		}
	}

	// clear the game map (only used for tests)
	public void reset()
	{
		// time that the game list was last updated
		lastUpdateTime = -1;

		synchronized(gameList)
		{
			gameList.clear();
		}
		logger.error("Cleared active Games: " + gameList.keySet());
	}

	// refresh games (return players / game list / copy of games)
	//
	// for playerList
	//		players in active games
	//
	// getGameState
	//		players in ALL games
	//
	// get game state (synchronized)
	//		check in and refresh
	//		return getGameCopy
	//
	// get game (synchronized)
	//
	// get game copy
	//
	// sendTictactoeMove(long currentTime, @NonNull String thisPlayerName, int location)

	// get list of players that are in an active game and remove expired games
	public List<String> getPlayersInActiveGames(long currentTime)
	{
		List<String> playerList = new ArrayList<String>();

		// lock out while searching for players
		synchronized(gameList)
		{
			// update state of games before checking active players
			refreshGameList(currentTime);

			// add active players from each tictactoe game
			for ( Long key : gameList.keySet() ) 
			{
				TictactoeGame game = gameList.get(key);

				// sync game object
				synchronized(game)
				{
					// gameState should have been updated already by refreshGameList
					TictactoeGame.GameState gameState = game.getGameStateNoUpdate();

					// add players if game is still active
					if (gameState.ordinal() < TictactoeGame.GameState.GAME_OVER_ERROR.ordinal())
					{
						// names of players in the game with given ID
						List<String> playersInCurrentGame = game.getPlayerNames();

						// add to list of players in active games
						playerList.addAll(playersInCurrentGame);
					}
				}
			}
		}

		return playerList;
	}

	// get list of players that are in ended and active game and remove expired games
	public List<String> getPlayersInAllGames(long currentTime)
	{
		List<String> playerList = new ArrayList<String>();

		// lock out while searching for players
		synchronized(gameList)
		{
			// update state of games before checking active players
			refreshGameList(currentTime);

			// add active players from each tictactoe game
			for ( Long key : gameList.keySet() ) 
			{
				TictactoeGame game = gameList.get(key);

				// gameState should have been updated already by refreshGameList
				TictactoeGame.GameState gameState = game.getGameStateNoUpdate();

				// add players if game is still active
				// names of players in the game with given ID
				List<String> playersInCurrentGame = game.getPlayerNames();

				// add to list of players in active games
				playerList.addAll(playersInCurrentGame);
			}
		}

		return playerList;
	}

	// get id of game that player is currently in
	public long getPlayerGameId(long currentTime, String playerName)
	{
		// lock out while searching for players
		synchronized(gameList)
		{
			// update state of games before checking active players
			refreshGameList(currentTime);

			// search all active games
			for ( Long key : gameList.keySet() ) 
			{
				TictactoeGame game = gameList.get(key);

				// return the id for the game if player is x/o
				if (game.playerIsInThisGame(playerName))
				{
					return key;
				}
			}
		}

		// player was not found in any game
		return -1;
	}

	public TictactoeGame getGameByPlayerName(long currentTime, @NonNull String playerName)
	{
		synchronized(gameList)
		{
			// update list
			refreshGameList(currentTime);

			// get id
			long gameId = getPlayerGameId(currentTime, playerName);

			if (gameId > 0)
			{
				return gameList.get(gameId);
			}
		}
		return null;
	}

	public TictactoeGame getGameCopyByPlayerName(long currentTime, @NonNull String playerName)
	{
		TictactoeGame game = getGameByPlayerName(currentTime, playerName);

		if (game != null)
		{
			synchronized(game)
			{
				// player has checked in if requesting state
				game.setPlayerReadyByName(currentTime, playerName);

				// return a copy of the game if found
				return new TictactoeGame(game);
			}
		}

		// return a game in error state
		return new TictactoeGame((currentTime));
	}

	// player wants to add a move to the board
	public TictactoeGameService.GameServiceResponse sendTictactoeMove(long currentTime, @NonNull String thisPlayerName, int location)
	{
		TictactoeGame currentGame = getGameByPlayerName(currentTime, thisPlayerName);

		if (currentGame != null)
		{
			synchronized(currentGame)
			{
				if (currentGame.handlePlayerMoveByPlayername(currentTime, thisPlayerName, location))
				{
					return TictactoeGameService.GameServiceResponse.SUCCESS;
				}
			}
		}

		return TictactoeGameService.GameServiceResponse.INVALID_MOVE;
	}

	public void printGameIds()
	{
		logger.error("Active Games: " + gameList.keySet());
	}

	public void printGameById(long id)
	{
		// lock out while searching for players
		synchronized(gameList)
		{
			TictactoeGame game = gameList.get(id);
			if (game != null)
			{
				game.printBoard();
			}
			else
			{
				logger.error("Game id " + id + " did not exist");
			}
		}
	}
}

package com.jonathan.web.resources;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import com.jonathan.web.resources.TictactoeGame;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Size;
import javax.validation.constraints.NotEmpty;
import org.springframework.lang.NonNull;
import org.slf4j.LoggerFactory;
import java.io.Serializable;

import lombok.Data;
import java.io.Serializable;

@Data
@SuppressWarnings("serial")
public class TictactoeGameDto implements Serializable
{
	public static final int BOARD_SIZE = 9;

	// game is starting/player is moving/player won/draw
	private TictactoeGame.GameState gameState;

	// user names of players
	private String xPlayerName;
	private String oPlayerName;

	// if user has checked in
	private boolean xPlayerReady;
	private boolean oPlayerReady;

	// 3x3 tictactoe game board (row = index/3; col = index%3)
	private char gameBoard[];

	// milliseconds before game times out if no action taken
	public static final long ACTION_TIMEOUT_MS = 15000;

	// milliseconds before game is deleted after ending
	public static final long ACTION_DELETE_GAME_MS = ACTION_TIMEOUT_MS + 30000;

	// system time (in milliseconds) that last allowed move was received from user
	private long lastMoveTimeMs;

	// reason the game ended
	private String gameOverMessage;

	// create copy of a tictactoe game to DTO type for rabbitmq
	public TictactoeGameDto(TictactoeGame copiedGame)
	{
		gameState = copiedGame.getGameStateNoUpdate();
		xPlayerName = copiedGame.getXPlayerName();
		oPlayerName = copiedGame.getOPlayerName();
		xPlayerReady = copiedGame.getXPlayerReady();
		oPlayerReady = copiedGame.getOPlayerReady();
		gameBoard = copiedGame.getGameBoard();
		lastMoveTimeMs = copiedGame.getLastMoveTimeMs();
		gameOverMessage = copiedGame.getGameOverMessage();
	}

	public List<String> getPlayerNames()
	{
		List<String> playerNames = new ArrayList<String>(List.of(xPlayerName, oPlayerName));
		return playerNames;
	}

	// copy constructor only
	private TictactoeGameDto()
	{
	}

	public String getXPlayerName()
	{
		return xPlayerName;
	}

	public String getOPlayerName()
	{
		return oPlayerName;
	}

	public boolean getXPlayerReady()
	{
		return xPlayerReady;
	}

	public boolean getOPlayerReady()
	{
		return oPlayerReady;
	}

}



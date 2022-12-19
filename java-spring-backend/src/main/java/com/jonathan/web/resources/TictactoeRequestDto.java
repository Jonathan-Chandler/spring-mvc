package com.jonathan.web.resources;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import javax.validation.constraints.NotEmpty;

@Data
public class TictactoeRequestDto 
{
	public enum ResponseType
	{
		SUCCESS,
		START_GAME,
		ERROR_CURRENT_PLAYER_DOES_NOT_EXIST,
		ERROR_CURRENT_PLAYER_IS_IN_GAME,
		ERROR_VERSUS_PLAYER_DOES_NOT_EXIST,
		ERROR_VERSUS_PLAYER_IS_IN_GAME,
		ERROR_REQUEST_EXISTS,
		ERROR_UNKNOWN,
	}


	// username of player doing tictactoe_action
	private ResponseType responseType;
	private String currentPlayer;
	private String versusPlayer;
	private long gameId;

	public TictactoeRequestDto(ResponseType returnResponse)
	{
		responseType = returnResponse;
		currentPlayer = "";
		versusPlayer = "";
		gameId = -1;
	}

	public TictactoeRequestDto(ResponseType returnResponse, long returnGameId, String requestingUser, String requestedUser)
	{
	    responseType = returnResponse;
		gameId = returnGameId;
		currentPlayer = requestingUser;
		versusPlayer = requestedUser;
	}

	public TictactoeRequestDto(ResponseType returnResponse, String requestingUser, String requestedUser)
	{
	    responseType = returnResponse;
		gameId = -1;
		currentPlayer = requestingUser;
		versusPlayer = requestedUser;
	}

	//// possible user actions
	//enum tictactoe_action
	//{
	//	ENTER_GAME,
	//	LEAVE_GAME,
	//	LIST_GAMES,
	//	MOVE,
	//};
	//private int action;

	//// index to add X/O if action is MOVE
	//private int move_index;

	//// if action was ENTER_GAME and game should be real time
	//private boolean realtime_game;
}


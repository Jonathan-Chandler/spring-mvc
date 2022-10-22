package com.jonathan.web.resources;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TictactoeRequestDto 
{
	private int game_id;

	// username of player doing tictactoe_action
	private String player_username;

	// possible user actions
	enum tictactoe_action
	{
		ENTER_GAME,
		LEAVE_GAME,
		MOVE,
	};

	// index to add X/O if action is MOVE
	private int move_index;

	//// if action was ENTER_GAME and game should be real time
	//private boolean realtime_game;
}


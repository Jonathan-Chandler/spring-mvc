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
public class TictactoeGameResponseDto 
{
	private int game_id;

	// user names of players in game
	private String player1_username; 
	private String player2_username;

	// player that is next to move (false = player1 / true = player2)
	private boolean active_player;

	// if game is over and winning player (same as active player)
	private boolean game_over;
	private boolean winning_player;

	// latest game board after previous move
	private String board;
}


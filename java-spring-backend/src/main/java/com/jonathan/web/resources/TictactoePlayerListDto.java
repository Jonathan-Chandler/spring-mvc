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
public class TictactoePlayerListDto 
{
	//TictactoePlayerListDto(String[] players)
	//{
	//	Arrays.copyOf(players, players.length);
	//}

	// entire playerlist
	private String[] playerList;

	// players that this user has requested to play against
	private String[] requestedUsers;

	// players that have requested to play against this player
	private String[] requestingUsers;

	//@Override
    //public String toString() {
    //    return String.format("{usernames:%s}", usernames);
    //}
}


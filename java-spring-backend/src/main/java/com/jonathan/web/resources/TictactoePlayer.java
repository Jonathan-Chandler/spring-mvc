package com.jonathan.web.resources;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Size;
import javax.validation.constraints.NotEmpty;

public class TictactoePlayer 
{
	//public enum PlayerState {
	//	IN_LOBBY,
	//	JOINING_GAME,
	//	IN_GAME,
	//	GAME_OVER,
	//}
	public enum PlayerResponse {
		SUCCESS,
		FAILED_PLAYER_REQUEST_EXISTS,
	}

	// time before user will be removed from the list
	private static final long PLAYER_TIMEOUT_MS = 15000;

	// users that this user has requested a match against
	private ArrayList<String> requestedUsers;

	// time that last message was received from user
	private Long lastCheckin;

	// ID of the game that this player has joined
	//private Long currentGameId;

	public TictactoePlayer(Long time)
	{
		requestedUsers = new ArrayList<String>();
		lastCheckin = time;
		//currentGameId = Long.valueOf(-1);
	}

	public void setCheckinTime(Long currentTime)
	{
		lastCheckin = currentTime;
	}

	public List<String> getRequestedUsers()
	{
		return requestedUsers;
	}

	public PlayerResponse addRequestedUser(String requestedUser)
	{
		// add to player match requests
		if (!requestedUsers.contains(requestedUser))
		{
			requestedUsers.add(requestedUser);
			return PlayerResponse.SUCCESS;
		}

		// player was already in the request list
		return PlayerResponse.FAILED_PLAYER_REQUEST_EXISTS;
	}
	
	public boolean hasRequestedUser(String requestFrom)
	{
		return (requestedUsers.contains(requestFrom));
	}

	public void clearRequests()
	{
		requestedUsers.clear();
	}

	public void joinGame(long currentTime, long gameId)
	{
		// set time that player joined game
		lastCheckin = currentTime;

		// set ID for game lookup
		//currentGameId = gameId;

		// clear requested users and set state to join game
		requestedUsers.clear();
		//state = PlayerState.JOINING_GAME;
	}

	public boolean isActive(Long currentTime)
	{
		if ((currentTime - lastCheckin) > PLAYER_TIMEOUT_MS)
		{
			return false;
		}
		return true;
	}

	//public PlayerState getState()
	//{
	//	return state;
	//}

	//public Long getGameId()
	//{
	//	return currentGameId;
	//}

	//public void setState(PlayerState newState)
	//{
	//	state = newState;
	//}

	//public boolean isInLobby()
	//{
	//	return (state == PlayerState.IN_LOBBY);
	//}


	//@Override
    //public String toString() {
    //    return String.format("{usernames:%s}", usernames);
    //}
}


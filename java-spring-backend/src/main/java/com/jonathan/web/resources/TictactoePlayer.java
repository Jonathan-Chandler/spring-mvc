package com.jonathan.web.resources;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Size;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
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
	private static final long PLAYER_TIMEOUT_MS = 20000;

	// users that this user has requested a match against
	private List<String> requestedUsers;

	// time that last message was received from user
	private long checkInTime;

	public TictactoePlayer(long time)
	{
		requestedUsers = new ArrayList<String>();
		checkInTime = time;
	}

	public List<String> getRequestedUsers(long currentTime)
	{
		checkInTime = currentTime;

		return requestedUsers;
	}

	public PlayerResponse addRequestedUser(long currentTime, String requestedUser)
	{
		// update time
		checkInTime = currentTime;

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
		checkInTime = currentTime;

		// set ID for game lookup
		//currentGameId = gameId;

		// clear requested users and set state to join game
		requestedUsers.clear();
		//state = PlayerState.JOINING_GAME;
	}

	public boolean isActive(long currentTime)
	{
		if ((currentTime - checkInTime) > PLAYER_TIMEOUT_MS)
		{
			return false;
		}
		return true;
	}

	//public PlayerState getState()
	//{
	//	return state;
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


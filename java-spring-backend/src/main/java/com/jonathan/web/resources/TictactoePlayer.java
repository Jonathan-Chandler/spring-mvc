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
	public enum PlayerState {
		NONE,
		IN_LOBBY,
		JOINING_GAME,
		IN_GAME,
	}

	// time before user will be removed from the list
	private static final long PLAYER_TIMEOUT_MS = 15000;

	// user is in lobby/game/game over/not waiting to play
	private PlayerState state;

	// users that this user has requested a match against
	private ArrayList<String> requestedUsers;

	// time that last message was received from user
	private Long lastCheckin;

	// ID of the game that this player has joined
	private Long currentGame;

	public TictactoePlayer(Long time)
	{
		state = PlayerState.NONE;
		requestedUsers = new ArrayList<String>();
		lastCheckin = time;
	}

	public void setCheckinTime(Long currentTime)
	{
		lastCheckin = currentTime;
	}

	public List<String> getRequestedUsers()
	{
		return requestedUsers;
	}

	public void addRequestedUser(String requestedUser)
	{
		if (!requestedUsers.contains(requestedUser))
		{
			requestedUsers.add(requestedUser);
		}
	}
	
	public boolean hasRequestedUser(String requestFrom)
	{
		return (requestedUsers.contains(requestFrom));
	}

	public void clearRequests()
	{
		requestedUsers.clear();
	}

	public PlayerState getState()
	{
		return state;
	}

	public void joinGame(long currentTime, long gameId)
	{
		// set ID for game lookup
		currentGame = gameId;

		// clear requested users and set state to join game
		requestedUsers.clear();
		state = PlayerState.JOINING_GAME;
	}

	public void setState(PlayerState newState)
	{
		state = newState;
	}

	public boolean isActive(Long currentTime)
	{
		if ((currentTime - lastCheckin) > PLAYER_TIMEOUT_MS)
		{
			return false;
		}
		return true;
	}

	public boolean isInLobby()
	{
		return (state == PlayerState.IN_LOBBY);
	}


	//@Override
    //public String toString() {
    //    return String.format("{usernames:%s}", usernames);
    //}
}


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

	// user is in lobby/game/game over/not waiting to play
	private PlayerState state;

	// users that this user has requested a match against
	private ArrayList<String> requestedUsers;

	// time that last message was received from user
	private Long lastCheckin;

	public TictactoePlayer(Long time)
	{
		state = PlayerState.NONE;
		requestedUsers = new ArrayList<String>();
		lastCheckin = time;
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
		requestedUsers = new ArrayList<String>();
	}

	public PlayerState getState()
	{
		return state;
	}

	public void setState(PlayerState newState)
	{
		state = newState;
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


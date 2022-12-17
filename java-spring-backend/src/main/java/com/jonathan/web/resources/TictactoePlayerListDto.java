package com.jonathan.web.resources;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.ArrayList;

import javax.validation.constraints.Size;
import javax.validation.constraints.NotEmpty;

public class TictactoePlayerListDto 
{
	// entire playerlist
	private List<String> availableUsers;

	// players that this user has requested to play against
	private List<String> requestedUsers;

	// players that have requested to play against this player
	private List<String> requestingUsers;

	public TictactoePlayerListDto(List<String> available, List<String> requested, List<String> requesting)
	{
		availableUsers = available;
		requestedUsers = requested;
		requestingUsers = requesting;
	}

	public List<String> getAvailableUsers()
	{
		return availableUsers;
	}

	public List<String> getRequestedUsers()
	{
		return requestedUsers;
	}

	public List<String> getRequestingUsers()
	{
		return requestingUsers;
	}

	public int getAvailableUsersCount()
	{
		return availableUsers.size();
	}

	public int getRequestedUsersCount()
	{
		return requestedUsers.size();
	}

	public int getRequestingUsersCount()
	{
		return requestingUsers.size();
	}

	//@Override
    //public String toString() {
    //    return String.format("{usernames:%s}", usernames);
    //}
}


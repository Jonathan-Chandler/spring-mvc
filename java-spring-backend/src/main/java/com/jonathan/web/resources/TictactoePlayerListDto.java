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
	public enum ServiceResponse {
		SUCCESS,
		PLAYER_IN_GAME,
		UNKNOWN_ERROR,
	}

	// tictactoe service response
	ServiceResponse serviceResponse;

	// entire playerlist
	private List<String> availableUsers;

	// players that this user has requested to play against
	private List<String> requestedUsers;

	// players that have requested to play against this player
	private List<String> requestingUsers;

	// constructor for error state response
	public TictactoePlayerListDto(ServiceResponse currentState)
	{
		// reason for failure
		serviceResponse = currentState;

		// return empty lists if TictactoeService returns an error response
		availableUsers = new ArrayList<String>();
		requestedUsers = new ArrayList<String>();
		requestingUsers = new ArrayList<String>();
	}

	public TictactoePlayerListDto(List<String> available, List<String> requested, List<String> requesting)
	{
		serviceResponse = ServiceResponse.SUCCESS;
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

	public ServiceResponse getServiceResponse()
	{
		return serviceResponse;
	}

	//@Override
    //public String toString() {
    //    return String.format("{usernames:%s}", usernames);
    //}
}


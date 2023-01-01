package com.jonathan.web.frontend;
import lombok.Data;
import java.io.Serializable;

@Data
@SuppressWarnings("serial")
public class RequestDto implements Serializable
{
	public enum Type
	{
		GET_PLAYERLIST,
		REQUEST_GAME,
		CHECK_IN_GAME,
		REQUEST_MOVE,
		REFRESH_GAME,
		FORFEIT_GAME,
	}

	// message type
	private Type requestType;

	// user that is being requested to play against
	private String requestedUser;

	// location to move on the game board
	int moveLocation;

	public RequestDto() 
	{
	}

	public RequestDto(Type requestType) {
		this.requestType = requestType;
	}
}

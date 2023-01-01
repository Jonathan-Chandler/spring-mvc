package com.jonathan.web.frontend;
import lombok.Data;

//public class TestDto extends Serializable
//public class RequestDto implements Serializable
//@SuppressWarnings("serial")
//public class RequestDto implements Serializable
@Data
public class RequestDto
{
	public enum Type
	{
		GET_PLAYERLIST,
		REQUEST_GAME,
		REQUEST_MOVE,
		REFRESH_GAME,
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

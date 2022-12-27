package com.jonathan.web.frontend;
import java.io.Serializable;

//public class TestDto extends Serializable
//public class RequestDto implements Serializable
@SuppressWarnings("serial")
public class ResponseDto implements Serializable
{
	public enum Type
	{
		PLAYERLIST,
		JOIN_GAME,
		REFRESH_GAME,
	}

	private Type responseType;
	private Type requestEnemyPlayer;

	public ResponseDto() 
	{
	}

	public ResponseDto(Type responseType) {
		this.responseType = responseType;
	}

	public Type getRequestType()
	{
		return responseType;
	}

	public Type setResponseType(Type responseType)
	{
		return responseType;
	}
}

package com.jonathan.web.frontend;
import java.io.Serializable;
import lombok.Data;

@Data
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
}

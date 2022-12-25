package com.jonathan.web.frontend;

//public class TestDto extends Serializable
public class RequestDto
{
	public enum Type
	{
		GET_PLAYERLIST,
	}

	private Type requestType;

	public RequestDto() {
	}

	public RequestDto(Type requestType) {
		this.requestType = requestType;
	}
}

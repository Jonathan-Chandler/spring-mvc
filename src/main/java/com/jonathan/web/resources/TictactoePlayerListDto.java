package com.jonathan.web.resources;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TictactoePlayerListDto 
{
	//public TictactoePlayerListDto(String username, boolean myRequest, boolean theirRequest)
	//{
	//	this.username = username;
	//	this.myRequest = myRequest;
	//	this.theirRequest = theirRequest;
	//}
	private String username;
	private boolean myRequest;
	private boolean theirRequest;
}


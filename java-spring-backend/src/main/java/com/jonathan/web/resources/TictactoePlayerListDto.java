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
	//TictactoePlayerListDto(String[] players)
	//{
	//	Arrays.copyOf(players, players.length);
	//}

	private String[] usernames;
	//@Override
    //public String toString() {
    //    return String.format("{usernames:%s}", usernames);
    //}
}


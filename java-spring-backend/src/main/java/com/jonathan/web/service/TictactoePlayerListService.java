package com.jonathan.web.service;

import java.util.List;

import com.jonathan.web.resources.TictactoeGame;
import com.jonathan.web.resources.TictactoePlayerListDto;
import com.jonathan.web.resources.TictactoeRequestDto;
import org.springframework.lang.NonNull;

public interface TictactoePlayerListService
{
	public enum TictactoePlayerListServiceResponse 
	{
		SUCCESS,
		START_GAME,
		ERROR_CURRENT_PLAYER_DOES_NOT_EXIST,
		ERROR_CURRENT_PLAYER_IS_IN_GAME,
		ERROR_VERSUS_PLAYER_DOES_NOT_EXIST,
		ERROR_VERSUS_PLAYER_IS_IN_GAME,
		ERROR_REQUEST_EXISTS,
	}

	//public List<String> getAllPlayers(long currentTime);

	public TictactoePlayerListDto getPlayerList(long currentTime, @NonNull String thisPlayerName);

	public TictactoeRequestDto addPlayerRequest(long currentTime, @NonNull String thisPlayerName, @NonNull String versusPlayerName);

	public void reset();

  //public List<TictactoePlayerListDto> getPlayerList();
	//public TictactoePlayerListDto getPlayerList();
	//public void userCheckIn(String playerName);
	//public void userRequestMatch(String requestFrom, String requestTo);
//  public Tictactoe findByPlayer(String username);
//
//  public void deleteById(int id);
}


package com.jonathan.web.service;

import java.util.List;

import com.jonathan.web.entities.TictactoeGame;
import com.jonathan.web.resources.TictactoePlayerListDto;

public interface TictactoeService
{
  //public List<TictactoePlayerListDto> getPlayerList();
	public TictactoePlayerListDto getPlayerList();
	public void userCheckIn(String playerName);
	public void userRequestMatch(String requestFrom, String requestTo);
//  public Tictactoe findByPlayer(String username);
//
//  public void deleteById(int id);
}


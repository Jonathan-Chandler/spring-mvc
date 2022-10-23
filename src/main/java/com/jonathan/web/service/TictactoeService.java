package com.jonathan.web.service;

import java.util.List;

import com.jonathan.web.entities.Tictactoe;
import com.jonathan.web.resources.TictactoePlayerListDto;

public interface TictactoeService
{
  public List<TictactoePlayerListDto> getPlayerList();
//  public Tictactoe findByPlayer(String username);
//
//  public void deleteById(int id);
}


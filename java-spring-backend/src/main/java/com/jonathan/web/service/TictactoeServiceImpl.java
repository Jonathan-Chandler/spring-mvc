package com.jonathan.web.service;

import java.util.List;
import java.util.ArrayList;

import com.jonathan.web.entities.TictactoeGame;
import com.jonathan.web.resources.TictactoePlayerListDto;
import com.jonathan.web.resources.OnlineUserDto;
import com.jonathan.web.service.TictactoeService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class TictactoeServiceImpl implements TictactoeService
{
  @Autowired
  private UserService userService;

  public List<TictactoePlayerListDto> getPlayerList()
  {
    List<OnlineUserDto> onlineUsers = userService.getOnlineUsers();
    List<TictactoePlayerListDto> tttPlayerList = new ArrayList<TictactoePlayerListDto>();

    for (int i = 0; i < onlineUsers.size(); i++) {
      tttPlayerList.add(new TictactoePlayerListDto(onlineUsers.get(i).getUsername(), true, true));
    }

    return tttPlayerList;
  }
}


package com.jonathan.web.controllers;

import java.time.Instant;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import java.security.SecureRandom;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.jonathan.web.service.UserService;


import org.springframework.security.core.AuthenticationException;
import com.jonathan.web.resources.UserLoginDto;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.jonathan.web.service.UserService;
import com.jonathan.web.service.TictactoePlayerListService;
import com.jonathan.web.service.TictactoeGameService;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import com.jonathan.web.dao.UserRepository;
import com.jonathan.web.resources.TictactoeRequestDto;
import com.jonathan.web.resources.TictactoePlayerListDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import java.util.List;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.core.Message;
import com.jonathan.web.resources.TestDto;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import com.jonathan.web.frontend.RequestDto;
import org.springframework.lang.NonNull;

import com.jonathan.web.resources.TictactoeRequestDto;
import com.jonathan.web.resources.TictactoeGame;


import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import Java.lang.System;

//import org.springframework.amqp.core.ExchangeTypes.*;

import org.springframework.context.annotation.Profile;

@Profile("production")
public class TReceiver 
{
	//@Autowired
	//Logger logger;
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TictactoePlayerListService tictactoePlayerListService;

	@Autowired
	private TictactoeGameService gameService;

    @Autowired
    private RabbitTemplate template;

	@RabbitListener(bindings = @QueueBinding(
		value = @Queue(value = "playerQueue", durable = "true"),
        exchange = @Exchange(value = "amq.topic", type = "topic"),
        key = "from.user.*")
	)
    public void receiveRequestDto(Message message, @RequestBody final RequestDto request)
	{
		String routingKey = message.getMessageProperties().getReceivedRoutingKey();
		String usernameFromRoutingKey = routingKey.substring(10);
		String routingKeyToUser;
		TictactoePlayerListDto playerList;

		// message wasn't a request or routing didn't match login username
		if (!messageIsValid(message, "com.jonathan.web.frontend.RequestDto", usernameFromRoutingKey))
		{
			return;
		}

		RequestDto.Type requestType = request.getRequestType();
		logger.error("getRequestType = " + requestType);

		// call response functions based on request type
		switch (requestType)
		{
			case GET_PLAYERLIST:
				sendPlayerList(usernameFromRoutingKey);
				break;
			case REQUEST_GAME:
				requestGame(usernameFromRoutingKey, request);
				break;
			case REFRESH_GAME:
				sendGame(usernameFromRoutingKey);
				break;
			case REQUEST_MOVE:
				sendMove(usernameFromRoutingKey, request);
				break;
			default:
				logger.error("Unknown requestType: " + requestType);
		};
    }

	private boolean messageIsValid(Message message, String expectedType, String expectedUser)
	{
		// reject null message types
		if (expectedType == null || expectedType.isEmpty() || expectedType.isBlank())
		{
			return false;
		}

		// reject null users
		if (expectedUser == null || expectedUser.isEmpty() || expectedUser.isBlank())
		{
			return false;
		}

        Map<String, Object> headers = message.getMessageProperties().getHeaders();
		String headerTypeId = headers.get("__TypeId__").toString();
		String headerUsername = headers.get("login").toString();
		logger.error(" [x] message headers login: " + headers.get("login"));
		logger.error(" [x] message headers routingKey: " + message.getMessageProperties().getReceivedRoutingKey());
		logger.error(" [x] message headers __TypeId__: " + headers.get("__TypeId__"));

		// both matched expected values
		return (expectedType.equals(headerTypeId) && expectedUser.equals(headerUsername));
	}

	public void requestGame(@NonNull String requestingUser, @NonNull RequestDto gameRequest)
	{
		String requestedUser = gameRequest.getRequestedUser();
		long currentTime;
		TictactoeRequestDto requestDto;

		if (requestedUser == null || requestedUser.isEmpty() || requestedUser.isBlank())
		{
			logger.error("Player " + requestingUser + " requested invalid match");
			return;
		}

		// add the request to the player list
		currentTime = java.lang.System.currentTimeMillis();
		requestDto = tictactoePlayerListService.addPlayerRequest(currentTime, requestingUser, requestedUser);

		switch (requestDto.getResponseType())
		{
			//case TictactoeRequestDto.ResponseType.START_GAME:
			case START_GAME:
				// send both players to game if both have requested
				sendGame(requestedUser);

				// fall through
			//case TictactoeRequestDto.ResponseType.ERROR_CURRENT_PLAYER_IS_IN_GAME:
			case ERROR_CURRENT_PLAYER_IS_IN_GAME:
				// send this player to game if in progress
				sendGame(requestingUser);
				break;
			default:
				// refresh player list in all other cases
				break;
		}
	}

	public void sendPlayerList(@NonNull String username)
	{
		String routingKeyToUser = "to.user." + username;
		long currentTime = java.lang.System.currentTimeMillis();
		TictactoePlayerListDto playerListDto;

		// get player list
		playerListDto = tictactoePlayerListService.getPlayerList(currentTime, username);

		// send game information if already joined a game
		if (playerListDto.getServiceResponse() == TictactoePlayerListDto.ServiceResponse.PLAYER_IN_GAME)
		{
			// send this player to game if in progress instead of the player list
			sendGame(username);
		}
		else
		{
			template.convertAndSend("amq.topic", routingKeyToUser, playerListDto);
		}
	}

	public void sendMove(@NonNull String username, @NonNull RequestDto request)
	{
		long currentTime = java.lang.System.currentTimeMillis();
		int location = request.getMoveLocation();

		TictactoeGameService.GameServiceResponse response;

		response = gameService.sendTictactoeMove(currentTime, username, location);
		if (response == TictactoeGameService.GameServiceResponse.SUCCESS)
		{
			// move was accepted, get game state
			TictactoeGame currentGame = gameService.getGameCopyByPlayerName(currentTime, username);
			List<String> playersInGame = currentGame.getPlayerNames();

			// update game state for both users
			for (String playerName : playersInGame)
			{
				String routingKeyToUser = "to.user." + playerName;
				template.convertAndSend("amq.topic", routingKeyToUser, currentGame);
			}
		}
		else
		{
			// user is out of sync with game state or tried to make an invalid move
			sendGame(username);
		}
	}

	public void sendGame(@NonNull String username)
	{
		long currentTime = java.lang.System.currentTimeMillis();
		TictactoeGame currentGame = gameService.getGameCopyByPlayerName(currentTime, username);
		String routingKeyToUser = "to.user." + username;

		// send game information to this user
		template.convertAndSend("amq.topic", routingKeyToUser, currentGame);
	}

	@RabbitListener(bindings = @QueueBinding(
		value = @Queue(value = "playerQueue", durable = "true"),
        exchange = @Exchange(value = "amq.topic", type = "topic"),
        key = "from.user.*")
	)
    public void receiveTestDto(Message message, @RequestBody final TestDto value)
	{
		String routingKey = message.getMessageProperties().getReceivedRoutingKey();
		String routingUsername = routingKey.substring(10);
		if (messageIsValid(message, "com.jonathan.web.resources.TestDto", routingUsername))
		{
			Map<String, Object> headers = message.getMessageProperties().getHeaders();
			String headerUsername = headers.get("login").toString();
			String messageValue = value.getMessage();
			logger.error("Headers: " + headers.keySet());
			logger.error("__TypeId__: " + headers.get("__TypeId__"));

			logger.error(" [x] message headers login: " + headers.get("login"));
			logger.error(" [x] message headers routingKey: " + message.getMessageProperties().getReceivedRoutingKey());
			logger.error(" [x] getMessage = " + messageValue);
		}
	}

    //public void receive(@RequestBody final TictactoePlayerListDto playerList)

	//@RabbitListener(bindings = @QueueBinding(
	//	value = @Queue(value = "hello", durable = "true"),
    //    exchange = @Exchange(value = "auto.exch"),
    //    key = "orderRoutingKey")
	//)
	
	//@RabbitListener(bindings = @QueueBinding(
	//	value = @Queue(value = "hello", durable = "true"),
    //    exchange = @Exchange(value = "amq.topic", type = "topic"),
    //    key = "hello")
	//)
    //public void receive(@RequestBody final String message)
	//{
    //    System.out.println(" [x] message from amq topic: '" + message + "'");
    //    logger.error(" [x] message from amq topic: '" + message + "'");
    //}
}


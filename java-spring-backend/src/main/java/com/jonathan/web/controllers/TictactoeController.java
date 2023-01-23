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

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
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

import java.nio.charset.StandardCharsets;
import java.util.Map;
import com.jonathan.web.frontend.RequestDto;
import org.springframework.lang.NonNull;

import com.jonathan.web.resources.TictactoeRequestDto;
import com.jonathan.web.resources.TictactoeGameDto;

import org.springframework.stereotype.Component;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

import org.springframework.context.annotation.Profile;

@Profile("production")
@Component
public class TictactoeController 
{
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
			logger.error("Message was not valid for user: " + usernameFromRoutingKey);
			return;
		}

		RequestDto.Type requestType = request.getRequestType();

		// call response functions based on request type
		switch (requestType)
		{
			case GET_PLAYERLIST:
				// requestType == 0
				logger.error("GET_PLAYERLIST for user: " + usernameFromRoutingKey);
				handleRefresh(usernameFromRoutingKey);
				break;
			case REQUEST_GAME:
				// requestType == 1
				logger.error("REQUEST_GAME for user: " + usernameFromRoutingKey);
				requestGame(usernameFromRoutingKey, request);
				break;
			case CHECK_IN_GAME:
				// requestType == 2
				logger.error("CHECK_IN_GAME for user: " + usernameFromRoutingKey);
				playerCheckIn(usernameFromRoutingKey);
				break;
			case REQUEST_MOVE:
				// requestType == 3
				logger.error("REQUEST_MOVE for user: " + usernameFromRoutingKey);
				sendMove(usernameFromRoutingKey, request);
				break;
			case REFRESH_GAME:
				// requestType == 4
				logger.error("REFRESH_GAME for user: " + usernameFromRoutingKey);
				handleGameRefresh(usernameFromRoutingKey);
				break;
			case FORFEIT_GAME:
				// requestType == 5
				logger.error("FORFEIT_GAME for user: " + usernameFromRoutingKey);
				playerForfeit(usernameFromRoutingKey);
				break;
			default:
				logger.error("Unknown requestType: '" + requestType + "' for user: " + usernameFromRoutingKey);
				//logger.error("Unknown requestType: " + requestType);
				break;
		};
    }

	public void playerCheckIn(@NonNull String requestingUser)
	{
		// add the request to the player list
		long currentTime = java.lang.System.currentTimeMillis();

		// player is ready to start the game
		TictactoeGameDto currentGame = gameService.checkInPlayer(currentTime, requestingUser);

		// update state for both players
		List<String> playersInGame = currentGame.getPlayerNames();

		// update game state for both users
		for (String playerName : playersInGame)
		{
			if (playerName != null && !playerName.isEmpty())
			{
				// game is in a valid state
				handleGameRefresh(playerName);
			}
			else
			{
				// player is checking in to an invalid game, respond with game in error state and return
				handleGameRefresh(requestingUser);
				return;
			}
		}
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
			case START_GAME:
				// send both players to game if both have requested
				logger.error("RequestGame returns START_GAME for " + requestingUser + " and " + requestedUser);
				handleGameRefresh(requestedUser);
				handleGameRefresh(requestingUser);
				break;

			case ERROR_CURRENT_PLAYER_IS_IN_GAME:
				// send this player to game if in progress
				logger.error("RequestGame returns ERROR " + requestingUser + " is in game");
				handleGameRefresh(requestingUser);
				break;

			default:
				logger.error("Player " + requestingUser + " attempts add user " + requestedUser);
				logger.error("Response: " + requestDto.getResponseType());

				// refresh player list in all other cases
				handleRefresh(requestingUser);
				break;
		}
	}

	public void handleRefresh(@NonNull String username)
	{
		String routingKeyToUser = "to.user." + username;
		long currentTime = java.lang.System.currentTimeMillis();
		TictactoePlayerListDto playerListDto;

		// get player list
		playerListDto = tictactoePlayerListService.getPlayerList(currentTime, username);

		// send game information if already joined a game
		if (playerListDto.getServiceResponse() == TictactoePlayerListDto.ServiceResponse.PLAYER_IN_GAME)
		{
			logger.error("handleRefresh sends PLAYER_IN_GAME for user " + username);

			// send this player to game if in progress instead of the player list
			handleGameRefresh(username);
		}
		else
		{
			logger.error("get playerListDto request for user " + username);
			logger.error("routingKeyToUser: " + routingKeyToUser);
			logger.error("playerListDto: " + playerListDto.toString());
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
			TictactoeGameDto currentGame = gameService.getGameCopyByPlayerName(currentTime, username);
			List<String> playersInGame = currentGame.getPlayerNames();

			// update game state for both users
			for (String playerName : playersInGame)
			{
				if (playerName != null && !playerName.isEmpty())
				{
					// game is in a valid state
					String routingKeyToUser = "to.user." + playerName;
					template.convertAndSend("amq.topic", routingKeyToUser, currentGame);
				}
				else
				{
					// player is in an invalid game, respond with game in error state
					handleGameRefresh(username);
					return;
				}
			}
		}
		else
		{
			// user is out of sync with game state or tried to make an invalid move
			handleGameRefresh(username);
		}
	}

	public void playerForfeit(@NonNull String requestingUser)
	{
		long currentTime = java.lang.System.currentTimeMillis();
		TictactoeGameDto tictactoeGame = gameService.sendTictactoeForfeit(currentTime, requestingUser);

		// send the forfeit game state to both users
		broadcastGameState(requestingUser, tictactoeGame);
	}

	public void broadcastGameState(@NonNull String requestingUser, @NonNull TictactoeGameDto currentGame)
	{
		List<String> playersInGame = currentGame.getPlayerNames();

		for (String playerName : playersInGame)
		{
			if (playerName != null && !playerName.isEmpty())
			{
				// game is in a valid state
				String routingKeyToUser = "to.user." + playerName;
				template.convertAndSend("amq.topic", routingKeyToUser, currentGame);
			}
			else
			{
				// player is in an invalid game, only send requestingUser the game in error state
				handleGameRefresh(requestingUser);
				return;
			}
		}
	}

	public void handleGameRefresh(@NonNull String username)
	{
		long currentTime = java.lang.System.currentTimeMillis();
		TictactoeGameDto currentGame = gameService.getGameCopyByPlayerName(currentTime, username);
		String routingKeyToUser = "to.user." + username;

		logger.error("handleGameRefresh to user " + username);
		logger.error("handleGameRefresh routing key " + routingKeyToUser);
		logger.error("handleGameRefresh currentGame: " + currentGame.toString());

		// send game information to this user
		template.convertAndSend("amq.topic", routingKeyToUser, currentGame);
	}
}



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
//import org.springframework.web.bind.annotation.Produces;
//import org.springframework.web.bind.annotation.Path;
//import org.springframework.web.bind.annotation.Consumes;
//import org.springframework.web.bind.annotation.Method;
//import org.springframework.web.bind.annotation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.jonathan.web.service.UserService;
import com.jonathan.web.service.TictactoeService;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import com.jonathan.web.dao.UserRepository;
import com.jonathan.web.resources.TictactoeRequestDto;
import com.jonathan.web.resources.TictactoePlayerListDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import java.util.List;
import java.util.ArrayList;

//import javax.json.JsonObject;
//import org.springframework.http.
import org.slf4j.Logger;
import java.io.IOException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
//import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import com.jonathan.web.resources.TestDto;
import org.springframework.stereotype.Controller;
//import org.springframework.messaging.simp.user.SimpUserRegistry;

import org.springframework.web.socket.messaging.SessionSubscribeEvent;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.support.GenericMessage;
//import org.springframework.messaging.simp.user.SimpUser;
import java.security.Principal;
import com.jonathan.web.controllers.RSender;

//@CrossOrigin(origins = "http://localhost:3000/tictactoe/playerlist")
@Controller
//@CrossOrigin(origins = "http://localhost:3000")
//@CrossOrigin(origins = "http://localhost:3000", allowedHeaders="*")
public class TictactoeController 
{
	@Autowired
	private TictactoeService tictactoeService;

	//@Autowired
	//private SimpUserRegistry simpUserRegistry;

	//private final SimpMessagingTemplate simpMessagingTemplate;

	
	//@Autowired
	//RSender rabbitSender;

	@Autowired
	Logger logger;

	//public TictactoeController(SimpMessagingTemplate simpMessagingTemplate) 
	//{
	//	this.simpMessagingTemplate = simpMessagingTemplate;
	//}
	// send return value to /tictactoe/playerlist (stompClient.subscribe('/tictactoe/playerlist'))
	//@SendTo("/topic/messages")
	//public List<TictactoePlayerListDto> getMessages(String clientMessage)

	// handle messages from /tictactoe/playerlist stompClient.send("/tictactoe/playerlist", ...)
	//@MessageMapping("/greetings")
	@GetMapping(value="/greetings")
	public void getMessages()
	{
		logger.info("sent test message to rsend");
		//rabbitSender.send("test-greetings");
		//logger.info("client message");
		//logger.info("client message: " + greeting);
		//String text = "[" + Instant.now() + "]: " + greeting;
		//simpMessagingTemplate.convertAndSend("/topic/greetings", text);


		//return new TestDto("test");
		//return clientMessage;
		//logger.info("get client message: " + clientMessage);
		//List<TictactoePlayerListDto> playerList = tictactoeService.getPlayerList();
		//return playerList;
	}

	//@EventListener
	//public void handleSessionSubscribeEvent(SessionSubscribeEvent event)
	//{
	//	//getNativeHeader()
	//	logger.info("sessionsubscribeevent: ");
	//	GenericMessage message = (GenericMessage) event.getMessage();
	//	String simpDestination = (String) message.getHeaders().get("simpDestination");
	//	String authHeader = (String) message.getHeaders().get("Authorization");
	//	String nativeHeaders = message.getHeaders().get("nativeHeaders").toString();
	//	logger.info("nativeHeaders: " + nativeHeaders);
	//	logger.info("allHeaders: " + message.getHeaders().toString());
	//	logger.info("simpDestination: " + simpDestination);
	//	logger.info("authHeader: " + authHeader);

	//	if (simpDestination.startsWith("/topic/playerList"))
	//	{
	//		// read user name
	//		Principal userPrincipal = event.getUser();
	//		if (userPrincipal != null)
	//			logger.info("userPrincipal: " + userPrincipal.toString());
	//		else
	//			logger.info("userPrincipal: null");
	//	}

	//	//userPrincipal.getName();
	//}

	//@EventListener
	//public void handleSubscribeEvent(SessionSubscribeEvent event)
	//{
	//  simpMessagingTemplate.convertAndSendToUser(event.getUser().getName(), "/greetings", "greetings");
	//}

	// handle messages from /tictactoe/playerlist stompClient.send("/tictactoe/playerlist", ...)
//	@MessageMapping("/playerList")
//	// send return value to /tictactoe/playerlist (stompClient.subscribe('/tictactoe/playerlist'))
//	//@SendTo("/topic/messages")
//	//public List<TictactoePlayerListDto> getMessages(String clientMessage)
//	public void getPlayerList()
//	{
//		ArrayList<TictactoePlayerListDto> playerList = new ArrayList<TictactoePlayerListDto>();
//		List<String> connections = this.simpUserRegistry
//			.getUsers()
//			.stream()
//			.map(u -> u.getName())
//			.collect(Collectors.toList());
////			.collect(Collectors.toList());
////			.map(SimpUser::getName)
//		logger.info("playerList size: " + connections.size());
//
//		for (int i = 0; i < connections.size(); i++)
//		{
//			playerList.add(new TictactoePlayerListDto(connections.get(i), true, true));
//		}
//
//		//List<TictactoePlayerListDto> playerList = tictactoeService.getPlayerList();
//
//		simpMessagingTemplate.convertAndSend("/topic/playerList", playerList);
//
//		//return new TestDto("test");
//		//return clientMessage;
//		//logger.info("get client message: " + clientMessage);
//		//List<TictactoePlayerListDto> playerList = tictactoeService.getPlayerList();
//		//return playerList;
//	}
}


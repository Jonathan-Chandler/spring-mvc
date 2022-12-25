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

//import javax.json.JsonObject;
//import org.springframework.http.
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
//import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import com.jonathan.web.resources.TestDto;
import org.springframework.stereotype.Controller;

import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.support.GenericMessage;
import java.security.Principal;
//import com.jonathan.web.controllers.RSender;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@Controller
public class TictactoeController 
{
	@Autowired
	private TictactoePlayerListService playerListService;

	@Autowired
	private TictactoeGameService gameService;

	//@Autowired
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	//public getPlayerList(long currentTime, @NonNull String thisPlayerName)
	//{
	//	TictactoePlayerListDto 
	//}

	//public void getPlayerList()
	//{
	//}
	//@GetMapping(value="/greetings")
	//public void getMessages()
	//{
	//	logger.info("sent test message to rsend");
	//}

	//// bind 
	//@RabbitListener(bindings = @QueueBinding(
	//	value = @Queue(value = "playerQueue", durable = "true"),
    //    exchange = @Exchange(value = "amqp.topic"),
    //    key = "user.*")
	//)
    //public void receive(@RequestBody TestDto playerList)
	//{
    //    //System.out.println(" [x] Received '" + playerList + "'");
    //    logger.error(" [x] message from amqp.topic user.*: '" + playerList.getMessage() + "'");
    //}
}


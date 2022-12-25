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

    //public void receive(@RequestBody final TictactoePlayerListDto playerList)

	//@RabbitListener(bindings = @QueueBinding(
	//	value = @Queue(value = "hello", durable = "true"),
    //    exchange = @Exchange(value = "auto.exch"),
    //    key = "orderRoutingKey")
	//)
	@RabbitListener(bindings = @QueueBinding(
		value = @Queue(value = "playerQueue", durable = "true"),
        exchange = @Exchange(value = "amq.topic", type = "topic"),
        key = "from.user.*")
	)
    public void receive(Message message, @RequestBody final TestDto value)
	{
        Map<String, Object> headers = message.getMessageProperties().getHeaders();
		String routingKey = message.getMessageProperties().getReceivedRoutingKey();
		String routingUsername = routingKey.substring(10);
		String headerUsername = headers.get("login").toString();
		String requestToUser = value.getMessage();
        logger.error(" [x] message headers login: " + headers.get("login"));
        logger.error(" [x] message headers routingKey: " + message.getMessageProperties().getReceivedRoutingKey());

		//// check if username in header matches routing key format 'user.<username>'
		//if (!routingUsername.equals(headerUsername))
		//{
		//	logger.error("routing key username: " + routingUsername);
		//	logger.error("Fail username / topic comparison");
		//	return;
		//}

		////receivedRoutingKey=user.test_user123
        //logger.error("get testDto value: " + value.getMessage());

		//// update last check in time
		//tictactoeService.userCheckIn(routingUsername);


		//// send match request
		//logger.error("userRequestMatch(" + headerUsername + ", " + requestToUser + ")");
		//tictactoeService.userRequestMatch(headerUsername, requestToUser);
    }

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


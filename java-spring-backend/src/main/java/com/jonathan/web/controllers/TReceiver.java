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

import org.slf4j.Logger;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import com.jonathan.web.resources.TestDto;

//import org.springframework.amqp.core.ExchangeTypes.*;

public class TReceiver 
{
	@Autowired
	Logger logger;

    //public void receive(@RequestBody final TictactoePlayerListDto playerList)

	//@RabbitListener(bindings = @QueueBinding(
	//	value = @Queue(value = "hello", durable = "true"),
    //    exchange = @Exchange(value = "auto.exch"),
    //    key = "orderRoutingKey")
	//)
	@RabbitListener(bindings = @QueueBinding(
		value = @Queue(value = "playerQueue", durable = "true"),
        exchange = @Exchange(value = "amq.topic", type = "topic"),
        key = "user.*")
	)
    public void receive(@RequestBody TestDto playerList)
	{
        //System.out.println(" [x] Received '" + playerList + "'");
        //logger.error(" [x] message from amq topic: '" + playerList.getMessage() + "'");
        logger.error(" [x] message from amq.topic user.*: '" + playerList.getMessage() + "'");
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


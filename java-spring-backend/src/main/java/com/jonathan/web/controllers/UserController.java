package com.jonathan.web.controller.authentication;

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
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.jonathan.web.service.UserService;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import com.jonathan.web.dao.UserRepository;
import com.jonathan.web.resources.UserRegistrationDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;

// rabbitmq request
import org.springframework.web.bind.annotation.RequestParam;
import com.jonathan.web.resources.rabbitmq.*;
import org.slf4j.Logger;

@RestController
@CrossOrigin(origins = "*")
public class UserController 
{
	@Autowired
	private UserService userService;

	@Autowired
	Logger logger;

	@RequestMapping
	(
		value = {"/login"},
		method = RequestMethod.POST,
		produces = "application/json",
		consumes = "application/json"
	)
	@ResponseBody
	public ResponseEntity<String> login(@RequestBody UserLoginDto userLogin) 
	{
		logger.error("login call");
		HashMap<String, String> responseJson = new HashMap<>();
		String loginJwt;

		try 
		{
			loginJwt = userService.login(userLogin);
		} 
		catch (Exception e) 
		{
			return new ResponseEntity<>("Failed to login: " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}

		HttpHeaders responseHeader = new HttpHeaders();
		responseHeader.add("Authorization", loginJwt);

		responseJson.put("Auth", loginJwt);
		System.out.println(responseJson);

		return ResponseEntity.ok()
			.headers(responseHeader)
			.body("{\"Authorization\": \"" + loginJwt + "\"}");
	}

	@RequestMapping
	(
		value = {"/register"},
		method = RequestMethod.POST,
		produces = "application/json",
		consumes = "application/json"
	)
	@ResponseBody
	public ResponseEntity<String> register(@RequestBody UserRegistrationDto registrationRequest) 
	{
		logger.error("Registration call");

		try 
		{
			String response = userService.register(registrationRequest);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		catch (Exception e)
		{
			logger.info("Failed to register: " + e.getMessage());

			return new ResponseEntity<>("Failed to register: " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	//username - the name of the user
	//password - the password provided (may be missing if e.g. rabbitmq-auth-mechanism-ssl is used)
	@RequestMapping
	(
		path = {"/rabbitmq/user"},
		method = {RequestMethod.GET, RequestMethod.POST},
		produces = "application/x-www-form-urlencoded",
		consumes = "application/x-www-form-urlencoded"
	)
	public String rabbitUser(@RequestParam("username") String username, @RequestParam("password") String password)
	{
		logger.error("rabbitUser call");
		HttpHeaders responseHeader = new HttpHeaders();
		HashMap<String, String> responseJson = new HashMap<>();
		String loginJwt;

		logger.info("Get /rabbitmq/user request: " + username + ":" + password);
		if (username.equals("guest")
				&& password.equals("guest"))
		{
			return "allow administrator";
		}

		try 
		{
			// convert to standard login dto
			UserLoginDto loginDto = new UserLoginDto(username, password);
			loginJwt = userService.login(loginDto);
		} 
		catch (Exception e) 
		{
			return "deny";
		}

		//allow [list of tags] - (for user_path only) - allow access, and mark the user as an having the tags listed
		return "allow ";
	}

	//username - the name of the user
	//vhost - the name of the virtual host being accessed
	//ip - the client ip address
	//Note that you cannot create arbitrary virtual hosts using this plugin; you can only determine whether your users can see / access the ones that exist.
	@RequestMapping
	(
		path = {"/rabbitmq/vhost"},
		method = {RequestMethod.GET, RequestMethod.POST},
		produces = "application/x-www-form-urlencoded",
		consumes = "application/x-www-form-urlencoded"
	)
	public String rabbitVhostPath
	(
		@RequestParam("username")  String username, 
		@RequestParam("vhost")  String vhost, 
		@RequestParam("ip")  String ip
	) 
	{
		logger.info("Get /rabbitmq/vhost request: " + username + " vhost: " + vhost + " ip: " + ip);
		return "allow";
	}

	//username - the name of the user
	//vhost - the name of the virtual host containing the resource
	//resource - the type of resource (exchange, queue, topic)
	//name - the name of the resource
	//permission - the access level to the resource (configure, write, read) - see the Access Control guide for their meaning
	@RequestMapping
	(
		path = {"/rabbitmq/resource"},
		method = {RequestMethod.GET, RequestMethod.POST},
		produces = "application/x-www-form-urlencoded",
		consumes = "application/x-www-form-urlencoded"
	)
	public String rabbitResourcePath
	(
		@RequestParam("username")  String username, 
		@RequestParam("vhost")  String vhost, 
		@RequestParam("resource")  String resource,
		@RequestParam("name")  String name,
		@RequestParam("permission")  String permission
	)
	{
		logger.info
		(
			"Get /rabbitmq/resource request: " + 
			"username: " + username +
			"vhost: " + vhost +
			"resource: " + resource +
			"name: " + name +
			"permission: " + permission
		);

		return "allow";
	}

	//vhost - the name of the virtual host containing the resource
	//resource - the type of resource / topic
	//name - the name of the exchange
	//permission - the access level to the resource (write or read)
	//routing_key - the routing key of a published message (when the permission is write) or routing key of the queue binding (when the permission is read)
	@RequestMapping
	(
		path = {"/rabbitmq/topic"},
		method = {RequestMethod.GET, RequestMethod.POST},
		produces = "application/x-www-form-urlencoded",
		consumes = "application/x-www-form-urlencoded"
	)
	public String rabbitTopicPath
	(
		@RequestParam("username")  String username, 
		@RequestParam("vhost")  String vhost, 
		@RequestParam("resource")  String resource,
		@RequestParam("name")  String name,
		@RequestParam("permission")  String permission,
		@RequestParam("routing_key")  String routing_key
	)
	{
		logger.info
		(
			"Get /rabbitmq/topic request: " +
			"username: " + username +
			"vhost: " + vhost +
			"resource: " + resource + 
			"name: " + name + 
			"permission: " + permission + 
			"routing_key: " + routing_key
		);
		return "allow";
	}
}

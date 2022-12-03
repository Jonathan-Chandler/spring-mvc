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

import org.springframework.web.bind.annotation.RequestParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jonathan.web.service.JwtTokenService;

@RestController
@CrossOrigin(origins = "*")
public class UserController 
{
	@Autowired
	private UserService userService;

	@Autowired
	private JwtTokenService jwtTokenService;

	//@Autowired
	//Logger logger;
	final Logger logger = LoggerFactory.getLogger(this.getClass());

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
		logger.error("Return login auth: " + responseJson);

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

	//rabbitmq param
	//vhost - name of the virtual host being accessed - cannot create virtual hosts
	//name - name of the resource/exchange
	//resource - type of resource / topic
	//routing_key - routing key of a published message (write) or routing key of the queue binding (when permission is read)
	//resource - type of resource (exchange, queue, topic)
	//permission - access level to the resource (configure, write, read)
	@RequestMapping
	(
		path = {"/rabbitmq/user"},
		method = {RequestMethod.GET, RequestMethod.POST},
		produces = "application/x-www-form-urlencoded",
		consumes = "application/x-www-form-urlencoded"
	)
	public String rabbitUser(@RequestParam("username") String username, @RequestParam("password") String password)
	{
		String loginJwt;
		logger.info("Get /rabbitmq/user login request username:" + username + " password:" + password);

		if (username.equals("guest") || username.isEmpty() || password.isEmpty())
		{
			logger.error("/rabbitmq/user denies request: username:" + username + " password:" + password);
			return "deny";
		}

		try 
		{
			// auth failed
			if (!jwtTokenService.validateJwtToken(password) 
					|| !jwtTokenService.validateJwtTokenUsername(password, username))
			{

				logger.error("validation fails for user: " + username + " token: " + password);
				if (!jwtTokenService.validateJwtToken(password))
					logger.error("validation fails to validate jwt token");

				if (!jwtTokenService.validateJwtTokenUsername(password, username))
					logger.error("validation fails to validate username in jwt token");

				logger.error("/rabbitmq/user denies request: username:" + username + " password:" + password);
				return "deny";
			}

			//// convert to standard login dto
			//UserLoginDto loginDto = new UserLoginDto(username, token);
			//loginJwt = userService.login(loginDto);
		} 
		catch (Exception e) 
		{
			logger.error("/rabbitmq/user denies request: username:" + username + " password:" + password);
			logger.error("/rabbitmq/user Error: " + e.getMessage());
			return "deny";
		}

		// allow [list of tags] - user should not have administrator/management tags
		// possible tags: Admin Monitoring Policymaker Management Impersonator None
		return "allow ";
	}

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

		// only allow vhost /
		if (!vhost.equals("/"))
		{
			logger.error("/rabbitmq/vhost denies request: " + username + " vhost: " + vhost + " ip: " + ip);
			return "deny";
		}

		return "allow";
	}

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
		String stompPrefix = "stomp-subscription-";

		// Get /rabbitmq/resource request:  username: test_user123 vhost: / resource: queue name: stomp-subscription-cctHqEUpExbyBY2jbtz3ag permission: write
		logger.info
		(
			"Get /rabbitmq/resource request: " + 
			" username: " + username +
			" vhost: " + vhost +
			" resource: " + resource +
			" name: " + name +
			" permission: " + permission
		);

		// only allow vhost /
		if (!vhost.equals("/"))
		{
			logger.error(
				"/rabbitmq/resource denies request: " + 
				" username: " + username +
				" vhost: " + vhost +
				" resource: " + resource +
				" name: " + name +
				" permission: " + permission
			);
			return "deny";
		}


		// allow access to auto created stomp subscription queues
		if (resource.equals("queue") && name.startsWith(stompPrefix))
		{
			return "allow";
		}

		// allow access to read/write amq topic list
		if ((permission.equals("read") || permission.equals("write"))
			&& resource.equals("exchange") 
			&& name.equals("amq.topic"))
		{
			return "allow";
		}

		logger.error(
			"/rabbitmq/resource denies request: " + 
			" username: " + username +
			" vhost: " + vhost +
			" resource: " + resource +
			" name: " + name +
			" permission: " + permission
		);
		return "deny";
	}

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
		String toUserRoutingKey = "to.user." + username;
		String fromUserRoutingKey = "from.user." + username;
		String playerlistRoutingKey = "playerlist";

		// Get /rabbitmq/topic request:  username: test_user123 vhost: / resource: topic name: amq.topic permission: write routing_key: user.test_user123
		logger.info
		(
			"Get /rabbitmq/topic request: " +
			" username: " + username +
			" vhost: " + vhost +
			" resource: " + resource + 
			" name: " + name + 
			" permission: " + permission + 
			" routing_key: " + routing_key
		);

		// only allow vhost /
		if (!vhost.equals("/"))
		{
			logger.error(
				"/rabbitmq/topic denies request: " + 
				" username: " + username +
				" vhost: " + vhost +
				" resource: " + resource + 
				" name: " + name + 
				" permission: " + permission + 
				" routing_key: " + routing_key
			);
			return "deny";
		}

		// only using topic exchange
		if (!resource.equals("topic"))
		{
			logger.error(
				"/rabbitmq/topic denies request: " + 
				" username: " + username +
				" vhost: " + vhost +
				" resource: " + resource + 
				" name: " + name + 
				" permission: " + permission + 
				" routing_key: " + routing_key
			);
			return "deny";
		}

		// only allow topics in amq.topic
		if (!name.equals("amq.topic"))
		{
			logger.error(
				"/rabbitmq/topic denies request: " + 
				" username: " + username +
				" vhost: " + vhost +
				" resource: " + resource + 
				" name: " + name + 
				" permission: " + permission + 
				" routing_key: " + routing_key
			);
			return "deny";
		}

		// allow read for user on /topic/to.user.<username>
		if (permission.equals("read") && routing_key.equals(toUserRoutingKey))
		{
			return "allow";
		}

		// allow write for user on /topic/from.user.<username>
		if (permission.equals("write") && routing_key.equals(fromUserRoutingKey))
		{
			return "allow";
		}

		// allow read for all users on /topic/playerlist
		if (permission.equals("read") 
			&& routing_key.equals(playerlistRoutingKey))
		{
			return "allow";
		}

		logger.error(
			"/rabbitmq/topic denies request: " + 
			" username: " + username +
			" vhost: " + vhost +
			" resource: " + resource + 
			" name: " + name + 
			" permission: " + permission + 
			" routing_key: " + routing_key
		);
		return "deny";
	}
}

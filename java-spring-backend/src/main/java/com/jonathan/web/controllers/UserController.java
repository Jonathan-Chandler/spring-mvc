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
//import org.springframework.web.bind.annotation.Produces;
//import org.springframework.web.bind.annotation.Path;
//import org.springframework.web.bind.annotation.Consumes;
//import org.springframework.web.bind.annotation.Method;
//import org.springframework.web.bind.annotation.Valid;
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
//import javax.json.JsonObject;
//import org.springframework.http.
import org.slf4j.Logger;

@RestController
//@CrossOrigin(origins = "http://localhost:3000")
//@CrossOrigin(origins = {"http://localhost:3000"})
@CrossOrigin(origins = "*")
public class UserController 
{
  @Autowired
  private UserService userService;

  @Autowired
  Logger logger;

  @RequestMapping(
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
    } catch (Exception e) 
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

  @RequestMapping(
    value = {"/register"},
    method = RequestMethod.POST,
    produces = "application/json",
    consumes = "application/json"
  )
  @ResponseBody
  public ResponseEntity<String> register(@RequestBody UserRegistrationDto registrationRequest) 
  {
	logger.error("Registration call");
    System.out.println(registrationRequest);

    try {
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

  //@RequestMapping(path="/rabbitmq/user", method={ RequestMethod.GET, RequestMethod.POST })
  //@RequestMapping(path="/rabbitmq/user", method={ RequestMethod.GET, RequestMethod.POST }, consumes="application/json")

	
  //@RequestMapping(path="/rabbitmq/user", method={ RequestMethod.GET, RequestMethod.POST }, consumes="text/plain")
  //public String rabbitUserPath(@RequestBody UserPathDto userPathDto) 
  @RequestMapping(
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
	  //logger.info("Get /rabbitmq/user request: " + userPathDto.toString());
	  logger.info("Get /rabbitmq/user request: " + username + ":" + password);
	  if (username.equals("guest")
	     && password.equals("guest"))
		 {
			 return "allow administrator";
		 }
	//  if (userPathDto.getUsername().equals("guest")
	//     && userPathDto.getPassword().equals("guest"))
	//	 {
	//		 return "allow administrator";
	//	 }

	  try 
	  {
		  // convert to standard login dto
		  //UserLoginDto loginDto = new UserLoginDto(userPathDto.getUsername(), userPathDto.getPassword());
		  UserLoginDto loginDto = new UserLoginDto(username, password);
		  loginJwt = userService.login(loginDto);
	  } catch (Exception e) 
	  {
		  return "deny";
	  }

	  //allow [list of tags] - (for user_path only) - allow access, and mark the user as an having the tags listed
	  return "allow ";
  }

  //vhost_path
  //username - the name of the user
  //vhost - the name of the virtual host being accessed
  //ip - the client ip address
  //Note that you cannot create arbitrary virtual hosts using this plugin; you can only determine whether your users can see / access the ones that exist.
  //@RequestMapping(path="/rabbitmq/vhost", method={ RequestMethod.GET, RequestMethod.POST })

  //public String rabbitVhostPath(@RequestBody VhostPathDto vhostPathDto) 
  @RequestMapping(
    path = {"/rabbitmq/vhost"},
    method = {RequestMethod.GET, RequestMethod.POST},
    produces = "application/x-www-form-urlencoded",
    consumes = "application/x-www-form-urlencoded"
  )
  public String rabbitVhostPath(
		@RequestParam("username")  String username, 
		@RequestParam("vhost")  String vhost, 
		@RequestParam("ip")  String ip
  ) 
  {
	  //logger.info("Get /rabbitmq/vhost request: " + vhostPathDto.toString());
	  logger.info("Get /rabbitmq/vhost request: " + username + " vhost: " + vhost + " ip: " + ip);
	  return "allow";
  }
	//resource_path
	//username - the name of the user
	//vhost - the name of the virtual host containing the resource
	//resource - the type of resource (exchange, queue, topic)
	//name - the name of the resource
	//permission - the access level to the resource (configure, write, read) - see the Access Control guide for their meaning
  //@RequestMapping(path="/rabbitmq/resource", method={ RequestMethod.GET, RequestMethod.POST })
  @RequestMapping(
    path = {"/rabbitmq/resource"},
    method = {RequestMethod.GET, RequestMethod.POST},
    produces = "application/x-www-form-urlencoded",
    consumes = "application/x-www-form-urlencoded"
  )
  public String rabbitResourcePath(
		@RequestParam("username")  String username, 
		@RequestParam("vhost")  String vhost, 
		@RequestParam("resource")  String resource,
		@RequestParam("name")  String name,
		@RequestParam("permission")  String permission
  )
  {
	  //logger.info("Get /rabbitmq/resource request: " + resourcePathDto.toString());
	  logger.info(
			  "Get /rabbitmq/resource request: " + 
			  "username: " + username +
			  "vhost: " + vhost +
			  "resource: " + resource +
			  "name: " + name +
			  "permission: " + permission
	  );

	  return "allow";
  }
	//
	//topic_path
	//username - the name of the user
	//vhost - the name of the virtual host containing the resource
	//resource - the type of resource (topic in this case)
	//name - the name of the exchange
	//permission - the access level to the resource (write or read)
	//routing_key - the routing key of a published message (when the permission is write) or routing key of the queue binding (when the permission is read)
	//See topic authorisation for more information about topic authorisation.
  //@RequestMapping(path="/rabbitmq/topic", method={ RequestMethod.GET, RequestMethod.POST })
  //public String rabbitTopicPath(@RequestBody TopicPathDto topicPathDto) 
  @RequestMapping(
    path = {"/rabbitmq/topic"},
    method = {RequestMethod.GET, RequestMethod.POST},
    produces = "application/x-www-form-urlencoded",
    consumes = "application/x-www-form-urlencoded"
  )
  public String rabbitTopicPath(
		@RequestParam("username")  String username, 
		@RequestParam("vhost")  String vhost, 
		@RequestParam("resource")  String resource,
		@RequestParam("name")  String name,
		@RequestParam("permission")  String permission,
		@RequestParam("routing_key")  String routing_key
  )
  {
	  logger.info(
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

//  {
//    System.out.println(registrationRequest);
//    //System.out.println(registrationRequest);
//    //HashMap<String, String> responseJson = new HashMap<>();
//    //String loginJwt;
//
//    //// read post
//    //String username = registrationRequest.getUsername();
//    //String email = registrationRequest.getEmail();
//    //String password = registrationRequest.getPassword();
//    //System.out.println("username: " + username);
//
//    //try 
//    //{
//    //  loginJwt = userService.register(username, email, password);
//    //} catch (Exception e) 
//    //{
//    //  logger.info("Failed jwt login for username: " + registrationRequest.getUsername());
//
//    //  return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//    //}
//    //logger.info("Returned jwt: '" + loginJwt + "' for user " + username);
//
//    //responseHeader.add("Authorization", loginJwt);
//
//    //responseJson.put("Auth", loginJwt);
//
//    //System.out.println(responseJson);
//    try {
//      String response = userService.register(registrationRequest);
//      HttpHeaders responseHeader = new HttpHeaders();
//      return new ResponseEntity<>(HttpStatus.OK);
//      //return ResponseEntity.ok()
//      //  .headers(null)
//      //  .body("{}");
//    }
//    catch (Exception e)
//    {
//			logger.info("Failed in register service: " + e);
//
//      HttpHeaders responseHeader = new HttpHeaders();
//      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//    }
//
//    //return ResponseEntity.ok()
//    //  .headers(responseHeader)
//    //  .body("{}");
//  }

//  @PostMapping("/register")
//  @ResponseStatus(code = HttpStatus.CREATED)
//  public void register(@RequestBody UserRegistrationDto newUserRequest) 
//  {
//    userService.register(newUserRequest);
//  }
}
//@PostMapping("/login")
//ResponseEntity<String> jwtResponse = ResponseEntity.header("Authorization", loginJwt);
//.contentType(TEXT_PLAIN)
//.body("some body");

//  .body("Response with header using ResponseEntity");
////return ResponseEntity.ok()
////  .headers(responseHeader)
////  .body("Response with header using ResponseEntity");
//ResponseEntity<String> jwtResponse = ResponseEntity.header("Authorization", loginJwt);
//RequestEntity.HeadersBuilder<?> head(String uriTemplate, Object... uriVariables);
//return new ResponseEntity<>(HttpStatus.OK);
//return ResponseEntity.created("/login").header("").body("");
//return responseJson;

//ResponseEntity responseHeaders = new HttpHeaders();
//responseHeaders.set("Authorization", loginJwt);

//System.out.println(new JSONObject(map));

////String username = (String) credentials.get("username");
////String password = (String) credentials.get("password");
//System.out.println("password: " + password);
//loginJwt = userService.login(userLogin.getUsername(), userLogin.getPassword());
//logger.info("Failed jwt login for username: " + credentials.get("username"));
//public Map<String, String> login(@RequestBody @Valid UserLoginDto userLogin) 
//public ResponseEntity<String> login(@RequestBody Map<String, String> credentials) 
//public ResponseEntity<String> login(@RequestBody @Valid UserLoginDto userLogin) 

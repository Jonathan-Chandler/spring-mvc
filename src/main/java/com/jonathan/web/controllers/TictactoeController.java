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

import org.springframework.messaging.simp.SimpMessagingTemplate;

//@CrossOrigin(origins = "http://localhost:3000/tictactoe/playerlist")
@Controller
@CrossOrigin(origins = "http://localhost:3000")
//@CrossOrigin(origins = "http://localhost:3000", allowedHeaders="*")
public class TictactoeController 
{
  @Autowired
  private TictactoeService tictactoeService;

  private final SimpMessagingTemplate simpMessagingTemplate;

  @Autowired
  Logger logger;

  public TictactoeController(SimpMessagingTemplate simpMessagingTemplate) 
  {
    this.simpMessagingTemplate = simpMessagingTemplate;
  }

  // handle messages from /tictactoe/playerlist stompClient.send("/tictactoe/playerlist", ...)
  @MessageMapping("/greetings")
  // send return value to /tictactoe/playerlist (stompClient.subscribe('/tictactoe/playerlist'))
  //@SendTo("/topic/messages")
  //public List<TictactoePlayerListDto> getMessages(String clientMessage)
  public void getMessages(String greeting)
  {
	  logger.info("client message");
	  logger.info("client message: " + greeting);
	  String text = "[" + Instant.now() + "]: " + greeting;
	  simpMessagingTemplate.convertAndSend("/topic/greetings", text);

	  //return new TestDto("test");
	  //return clientMessage;
	  //logger.info("get client message: " + clientMessage);
      //List<TictactoePlayerListDto> playerList = tictactoeService.getPlayerList();
	  //return playerList;
  }

  // handle messages from /tictactoe/playerlist stompClient.send("/tictactoe/playerlist", ...)
  @MessageMapping("/playerList")
  // send return value to /tictactoe/playerlist (stompClient.subscribe('/tictactoe/playerlist'))
  //@SendTo("/topic/messages")
  //public List<TictactoePlayerListDto> getMessages(String clientMessage)
  public void getPlayerList(String receive)
  {
	  logger.info("playerList message: " + receive);
      List<TictactoePlayerListDto> playerList = tictactoeService.getPlayerList();

	  simpMessagingTemplate.convertAndSend("/topic/playerList", playerList);

	  //return new TestDto("test");
	  //return clientMessage;
	  //logger.info("get client message: " + clientMessage);
      //List<TictactoePlayerListDto> playerList = tictactoeService.getPlayerList();
	  //return playerList;
  }


////  private final ExecutorService executor = Executors.newSingleThreadExecutor();
////
////  private void sleep(int seconds, SseEmitter sseEmitter) {
////    try {
////      Thread.sleep(seconds * 1000);
////    } catch (InterruptedException e) {
////      e.printStackTrace();
////      sseEmitter.completeWithError(e);
////    }
////  }
////
////  @GetMapping("/tictactoe/playerlist")
////  @CrossOrigin
////  public SseEmitter streamDateTime() {
////
////    //SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
////    SseEmitter sseEmitter = new SseEmitter();
////
////    sseEmitter.onCompletion(() -> logger.info("SseEmitter is completed"));
////    sseEmitter.onTimeout(() -> logger.info("SseEmitter is timed out"));
////    sseEmitter.onError((ex) -> logger.info("SseEmitter got error:", ex));
////
////	executor.execute(() -> 
////	{
////		try 
////		{
////			List<TictactoePlayerListDto> playerList = tictactoeService.getPlayerList();
////			sseEmitter.send(playerList);
////
////			//sleep(10, sseEmitter);
////		} catch (IOException e) {
////			e.printStackTrace();
////			sseEmitter.completeWithError(e);
////		}
////		sseEmitter.complete();
////	});
////	executor.shutdown();
////
////    //executor.execute(() -> {
////    //  for (int i = 0; i < 15000; i++) {
////    //    try {
////    //      List<TictactoePlayerListDto> playerList = tictactoeService.getPlayerList();
////    //      sseEmitter.send(playerList);
////
////    //      sleep(10, sseEmitter);
////    //    } catch (IOException e) {
////    //      e.printStackTrace();
////    //      sseEmitter.completeWithError(e);
////    //    }
////    //  }
////    //  sseEmitter.complete();
////    //});
////
////    logger.info("Controller exits");
////    return sseEmitter;
////  }
  //@RequestMapping(
  //  value = {"/tictactoe/playerlist"},
  //  method = RequestMethod.GET,
  //  produces = "application/json",
  //  consumes = "application/json"
  //)
  //@ResponseBody
////  public List<TictactoePlayerListDto> getPlayerList(@RequestHeader(value="Authorization") String authHeader)
////  {
////    logger.info("get auth header: " + authHeader);
////    
////
////    
////    //HashMap<String, String> responseJson = new HashMap<>();
////    //String loginJwt;
////
////    //try 
////    //{
////    //  loginJwt = userService.login(userLogin);
////    //} catch (Exception e) 
////    //{
////    //  return new ResponseEntity<>("Failed to get player list: " + e.getMessage(), HttpStatus.BAD_REQUEST);
////    //}
////
////    //HttpHeaders responseHeader = new HttpHeaders();
////    //responseHeader.add("Authorization", loginJwt);
////
////    //responseJson.put("Auth", loginJwt);
////    //System.out.println(responseJson);
////    
////    List<TictactoePlayerListDto> playerList = tictactoeService.getPlayerList();
////    //TictactoePlayerListDto player = new TictactoePlayerListDto("username", true, true);
////    //List<TictactoePlayerListDto> playerList = new ArrayList<TictactoePlayerListDto>();
////
////    return playerList;
////    // return ResponseEntity.ok()
////    //   .headers(responseHeader)
////    //   .body(playerList.toString());
////  }
//  @Autowired
//  private UserService userService;
//
//  @Autowired
//  private TictactoeService tictactoeService;
//
//  @Autowired
//  Logger logger;
//
//
//  @RequestMapping(
//    value = {"/login"},
//    method = RequestMethod.POST,
//    produces = "application/json",
//    consumes = "application/json"
//  )
//  @ResponseBody
//  public ResponseEntity<String> login(@RequestBody UserLoginDto userLogin) 
//  {
//    System.out.println(userLogin);
//    HashMap<String, String> responseJson = new HashMap<>();
//    String loginJwt;
//
//    // read post
//    String username = userLogin.getUsername();
//    String password = userLogin.getPassword();
//    System.out.println("username: " + username);
//
//    try 
//    {
//      loginJwt = userService.login(username, password);
//    } catch (Exception e) 
//    {
//      logger.info("Failed jwt login for username: " + userLogin.getUsername());
//
//      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//    }
//    logger.info("Returned jwt: '" + loginJwt + "' for user " + username);
//
//    HttpHeaders responseHeader = new HttpHeaders();
//    responseHeader.add("Authorization", loginJwt);
//
//    responseJson.put("Auth", loginJwt);
//
//    System.out.println(responseJson);
//    return ResponseEntity.ok()
//      .headers(responseHeader)
//      .body("{\"Authorization\": \"" + loginJwt + "\"}");
//  }
//
//  @RequestMapping(
//    value = {"/register"},
//    method = RequestMethod.POST,
//    produces = "application/json",
//    consumes = "application/json"
//  )
//
//  @ResponseBody
//  public ResponseEntity<String> register(@RequestBody UserRegistrationDto registrationRequest) 
//  {
//    System.out.println(registrationRequest);
//    HashMap<String, String> responseJson = new HashMap<>();
//    String loginJwt;
//
//    // read post
//    String username = registrationRequest.getUsername();
//    String email = registrationRequest.getEmail();
//    String password = registrationRequest.getPassword();
//    System.out.println("username: " + username);
//
//    try 
//    {
//      loginJwt = userService.register(username, email, password);
//    } catch (Exception e) 
//    {
//      logger.info("Failed jwt login for username: " + registrationRequest.getUsername());
//
//      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//    }
//    logger.info("Returned jwt: '" + loginJwt + "' for user " + username);
//
//    HttpHeaders responseHeader = new HttpHeaders();
//    responseHeader.add("Authorization", loginJwt);
//
//    responseJson.put("Auth", loginJwt);
//
//    System.out.println(responseJson);
//    return ResponseEntity.ok()
//      .headers(responseHeader)
//      .body("{\"Authorization\": \"" + loginJwt + "\"}");
//  }
//
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

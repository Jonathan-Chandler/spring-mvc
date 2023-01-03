import moment from 'moment'
import React, { useEffect, useCallback, useState } from "react";
import { useNavigate } from "react-router-dom";
import useAuth from '../../authentication/AuthProvider.tsx';
import { PLAYER_LIST_API_URL } from '../../../Constants';

//import Stomp from 'stompjs'
//import useWebSocket, { ReadyState } from 'react-use-websocket';

import { Stomp, Client } from '@stomp/stompjs';
//import SockJS from 'sockjs-client';

export default function TictactoePlayerList()
{
	const { username, token, isAuthenticated, getStompSession, playerList, tictactoeGame } = useAuth();
	const data = ["A", "B", "C", "D"]
    const [state, setState] = useState({
      convertedPlayerList: [],
      message: "",
      loadedTodos: false,
    });
    const navigate = useNavigate();

	useEffect(() =>
	{
		//setState({...state, convertedPlayerList: ["aoeu","oaeu"]})
		console.log("playerList: " + playerList);
		console.log("convertedPlayerList: " + state.convertedPlayerList);
		let newPlayerList = []
		//if( typeof playerList !== 'undefined' && playerList !== null )
		//{
		//	if( typeof playerList.availableUsers !== 'undefined' && playerList.availableUsers !== null )
		//	{
		//		//for (let i = 0; i < playerList.avalableUsers.length; i++)
		//		//{
		//		//	let currentPlayer = {"name": playerList.availableUsers[i], "requested": false, "requesting": false};

		//		//	if (playerList.requestedUsers.includes(currentPlayer.name))
		//		//		{
		//		//			console.log("includes requested");
		//		//			currentPlayer.requested = true;
		//		//		}

		//		//		if (playerList.requestingUsers.includes(currentPlayer.name))
		//		//			{
		//		//				console.log("includes requesting");
		//		//				currentPlayer.requesting = true;
		//		//			}
		//		//			console.log("Add player: " + JSON.stringify(currentPlayer));
		//		//			newPlayerList.push(currentPlayer)
		//		//}
		//	}
		//}
		console.log("newPlayerList: " + JSON.stringify(newPlayerList));
		//setState({...state, convertedPlayerList: playerList})
		//
	},[state, playerList])

	useEffect(() =>
	{
		// game has not ended with error or winner
		if (tictactoeGame.gameState === "X_PLAYER_MOVING" 
			|| tictactoeGame.gameState === "O_PLAYER_MOVING" 
			|| tictactoeGame.gameState === "STARTING")
		{
			console.log("Joining Game (gameState = " + tictactoeGame.gameState + ")");
			//navigate("/tictactoe/game/online")
		}
	},[tictactoeGame.gameState, navigate])

	const sendRequest = (name) => 
	{
		let stompSession = getStompSession();
		//const data = {message: name}
		const data = {requestType: 1, requestedUser: name, moveLocation: -1}
		//const data = "test";
		console.log("send request: " + JSON.stringify(data))

		// Additional headers
		//client.publish({
		let pass = token.split(' ')[1]
		const msg_destination = "/topic/from.user." + username
		//stompSession.send("/topic/user."+username, {}, JSON.stringify(data));
		stompSession.publish({
		  destination: msg_destination,
		  body: JSON.stringify(data),
		  //body: "{\"message\":\"data\"}",
		  headers: {
			  login: username, 
			  passcode: pass, 
			  "content-type":"application/json", 
			  "content-encoding":"UTF-8", 
			  "__TypeId__":"com.jonathan.web.frontend.RequestDto"
		  }
		});
		  //body: JSON.stringify(data),
	};

	const sendRequestDto = () => 
	{
		let stompSession = getStompSession();
		const data = {requestType: 0}
		//const data = "test";
		console.log("send " + data)

		// Additional headers
		//client.publish({
		let pass = token.split(' ')[1]
		const msg_destination = "/topic/from.user." + username
		//stompSession.send("/topic/user."+username, {}, JSON.stringify(data));
		stompSession.publish({
		  destination: msg_destination,
		  body: JSON.stringify(data),
		  //body: "{\"message\":\"data\"}",
		  headers: {
			  login: username, 
			  passcode: pass, 
			  "content-type":"application/json", 
			  "content-encoding":"UTF-8", 
			  "__TypeId__":"com.jonathan.web.frontend.RequestDto"
		  }
		});
		  //body: JSON.stringify(data),
		console.log("send body: " + JSON.stringify(data))
	};

	if (playerList && playerList.length >= 1)
	{
		return (
			<div>
				Player List
				<p />
				<div className="container">
					<table className="table">
						<tbody>
							<tr>
								<th>Name</th>
								<th>You Requested</th>
								<th>They Requested</th>
								<th>Send Request</th>
							</tr>
							{
								playerList.map((item) => (
									<tr key={item.username}>
										<td>{item.username}</td>
										<td>{item.myRequest ? "Yes" : "No"}</td>
										<td>{item.theirRequest ? "Yes" : "No"}</td>
										<td><button className="btn btn-success" onClick={() => sendRequest(item.username)}>Request</button></td>
									</tr>
								))
							}
						</tbody>
					</table>
				</div>
			</div>
		);
	}
	else
	{
		return (
			<div>
				Player List
				<p />
				<div className="container">
					<p />No other players are online.
				</div>
			</div>
		);
	}

//				<p /><button className="btn btn-success" onClick={() => sendRequest("test_user123")}>req test_user123</button>
//				<p /><button className="btn btn-success" onClick={() => sendRequest("test_user1234")}>req test_user1234</button>
//				<p /><button className="btn btn-success" onClick={() => sendRequestDto()}>send requestDto</button>
}
//				<p /><button className="btn btn-success" onClick={sendMessage}>Message</button>
////				<div className="container">
////					<table className="table">
////						<thead>
////							<tr>
////								<th>Username</th>
////							</tr>
////						</thead>
////						<tbody>
////							{
////								createTable()
////							}
////						</tbody>
////					</table>
////				</div>
////			<p /><button className="btn btn-success" onClick={sendMessage}>Message</button>
	//const {sendMessage, lastMessage, readyState} = useWebSocket(socketUrl)

	//////useEffect(() => {
	//////}, []);

	//////const onMessage = useCallback((d) =>
	//////{
	//////	try 
	//////	{
	//////		var parsed = JSON.parse(d.body);
	//////		var message = parsed.message;
	//////		setStompMessage(stompMessage.push(message));
	//////		setPlayerList((playerList) => [...playerList, parsed]);
	//////	}
	//////	catch (err)
	//////	{
	//////		console.log("Failed to get message: " + err);
	//////	}
	//////},[stompMessage, playerList])

	//////useEffect(() => 
	//////{
	//////	const ws = new WebSocket(socketUrl);
	//////	const client = Stomp.over(ws);
	//////	var on_error;

	//////	// subscribe to stomp topic on web socket connection
	//////	var on_connect = function(x) 
	//////	{
	//////		client.subscribe(
	//////			stompTopic, 
	//////			onMessage, 
	//////			stomp_subscribe_header);
	//////	};

	//////	var on_close = function()
	//////	{
	//////		console.log("close connection");
	//////	};

	//////	if (debugStompEnabled)
	//////	{
	//////		// error logging to console
	//////		on_error = function(err) 
	//////		{
	//////			console.log("STOMP Error: " + err);
	//////		};
	//////	}
	//////	else
	//////	{
	//////		// disable error logging
	//////		on_error = null;
	//////	}

	//////	if (debugWsEnabled)
	//////	{
	//////		// web socket debug logging
	//////		client.debug = function(msg) 
	//////		{
	//////			console.log("client.debug: " + msg);
	//////		}
	//////	}
	//////	else
	//////	{
	//////		// disable debug logging
	//////		client.debug = function(){};
	//////	}

	//////	// connect using guest credentials in header
	//////	client.connect('test_user123', 'password123', on_connect, on_error, '/');

	//////	// set ws/stomp session after initialized
	//////	setWebSockSession(ws);
	//////	setStompSession(client);
	//////},[debugWsEnabled, debugStompEnabled, socketUrl, stompTopic, onMessage, stomp_headers, stomp_subscribe_header])

	//////// send message to stomp topic
	//////const sendMessage = () => 
	//////{
	//////	const data = {message: "test_message"}
	//////	console.log("send " + data)

	//////	//stompSession.send(stompTopic, {}, JSON.stringify(data));
	//////	stompSession.send(stompTopic, stomp_subscribe_header, JSON.stringify(data));
	//////};
	//const onMessage = useCallback((d) =>
	//{
	//	try 
	//	{
	//		var parsed = JSON.parse(d.body);
	//		var message = parsed.message;
	//		setStompMessage(stompMessage.push(message));
	//		setPlayerList((playerList) => [...playerList, parsed]);
	//	}
	//	catch (err)
	//	{
	//		console.log("Failed to get message: " + err);
	//	}
	//},[stompMessage, playerList])

	//useEffect(() => 
	//{
	//	const ws = new WebSocket(socketUrl);
	//	const client = Stomp.over(ws);
	//	var on_error;

	//	// subscribe to stomp topic on web socket connection
	//	var on_connect = function(x) 
	//	{
	//		client.subscribe(
	//			stompTopic, 
	//			onMessage, 
	//			stomp_subscribe_header);
	//	};

	//	var on_close = function()
	//	{
	//		console.log("close connection");
	//	};

	//	if (debugStompEnabled)
	//	{
	//		// error logging to console
	//		on_error = function(err) 
	//		{
	//			console.log("STOMP Error: " + err);
	//		};
	//	}
	//	else
	//	{
	//		// disable error logging
	//		on_error = null;
	//	}

	//	if (debugWsEnabled)
	//	{
	//		// web socket debug logging
	//		client.debug = function(msg) 
	//		{
	//			console.log("client.debug: " + msg);
	//		}
	//	}
	//	else
	//	{
	//		// disable debug logging
	//		client.debug = function(){};
	//	}

	//	// connect using guest credentials in header
	//	client.connect('test_user123', 'password123', on_connect, on_error, '/');

	//	// set ws/stomp session after initialized
	//	setWebSockSession(ws);
	//	setStompSession(client);
	//},[debugWsEnabled, debugStompEnabled, socketUrl, stompTopic, onMessage, stomp_headers, stomp_subscribe_header])

	//// send message to stomp topic
	//const sendMessage = () => 
	//{
	//	const data = {message: "test_message"}
	//	console.log("send " + data)

	//	//stompSession.send(stompTopic, {}, JSON.stringify(data));
	//	stompSession.send(stompTopic, stomp_subscribe_header, JSON.stringify(data));
	//};
	////const onMessage = useCallback((d) =>
	////{
	////	try 
	////	{
	////		var parsed = JSON.parse(d.body);
	////		var message = parsed.message;
	////		setStompMessage(stompMessage.push(message));
	////		setPlayerList((playerList) => [...playerList, parsed]);
	////	}
	////	catch (err)
	////	{
	////		console.log("Failed to get message: " + err);
	////	}
	////},[stompMessage])
	//////},[stompMessage, playerList])

	////useEffect(() => 
	////{
	////	//const client = new StompJs.Client({
	////	const client = new Client({
	////		brokerURL: 'ws://172.17.0.3:61611/ws',
	////		connectHeaders: {
	////			login: 'test_user123',
	////			passcode: 'password123',
	////			//'heart-beat': '4000,4000'
	////		},

	////		debug: function (str) 
	////		{
	////			console.log(str);
	////		},
	////		reconnectDelay: 5000,
	////		//heartbeatIncoming: 4000,
	////		//heartbeatOutgoing: 4000,
	////	});

	////	client.onConnect = function (frame) 
	////	{
	////		console.log("onConnect")

	////		// all subscribes must be done here for reconnect
	////  		client.subscribe(
	////  			stompTopic, 
	////  			onMessage, 
	////  			stomp_headers
	////		);
	////	};

	////	client.onStompError = function (frame) {
	////		// error encountered at Broker
	////		console.log('Broker reported error: ' + frame.headers['message']);
	////		console.log('Additional details: ' + frame.body);
	////	};

	////	// init and save stomp session
	////	client.activate();
	////	setStompSession(client);
	////},[stompTopic, onMessage, stomp_headers])

	////// send message to stomp topic
	////const sendMessage = () => 
	////{
	////	const data = {message: "test_message"}
	////	console.log("send " + data)

	////	// Additional headers
	////	//client.publish({
	////	stompSession.publish({
	////	  destination: '/topic/hello',
	////	  body: JSON.stringify(data),
	////	  headers: stomp_headers,
	////	});
	////};



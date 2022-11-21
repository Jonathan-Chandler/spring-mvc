import moment from 'moment'
import React, { useEffect, useCallback, useState } from "react";
import { useNavigate } from "react-router-dom";
import useAuth from '../../authentication/AuthProvider.tsx';
import { PLAYER_LIST_API_URL } from '../../../Constants';

//import Stomp from 'stompjs'
//import useWebSocket, { ReadyState } from 'react-use-websocket';

import { Stomp, Client } from '@stomp/stompjs';
//import SockJS from 'sockjs-client';

export default function TictactoeAmqpTest()
{
	////const [socketUrl, setSocketUrl] = useState('ws://172.17.0.3:61611/ws');
	////const [stompTopic, setStompTopic] = useState('/topic/hello');
	////const [stomp_subscribe_header] = useState({login: 'test_user123', passcode: 'password124'});
	//////const [webSockSession, setWebSockSession] = useState(null);
	////const [stompSession, setStompSession] = useState(null);
	////const [playerList, setPlayerList] = useState([{}]);
	////const [debugStompEnabled, setDebugStompEnabled] = useState(true);
	////const [debugWsEnabled, setDebugWsEnabled] = useState(false);
	////const [stompMessage, setStompMessage] = useState([]);
	const [stomp_headers] = useState({login: 'test_user123', passcode: 'password123'});
	const { isAuthenticated, getStompSession, playerList } = useAuth();

	const sendMessage = () => 
	{
		let stompSession = getStompSession();
		const data = {message: "test_message"}
		console.log("send " + data)

		// Additional headers
		//client.publish({
		stompSession.publish({
		  destination: '/topic/hello',
		  body: JSON.stringify(data),
		  headers: stomp_headers,
		});
	};

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

	return (
		<div>
			amqp test
			<p />
				<div className="container">
					<table className="table">
						<thead>
							<tr>
								<th>Username</th>
							</tr>
						</thead>
						<tbody>
							{
								playerList && playerList.map(
									player =>
										<tr key={player.id}>
											<td>{player.message}</td>
										</tr>
								)
							}
						</tbody>
					</table>
				</div>
			<p /><button className="btn btn-success" onClick={sendMessage}>Message</button>
		</div>
	);
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

}

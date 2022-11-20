import moment from 'moment'
import React, { useEffect, useCallback, useState } from "react";
import { useNavigate } from "react-router-dom";
import useAuth from '../../authentication/AuthProvider.tsx';
import { PLAYER_LIST_API_URL } from '../../../Constants';
//import amqp from 'amqplib/callback_api'
//import amqplib from 'amqplib/callback_api'
//import { Stomp, Client } from '@stomp/stompjs';

//import SockJS from 'sockjs-client';
import Stomp from 'stompjs'

export default function TictactoeAmqpTest()
{
	//const [socketUrl, setSocketUrl] = useState('ws://127.0.0.1:61611/ws');
	const [socketUrl, setSocketUrl] = useState('ws://172.17.0.3:61611/ws');
	const [stompTopic, setStompTopic] = useState('/topic/hello');
	const [stomp_subscribe_header] = useState({login: 'test_user123', passcode: 'password124'});
	const [webSockSession, setWebSockSession] = useState(null);
	const [stompSession, setStompSession] = useState(null);
	//const [playerList, setPlayerList] = useState([]);
	const [playerList, setPlayerList] = useState([{}]);
	//const [stomp_headers] = useState({login: 'guest', passcode: 'guest'});
	const [stomp_headers] = useState({login: 'test_user123', passcode: 'password123'});
	//const [stomp_send_header] = useState({login: 'test_user123', passcode: 'password123', destination: '/topic/hello'});
	//const stomp_send_header = {login: 'guest', passcode: 'guest', destination: '/topic/hello'};
	//const [debugWsEnabled, setDebugWsEnabled] = useState(true);
	const [debugStompEnabled, setDebugStompEnabled] = useState(true);
	const [debugWsEnabled, setDebugWsEnabled] = useState(false);
	//const [debugStompEnabled, setDebugStompEnabled] = useState(false);
	const [stompMessage, setStompMessage] = useState([]);
	
	// /exchange/exchange_name[/routing_key].

	const onMessage = useCallback((d) =>
	{
		//console.log("d.body: " + d.body);
		try {
			console.log(d);
			//var entire_parse = JSON.parse(d);
			//console.log(entire_parse);
			//if (d.headers)
			//{
			//	var header_parse = JSON.parse(d.headers);
			//	console.log("header: " + header_parse);
			//}
			//else
			//{
			//	console.log("empty header");
			//}
			var parsed = JSON.parse(d.body);
			var message = parsed.message;
			setStompMessage(stompMessage.push(message));
			console.log("in useCallback onMessage: " + message);
			//playerList.push({"message":message});
			setPlayerList((playerList) => [...playerList, parsed]);
			//playerList.push(parsed)
			console.log("playerList: " + JSON.stringify(playerList))
		}
		catch (err)
		{
			console.log("Failed to get message: " + err);
		}
	},[stompMessage, playerList])

	useEffect(() => 
	{
		const ws = new WebSocket(socketUrl);
		const client = Stomp.over(ws);
		var on_error;

		// subscribe to stomp topic on web socket connection
		var on_connect = function(x) 
		{
			client.subscribe(
				stompTopic, 
				onMessage, 
				stomp_subscribe_header);
		};

		var on_close = function()
		{
			console.log("close connection");
		};

		if (debugStompEnabled)
		{
			// error logging to console
			on_error = function(err) 
			{
				console.log("STOMP Error: " + err);
			};
		}
		else
		{
			// disable error logging
			on_error = null;
		}

		if (debugWsEnabled)
		{
			// web socket debug logging
			client.debug = function(msg) 
			{
				console.log("client.debug: " + msg);
			}
		}
		else
		{
			// disable debug logging
			client.debug = function(){};
		}

		// connect using guest credentials in header
		//client.connect(stomp_headers, on_connect, on_error);
		//client.connect('guest', 'guest', on_connect, on_error, '/');
		client.connect('test_user123', 'password123', on_connect, on_error, '/');

		// set ws/stomp session after initialized
		setWebSockSession(ws);
		setStompSession(client);

	},[debugWsEnabled, debugStompEnabled, socketUrl, stompTopic, onMessage, stomp_headers, stomp_subscribe_header])

	// send message to stomp topic
	const sendMessage = () => 
	{
		const data = {message: "test_message"}
		console.log("send " + data)

		//stompSession.send(stompTopic, {}, JSON.stringify(data));
		stompSession.send(stompTopic, stomp_subscribe_header, JSON.stringify(data));
	};
//{stompMessage}

//				<table className="table">
//					<thead>
//						<tr>
//							<th>Username</th>
//							<th>IsRequesting</th>
//							<th>YouRequested</th>
//							<th>InitiateRequest</th>
//						</tr>
//					</thead>
//					<tbody>
//			{
//				playerList && playerList.map(
//					player =>
//						<tr key={player.username}>
//							<td>{player.username}</td>
//							<td>{player.myRequest ? "Yes" : "No"}</td>
//							<td>{player.theirRequest ? "Yes" : "No"}</td>
//						</tr>
//				)
//			}
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

//import { AMQPClient } from '@cloudamqp/amqp-client';
//import { AMQPWebSocketClient } from 'amqp-websocket-client.mjs'
//import { AMQPClient, AMQPWebSocketClient } from '@cloudamqp/amqp-client';
//'amqp-websocket-client.mjs'

//var amqp = require('amqplib/callback_api');

//var ws = new WebSocket("ws://localhost:61613/hello");
////var ws = new SockJS('http://127.0.0.1:15672/stomp')
//var client = Stomp.over(ws)
//client.connectHeaders = {
//	login: 'guest',
//	passcode: 'guest',
//};
//
//client.debug = onDebug;
//
//function onDebug(m)
//{
//	console.log("STOMP_DEBUG: " + m);
//}
//
//function onConnect() {
//	console.log("connected")
//	//client.subscribe('/topic/messages', message => {
//	client.subscribe('/hello', message => {
//		console.log(message);
//		//setState({mess: message.body});
//	});
//}
//
//function onError() {
//	console.log("Error")
//}
//
//client.connect('guest', 'guest', onConnect, onError, '/');
//
		// (Object) subscribe(destination, callback, headers = {})
		//client.subscribe(
		//	stompTopic, 
		//	function(d) {
		//		var p = JSON.parse(d.body);
		//		console.log("useEffect onMessage: " + p);
		//		setStompMessage(stompMessage, p);
		//	}, 
		//	stomp_subscribe_header);
		//	//onMessage,
		// (Object) subscribe(destination, callback, headers = {})

	//async function getMessage(message)
	//{
	//	console.log(message);
	//	return message;
	//}

	//// amqp-connection-manager
	//// rabbitmq-client

	//function errorCallback(e) {
	//	console.log("error: " + e)
	//}

	//function closeEventCallback() {
	//	console.log("closed")
	//}

	//function subscribeCallback(msg)
	//{
	//	console.log("subscribe: " + msg)
	//}

	//async function refreshPlayerListClicked()
	//{
	//	// req https
	//	//var ws = new WebSocket('wss://127.0.0.1:61612/ws');

	//	// using rabbitmq web_stomp
	//	var ws = new WebSocket('ws://127.0.0.1:61611/ws');

	//	var _client = Stomp.over(ws);

	//	// STOMP debug logging
	//	_client.debug = function(e) {
	//		console.log("debug: " + e);
	//	}

	//	var send = function(data) {
	//		console.log("send " + data)
	//		_client.send("/topic/hello", {}, data);
	//		//_client.subscribe("/topic/hello", subscribeCallback);
	//	};
	//	var on_connect = function(x) {
	//		var id = _client.subscribe('/topic/hello', function(d) {
	//			var p = JSON.parse(d.body);
	//			console.log(p);
	//		})
	//	};
	//	var on_error = function(x) {
	//		console.log('error: ' + x)
	//	};

	//	_client.connect('guest', 'guest', on_connect, on_error, '/');

	//	//_client.connect();
	//	//await _client.connect('guest', 'guest', connectCallback, errorCallback, closeEventCallback, 'host-string');
	//	//var headers = {
	//	//	login: 'guest',
	//	//	passcode: 'guest',
	//	//	'client-id': 'client-id',
	//	//};
	//	//await _client.connect(headers, connectCallback, errorCallback, closeEventCallback);
	//	//await _client.connect('guest', 'guest', connectCallback, errorCallback, closeEventCallback, 'host-string');
	//	//_client.subscribe("/topic/hello", subscribeCallback);
	//	//var subscription = _client.subscribe("/topic/hello", subscribeCallback);

	//	//var tx = _client.begin();
	//	//// send the message in a transaction
	//	//_client.send("/topic/hello", {transaction: tx.id}, "message in a transaction");
	//	//// commit the transaction to effectively send the message
	//	//tx.commit();
	//	//client.connect(login, passcode, connectCallback, errorCallback, closeEventCallback);
	//}

//		<p /><button className="btn btn-success" onClick={refreshPlayerListClicked}>Refresh</button>
}

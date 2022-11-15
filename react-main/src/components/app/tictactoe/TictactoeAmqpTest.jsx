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
	const [socketUrl, setSocketUrl] = useState('ws://127.0.0.1:61611/ws');
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

	//async function refreshPlayerListClicked()
	//{
	//	//var _client = Stomp.client('ws://localhost:61613/topic/hello');
	//	//var _client = Stomp.client('ws://localhost:61613/ws');
	//	//var _client = Stomp.client('wss://localhost:61614/ws');
	//	//var _client = Stomp.client('ws://localhost:15672/ws');
	//	//var _client = Stomp.client('ws://localhost:5672/ws');
	//	var _client = Stomp.client('ws://localhost:25672/ws');
	//	_client.heartbeat.outgoing = 2000; // client will send heartbeats every 20000ms
	//	_client.heartbeat.incoming = 0;     // client does not want to receive heartbeats
	//	_client.reconnect_delay = 5000;
	//	

	//	var connectCallback = function() {
	//		console.log("connected")
	//		_client.subscribe("/topic/hello", subscribeCallback);
	//	}

	//	//_client.connect();
	//	//await _client.connect('guest', 'guest', connectCallback, errorCallback, closeEventCallback, 'host-string');
	//	var headers = {
	//		login: 'guest',
	//		passcode: 'guest',
	//		'client-id': 'client-id',
	//	};
	//	await _client.connect(headers, connectCallback, errorCallback, closeEventCallback);
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

	//function refreshPlayerListClicked()
	//{
	//	var _client = new Client();
	//	//var headerValues = {}
	//	var headerValues = {
	//		'vhost': '/',
	//		'login': 'guest',
	//		'passcode': 'guest',
	//		'durable': 'true',
	//		'auto-delete': 'false',
	//	}

	//	_client.configure({
	//		//brokerURL: 'amqp://localhost:5672/',
	//		//brokerURL: 'ws://localhost:5672/stomp',
	//		//brokerURL: 'ws://localhost:15672/',
	//		//brokerURL: 'ws://localhost:61613/',
	//		//brokerURL: 'ws://localhost:61613/ws',
	//		//brokerURL: 'ws://localhost:61613',
	//		brokerURL: 'ws://localhost:61613/topic/hello',
	//		//brokerURL: 'ws://localhost:5672/',
	//		//brokerURL: 'ws://localhost:25672/',
	//		connectHeaders: headerValues,
	//		onConnect: () => 
	//		{
	//			//'/spring-boot', 
	//			//'/topic/spring-boot', 
	//			console.log("subscribe test")
	//			_client.subscribe
	//			(
	//				'hello', 
	//				function playerListMessageHandler(message) {
	//					console.log("message: " + message);
	//				},
	//				headerValues,
	//			)
	//		},
	//		onStompError: (frame) => {
	//			console.log("stompError: " + frame);
	//		},
	//		// debug messages
	//		debug: (str) => {
	//			console.log(new Date(), str);
	//		}
	//	});

	//	console.log("start activate")
	//	_client.activate();
	//	setStompSession(_client);
	//}


	//function refreshPlayerListClicked() {
	//	//var sock = new SockJS('http://127.0.0.1:15672')
	//	//var sock = new SockJS('http://127.0.0.1:61613')
	//	//var sock = new SockJS('http://127.0.0.1:15672')

	//	//var sock = new SockJS('http://localhost:15672/stomp')

	//	//var sock = new SockJS('http://localhost:15672')

	//	//var sock = new SockJS('http://localhost:5672')

	//	//var sock = new SockJS('http://localhost:61613') 

	//	const client = new Stomp.Client({
	//		brokerURL: 'ws://localhost:61613/ws',
	//		connectHeaders: {
	//			login: 'guest',
	//			passcode: 'guest',
	//		},
	//		debug: function (str) {
	//			console.log(str);
	//		},
	//		reconnectDelay: 5000,
	//		heartbeatIncoming: 4000,
	//		heartbeatOutgoing: 4000,
	//	});

	//	// Fallback code
	//	if (typeof WebSocket !== 'function') {
	//		// For SockJS you need to set a factory that creates a new SockJS instance
	//		// to be used for each (re)connect
	//		client.webSocketFactory = function () {
	//			// Note that the URL is different from the WebSocket URL
	//			return new SockJS('http://localhost:15674/stomp');
	//		};
	//	}

	//	client.onConnect = function (frame) {
	//		// Do something, all subscribes must be done is this callback
	//		// This is needed because this will be executed after a (re)connect
	//	};

	//	client.onStompError = function (frame) {
	//		// Will be invoked in case of error encountered at Broker
	//		// Bad login/passcode typically will cause an error
	//		// Complaint brokers will set `message` header with a brief message. Body may contain details.
	//		// Compliant brokers will terminate the connection after any error
	//		console.log('Broker reported error: ' + frame.headers['message']);
	//		console.log('Additional details: ' + frame.body);
	//	};

	//	client.activate();
	//}

	//function refreshPlayerListClicked()
	//{
	//	// await data from amqp queue
	//	//var client = new Client();
	//	//var ws = new WebSocket('ws://localhost:15672/ws')
	//	//var ws = new WebSocket('ws://127.0.0.1:61613/ws')
	//	var ws = new WebSocket('ws://127.0.0.1:61613/ws')
	//	//var ws = new WebSocket('ws://172.17.0.2:61613/ws')
	//	//var ws = new WebSocket('ws://172.17.0.2:15672/ws')
	//	const headers = {
	//		'login': 'guest',
	//		'passcode': 'guest',
	//		'durable': 'true',
	//		'auto-delete': 'false',
	//	}
	//	var stompClient = Stomp.over(ws)
	//	stompClient.connect(headers, function stompConnect(frame)
	//		{
	//			console.log("connected")
	//			const subscription = stompClient.subscribe('/spring-boot', getMessage)
	//			console.log("subscription:" + subscription)
	//			return subscription
	//		});
	//}

//	function refreshPlayerListClicked() {
//		//var sock = new SockJS('http://127.0.0.1:15672')
//		//var sock = new SockJS('http://127.0.0.1:61613')
//		//var sock = new SockJS('http://127.0.0.1:15672')
//
//		// 404 - Object not found
//		//var sock = new SockJS('http://localhost:15672/stomp')
//
//		//// err 404
//		//var sock = new SockJS('http://localhost:15672')
//
//		//// ERR_INVALID_HTTP_RESPONSE
//		//var sock = new SockJS('http://localhost:5672')
//
//		// connection_reset
//		//var sock = new SockJS('http://localhost:61613') 
//		var sock = new SockJS('http://localhost:61613/ws') 
//
//		sock.onopen = function() {
//			console.log("open");
//			//sock.send("test");
//			//let _client = Stomp.over(sock)
//			//_client.connect(
//			//	{login:'guest',passcode:'guest'}, 
//			//	function onConnect() {
//			//		console.log("connected, try subscribe");
//			//		_client.subscribe('/hello', message => {console.log(message)})
//			//	},
//			//	function onError(e){console.log("Error: " + e);}, 
//			//	'/'
//			//);
//		}
//		sock.onmessage = function(e) {
//			console.log("onmessage: " + e.data);
//			//sock.send("test");
//		}
//		sock.onclose = function() {
//			console.log("onclose");
//			//sock.send("test");
//		}
//		sock.error = function(e) {
//			console.log("onerror: " + e);
//			//sock.send("test");
//		}
//	}

	//const clientConnect = () => {
	//	console.log("connected");
	//	//setConnected(true)
	//}

	//const clientDisconnect = () => {
	//	console.log("disconnected");
	//	//setConnected(false)
	//}

	//const onMessageReceive = (msg, topic) => {
	//	console.log("topic: " + topic)
	//	console.log("msg:" + msg)
	//	//setPlayerList(msg)
	//}
  
	//function refreshPlayerListClicked()
	//{
	//	const wsclient = Stomp.overWS("ws://localhost:61613/stomp");

	//	////const client = new StompJs.Client(
	//	//const client = new Stomp.overWS(
	//	//	{
	//	//		brokerURL: 'ws://localhost:8080/ws',
	//	//		connectHeaders: {
	//	//		},
	//	//		debug: function (str) {
	//	//			console.log(str);
	//	//		},
	//	//		reconnectDelay: 5000,
	//	//		heartbeatIncoming: 3000,
	//	//		heartbeatOutgoing: 3000,
	//	//	});

	//	//client.onConnect = function(frame) {
	//	//	// subscribe
	//	//};

	//	//client.onStompError = function(frame) {
	//	//	console.log('Broker reported error: ' + frame.headers['message']);
	//	//	console.log('Additional details: ' + frame.body);
	//	//};
	//	//client.activate();
	//}

	//function refreshPlayerListClicked()
	//{

	//	//let newSocket = new WebSocket("ws://localhost:61613/stomp");
	//	//let newSocket = new WebSocket("ws://localhost:61613/stomp");
	//	let newSocket = new WebSocket("ws://localhost:61613");
	//	let _client = Stomp.overTcp(newSocket)
	//	_client.connect(
	//		{login:'guest',passcode:'guest'}, 
	//		function onConnect() {
	//			console.log("connected, try subscribe");
	//			_client.subscribe('/hello', message => {console.log(message)})
	//		},
	//		function onError(e){console.log("Error: " + e);}, 
	//		'/'
	//	);

	//	//setStompSession(_client);
	//}



	//async function refreshPlayerListClicked()
	//{

	//	let stompClient

	//	//var ws = new WebSocket('ws://localhost:15672/ws')
	//	//var ws = new WebSocket('ws://localhost:15672/ws')
	//	//var ws = new WebSocket('ws://guest:guest@localhost:61613/stomp')
	//	//var ws = new WebSocket('ws://guest:guest@localhost:61613/ws')
	//	//var ws = new WebSocket('ws://localhost:61613/ws')
	//	//var ws = new WebSocket('ws://localhost:61613/ws')

	//	// rabbitmq ex
	//	var ws = new WebSocket('ws://127.0.0.1:15674/ws')

	//	const headers = {
	//		login: 'guest123',
	//		passcode: 'guest123',
	//		'heart-beat': '0,0',
	//		'durable': 'true',
	//		'auto-delete': 'false'
	//	}

	//	var on_connect = function() {
	//		console.log('connected');
	//	};
	//	var on_error =  function(e) {
	//		console.log('error: ' + e);
	//	};

	//	stompClient = Stomp.over(ws)
	//	stompClient.connect('guest123', 'guest123', on_connect, on_error, '/');

	//	//stompClient.connect(headers , function(frame){
	//	//	console.log('Connected')
	//	//	const subscription = stompClient.subscribe('/queue/myQueue', function(message){
	//	//		console.log(message)
	//	//	})
	//	//})

	//}

	//async function refreshPlayerListClicked()
	//{
	//	//async function run() {
	//		try {
	//			//const amqp = new AMQPClient("ws://127.0.0.1:61613/ws")
	//			//const amqp = new AMQPClient("http://127.0.0.1:5672/ws")
	//			const amqp = new AMQPClient("amqp://127.0.0.1:5672/ws")
	//			const conn = await amqp.connect()
	//			const ch = await conn.channel()
	//			const q = await ch.queue()
	//			const consumer = await q.subscribe({noAck: true}, async (msg) => {
	//				console.log(msg.bodyToString())
	//				await consumer.cancel()
	//			})
	//			await q.publish("Hello World", {deliveryMode: 2})
	//			await consumer.wait() // will block until consumer is canceled or throw an error if server closed channel/connection
	//			await conn.close()
	//		} catch (e) {
	//			console.error("ERROR", e)
	//			e.connection.close()
	//			//setTimeout(run, 1000) // will try to reconnect in 1s
	//		}
	//	//}

	//	//run()
	//}

	//async function refreshPlayerListClicked()
	//{
	//	console.log("refresh");
	//	//const textarea = document.getElementById("textarea")
	//	//const input = document.getElementById("message")

	//	//const tls = window.location.scheme === "https:"
	//	const url = `amqp://127.0.0.1:61613`
	//	//const url = `amqp://127.0.0.1:15672`
	//	//const url = `ws://127.0.0.1:5672`
	//	const amqp = new AMQPWebSocketClient(url, "/", "guest", "guest")

	//	async function start() {
	//		try {
	//			const conn = await amqp.connect()
	//			const ch = await conn.channel()
	//			attachPublish(ch)
	//			const q = await ch.queue("")
	//			await q.bind("amq.fanout")
	//			const consumer = await q.subscribe({noAck: false}, (msg) => {
	//				console.log(msg)
	//				//textarea.value += msg.bodyToString() + "\n"
	//				console.log(msg.bodyToString())
	//				msg.ack()
	//			})
	//		} catch (err) {
	//			console.error("Error", err, "reconnecting in 1s")
	//			disablePublish()
	//			setTimeout(start, 1000)
	//		}
	//	}

	//	async function attachPublish(ch) {
	//		//document.forms[0].onsubmit = async (e) => {
	//			//e.preventDefault()
	//			try {
	//				//await ch.basicPublish("amq.fanout", "", input.value, { contentType: "text/plain" })
	//				await ch.basicPublish("amq.fanout", "", { contentType: "text/plain" })
	//			} catch (err) {
	//				console.error("Error", err, "reconnecting in 1s")
	//				disablePublish()
	//				setTimeout(start, 1000)
	//			}
	//			//input.value = ""
	//		//}
	//	}

	//	function disablePublish() {
	//		//document.forms[0].onsubmit = (e) => { alert("Disconnected, waiting to be reconnected") }
	//		alert("Disconnected, waiting to be reconnected")
	//	}

	//	start()
	//}


	//useEffect(() => 
	//{
	//	if (initialLoad)
	//	{
	//		setInitialLoad(false);
	//		console.log("initial load")
	//		amqp.connect('amqp://localhost:15672', function(error0, connection) {
	//			if (error0) {
	//				throw error0;
	//			}
	//			connection.createChannel(function(error1, channel) {
	//				if (error1) {
	//					throw error1;
	//				}
	//				var queue = 'rpc_queue';

	//				channel.assertQueue(queue, {
	//					durable: false
	//				});
	//				channel.prefetch(1);
	//				console.log(' [x] Awaiting RPC requests');
	//				channel.consume(queue, function reply(msg) {
	//					console.log(msg)
	//					//var n = parseInt(msg.content.toString());

	//					//console.log(" [.] fib(%d)", n);


	//					//channel.sendToQueue(msg.properties.replyTo,
	//					//	Buffer.from(r.toString()), {
	//					//		correlationId: msg.properties.correlationId
	//					//	});

	//					channel.ack(msg);
	//				});
	//			});
	//		});
	//	}
	//},[])

	//useEffect(() => 
	//{
	//	if (initialLoad)
	//	{
	//		setInitialLoad(false);
	//		console.log("initial load")

	//		var _client = new Client();
	//		var headerValues = {
	//			'login': 'guest',
	//			'passcode': 'guest',
	//			'durable': 'true',
	//			'auto-delete': 'false',
	//		}

	//		_client.configure({
	//			//brokerURL: 'ws://localhost:15672/stomp',
	//			//brokerURL: 'ws://localhost:61613/ws',
	//			brokerURL: 'ws://localhost:61613',
	//			connectHeaders: headerValues,
	//			onConnect: () => 
	//			{
	//				//'/spring-boot', 
	//				//'/topic/spring-boot', 
	//				console.log("subscribe test")
	//				_client.subscribe
	//				(
	//					'hello', 
	//					function playerListMessageHandler(message) {
	//						console.log("message: " + message);
	//					},
	//					headerValues,
	//				)
	//			},
	//			onStompError: (frame) => {
	//				console.log("stompError: " + frame);
	//			},
	//			// debug messages
	//			debug: (str) => {
	//				console.log(new Date(), str);
	//			}
	//		});

	//		console.log("start activate")
	//		_client.activate();
	//		setStompSession(_client);
	//	}
	//}, [initialLoad, stompSession])

//	function onConnect() {
//		console.log("connected")
////				client.subscribe('/topic/messages', message => {
////					console.log(message);
////					//setState({mess: message.body});
////				});
//	}
//
//	function onError() {
//		console.log("connected")
//	}
//
//	useEffect(() => 
//	{
//		if (initialLoad)
//		{
//			setInitialLoad(false);
//
//			let newSocket = new WebSocket("ws://localhost:61613/stomp");
//			//let newSocket = new WebSocket("ws://localhost:61613/stomp");
//			//let newSocket = new WebSocket("ws://localhost:15672");
//			let _client = Stomp.over(newSocket)
//			_client.connect(
//				{login:'guest',passcode:'guest'}, 
//				function onConnect() {
//					console.log("connected, try subscribe");
//					_client.subscribe('/hello', message => {console.log(message)})
//				},
//				onError, 
//				'/'
//			);
//
//			setStompSession(_client);
//		}
//	}, [initialLoad, stompSession])


	//	//const navigate = useNavigate();
	//	const { username, token } = useAuth();
	//	const [localMessage, setLocalMessage] = useState("");
	//	const [messages, setMessages] = useState("");
	//	const [playerList, setPlayerList] = useState(null)
	//	const [initialLoad, setInitialLoad] = useState(true)
	//
	//	async function getMessages() {
	//		const queue = 'tasks';
	//		const conn = await amqplib.connect('amqp://localhost');
	//		const ch1 = await conn.createChannel();
	//		await ch1.assertQueue(queue);
	//
	//		// Listener
	//		ch1.consume(queue, (msg) => {
	//			if (msg !== null)
	//			{
	//				console.log("recv msg: " + msg.content.toString());
	//				ch1.ack(msg);
	//				return msg.content.toString();
	//			}
	//			else
	//			{
	//				console.log("consumer cancelled by server");
	//			}
	//		});
	//	}
	//
	//	useEffect(() => 
	//	{
	//		// await data from amqp queue
	//		async function fetchData() { 
	//			// can await here
	//			const response = await getMessages();
	//			return response;
	//		}
	//
	//		// save to messages
	//		const newMessage = fetchData();
	//		setMessages(newMessage);
	//	}, []);
	//
	//	const refreshPlayerListClicked = () => {
	//		console.log("refresh");
	//		getMessages();
	//	}
	//
	//	//const playerListMessageHandler = (message) =>
	//	//{
	//	//	//let newPlayerList = null;
	//
	//	//	//try {
	//	//	//	newPlayerList = JSON.parse(message.body)
	//	//	//	console.log("Recv playerlist: " + newPlayerList);
	//	//	//}
	//	//	//catch (e) {
	//	//	//	console.log("Invalid PlayerList response: " + e)
	//	//	//}
	//
	//	//	//setPlayerList(newPlayerList);
	//	//}
	//
	//
	//	return (
	//		<div>
	//			<div>
	//				<h1>Player List</h1>
	//				<p /><button className="btn btn-success" onClick={refreshPlayerListClicked}>Refresh</button>
	//				<div className="container">
	//					<table className="table">
		//						<thead>
	//							<tr>
	//								<th>Username</th>
	//								<th>IsRequesting</th>
	//								<th>YouRequested</th>
	//								<th>InitiateRequest</th>
	//							</tr>
		//						</thead>
	//						<tbody>
	//							{
	//								playerList && playerList.map(
	//									player =>
	//										<tr key={player.username}>
	//											<td>{player.username}</td>
		//											<td>{player.myRequest ? "Yes" : "No"}</td>
		//											<td>{player.theirRequest ? "Yes" : "No"}</td>
	//										</tr>
	//								)
	//							}
	//						</tbody>
	//					</table>
	//				</div>
	//			</div>
	//		</div>
		//	)

//
//	useEffect(() => {
//		console.log('Component did mount');
//		// The compat mode syntax is totally different, converting to v5 syntax
//		// Client is imported from '@stomp/stompjs'
//		var client = new Client();
//
//		client.configure({
//			brokerURL: 'ws://localhost:8080/stomp',
//			onConnect: () => {
//				console.log('onConnect');
//
//				//client.subscribe('/queue/now', message => {
//				client.subscribe('/topic/messages', message => {
//					console.log(message);
//					//setState({mess: message.body});
//				});
//
//				//client.subscribe('/topic/greetings', message => {
//				//	console.log(message.body);
//				//});
//			},
//			// Helps during debugging, remove in production
//			debug: (str) => {
//				console.log(new Date(), str);
//			}
//		});
//
//		client.activate();
//	}, [])
//				<SockJsClient 
//					url='http://localhost:8080/stomp' 
//					topics={['/topic/messages']}
//					headers={customHeaders}
//					subscribeHeaders={customHeaders}
//					onMessage={onMessageReceive}
//					onConnect={onConnect}
//					onDisconnect={onDisconnect}
//					ref={ (client) => { this.clientRef = client }}
//					/>
				//message.appendChild(document.createTextNode(JSON.parse(payload.body).message));
				//message_list.appendChild(message);
//
//	useEffect(() =>
//	{
//		//if (socketConnected)
//		//{
//		//	socket.onopen = function() {
//		//		console.log('open');
//		//		socket.send('test');
//		//	};
//
//		//	socket.onmessage = function(e) {
//		//		console.log('message', e.data);
//		//		socket.close();
//		//	};
//
//		//	socket.onclose = function() {
//		//		console.log('close socket');
//		//	};
//		//}
//		//else
//		//{
//		//let newStompClient = Stomp.Client("ws://localhost:8080/tictactoe/playerlist")
//
//		if (!socketConnected)
//		{
//		setSocketConnected(true);
//		//let newSocket = new SockJS("gs-guide-websocket");
//		let newSocket = new SockJS("http://localhost:8080/stomp");
//		let newStompClient = Stomp.over(newSocket)
//
//		//let newStompClient = Stomp.client("ws://localhost:8080/tictactoe/playerlist");
//
//		//setCustomHeaders({"Authorization": token});
//		console.log("create new socket")
//		newStompClient.connect(
//		{},
//		frame => {
//		  //connected = true;
//			console.log("connect success");
//		  newStompClient.subscribe("/topic/greeting", function (test_text) {
//
//			  console.log("get message:")
//			  console.log(test_text)
//			  console.log(JSON.parse(test_text.body.content))
//		  });
//		},
//		error => {
//			console.log("connect fails");
//		  console.log(error);
//		  //connected = false;
//		})
//
//		//setSocket(newSocket)
//		setStompClient(newStompClient)
//		}
//		//}
//
//	}, [customHeaders, socket, socketConnected]);

	//useEffect(() => 
	//{
	//	const socket = new SockJS("http://localhost:8080/tictactoe/playerlist");
	//	const stompClient = Stomp.over(socket);
	//	const headers = {}
	//	//const headers = {Authorization: token}
	//	//stompClient.connect({}, onConnect, onError);

	//	stompClient.connect(headers, () => {
	//		stompClient.subscribe(
	//			'/tictactoe/playerlist', console.log, headers,
	//		);
	//	});

	//	return () => stompClient && stompClient.disconnect();
	//}, [token]);

	////if (!connected)
	////{
	////	client.activate();
	////}
	//stompClient.connect({}, function () {
	//	setConnected(true)
	//	stompClient.subscribe("", function (message) {
	//	});
	//});
	//}

//export const PLAYER_LIST_API_URL = 'http://localhost:8080/tictactoe/playerlist'

////				<SockJsClient 
////					url='http://localhost:8080/ws' 
////					topics={['/tictactoe/playerlist']}
////					headers={customHeaders}
////					subscribeHeaders={customHeaders}
////					onMessage={onMessageReceive}
////					onConnect={onConnect}
////					onDisconnect={onDisconnect}
////					/>
//					ref={ (client) => { this.clientRef = client }} />


	//function sendName() {
	//	stompClient.send("/app/hello", {}, JSON.stringify({'name': $("#name").val()}));
	//}

	//function showGreeting(message) {
	//	$("#greetings").append("<tr><td>" + message + "</td></tr>");
	//}

//	const clientConnect = () => {
//		console.log("connected");
//		setConnected(true)
//	}
//
//	const clientDisconnect = () => {
//		console.log("disconnected");
//		setConnected(false)
//	}
//
//	const onMessageReceive = (msg, topic) => {
//		console.log("topic: " + topic)
//		console.log("msg:" + msg)
//		setPlayerList(msg)
//	}
	
//	//const client = new StompJs.Client(
//	const client = new Stomp.overWS(
//	{
//		brokerURL: 'ws://localhost:8080/ws',
//		connectHeaders: {
//			Authorization: {token},
//		},
//		debug: function (str) {
//			console.log(str);
//		},
//		reconnectDelay: 5000,
//		heartbeatIncoming: 3000,
//		heartbeatOutgoing: 3000,
//	});
//
//	client.onConnect = function(frame) {
//		// subscribe
//	};
//
//	client.onStompError = function(frame) {
//		console.log('Broker reported error: ' + frame.headers['message']);
//		console.log('Additional details: ' + frame.body);
//	};

	////return (
	////	<div>
	////		<SockJsClient 
	////			url={"http://localhost:8080/"} 
	////			topics={["tictactoe/playerlist"]}
	////			onMessage={onMessageReceive}
	////			ref={ (client) => { this.clientRef = client }}
	////			onConnect={clientConnect} 
	////			onDisconnect={clientDisconnect} 
	////			debug={false}
	////		/>
	////	</div>
	////)
	//function setConnected(connected) {
	//	$("#connect").prop("disabled", connected);
	//	$("#disconnect").prop("disabled", !connected);
	//	if (connected) {
	//		$("#conversation").show();
	//	}
	//	else {
	//		$("#conversation").hide();
	//	}
	//	$("#greetings").html("");
	//}

	//function connect() {
	//	var socket = new SockJS('/gs-guide-websocket');
	//	stompClient = Stomp.over(socket);
	//	stompClient.connect({}, function (frame) {
	//		setConnected(true);
	//		console.log('Connected: ' + frame);
	//		stompClient.subscribe('/topic/greetings', function (greeting) {
	//			showGreeting(JSON.parse(greeting.body).content);
	//		});
	//	});
	//}

	//function disconnect() {
	//	if (stompClient !== null) {
	//		stompClient.disconnect();
	//	}
	//	setConnected(false);
	//	console.log("Disconnected");
	//}

	//	useEffect(() => {
	//		if (eventSource === undefined)
	//		{
	//			setEventSource(new EventSource(PLAYER_LIST_API_URL))
	//		}
	//	}, [eventSource])
	//
	//	eventSource.onmessage = (event) => {
	//		const 


	//useEffect(() => 
	//{
	//	if (!listening) 
	//	{
	//		let eventSource = undefined;
	//		eventSource = new EventSource(PLAYER_LIST_API_URL)

	//		eventSource.onopen = (event) => {
	//			console.log("connection opened");
	//		}

	//		eventSource.onmessage = (event) => {
	//			let currentResult = JSON.parse(event.data)
	//			console.log("current: " + currentResult);
	//			setPlayerList(currentResult)
	//			setLoadedPlayers(true);
	//			console.log("loaded players");
	//			//eventSource.close()
	//		}

	//		eventSource.onerror = (event) => 
	//		{
	//			console.log(event.target.readyState)
	//			if (event.target.readyState === EventSource.CLOSED) 
	//			{
	//				console.log('eventsource closed (' + event.target.readyState + ')')
	//			}
	//			eventSource.close();
	//		}

	//		setListening(true);

	//		//// cleanup
	//		//return () => {
	//		//	console.log('CLEANUP');
	//		//	eventSource.close();
	//		//};
	//	}

	//	// cleanup
	//	//return () => {
	//	//	console.log('CLEANUP');
	//	//	eventSource.close();
	//	//};
	//}, [listening])

    //if (loadedPlayers && playerList)
    //{
	//	console.log("playerList: " + playerList)
    //    return (
    //        <div>
    //            <h1>Player List</h1>
    //            {data.message && <div class="alert alert-success">{data.message}</div>}
    //            <div className="container">
    //                <table className="table">
    //                    <thead>
    //                        <tr>
    //                            <th>Username</th>
    //                            <th>IsRequesting</th>
    //                            <th>YouRequested</th>
    //                            <th>InitiateRequest</th>
    //                        </tr>
    //                    </thead>
    //                    <tbody>
    //                        {
    //                            playerList.map(
    //                                player =>
    //                                    <tr key={player.id}>
    //                                        <td>{player.username}</td>
	//										<td>{player.myRequest ? "Yes" : "No"}</td>
    //                                        <td>{player.theirRequest ? "Yes" : "No"}</td>
    //                                    </tr>
    //                            )
    //                        }
    //                    </tbody>
    //                </table>
	//				<button className="btn btn-success" onClick={refreshPlayersClicked}>Update</button>
    //            </div>
    //        </div>
    //    )
    //}
    //else
    //    return (
    //        <>
    //            <div className="container">
	//				<p>No other players are online</p>
	//				<p><button className="btn btn-success" onClick={refreshPlayersClicked}>Update Player List</button></p>
    //            </div>
    //        </>
    //    )        
//                                        <tr key={player.id}>
//                                            <td>
//												{!player.theirRequest && <button className="btn btn-warning" onClick={() => requestMatchClicked(player.username)}>Request</button>}
//												{player.theirRequest && <button className="btn btn-warning" onClick={() => requestMatchClicked(player.username)}>Accept Request</button>}
//											</td>

//    const navigate = useNavigate();
//    const { isAuthenticated, getSession, username } = useAuth();
//    const [state, setState] = useState({
//      players: [],
//      message: "",
//      loadedPlayers: false,
//    });
//
//    const getPlayers = useCallback(async () =>
//    {
//        let axiosSession = getSession();
//        const data = await axiosSession.get(PLAYER_LIST_API_URL)
//        .then( response => 
//        {
//			console.log("response: " + response);
//            return response.data;
//        }
//        )
//        .catch(err => {
//			console.log("error: " + err);
//            return null
//        })
//
//        if (data !== null)
//        {
//            setState({players: data});
//        }
//    }, [getSession]);
//
//    useEffect(() => 
//    {
//        // check if auth
//        if (!isAuthenticated())
//        {
//            navigate("/login");
//        }
//    }, [navigate, isAuthenticated]);
//
//    useEffect(() => 
//    {
//        // empty players
//        if (state.loadedPlayers === false)
//        {
//            console.log("useEffect(getplayers)")
//            // async update players list
//            getPlayers();
//            setState({...state, loadedPlayers: true});
//        }
//
//    }, [getPlayers, getSession, state]);
//
//	const refreshPlayersClicked = () =>
//	{
//		getPlayers();
//	}
//
//	const requestMatchClicked = (username) =>
//	{
//		let thisUser = username
//	}
//
//    if (state.players)
//    {
//        return (
//            <div>
//                <h1>Player List</h1>
//                {state.message && <div class="alert alert-success">{state.message}</div>}
//                <div className="container">
//                    <table className="table">
//                        <thead>
//                            <tr>
//                                <th>Username</th>
//                                <th>IsRequesting</th>
//                                <th>YouRequested</th>
//                                <th>InitiateRequest</th>
//                            </tr>
//                        </thead>
//                        <tbody>
//                            {
//                                state.players.map(
//                                    player =>
//                                        <tr key={player.id}>
//                                            <td>{player.username}</td>
//                                            <td>{player.theirRequest ? "Yes" : "No"}</td>
//											<td>{player.myRequest ? "Yes" : "No"}</td>
//                                            <td>
//												{!player.theirRequest && <button className="btn btn-warning" onClick={() => requestMatchClicked(player.username)}>Request</button>}
//												{player.theirRequest && <button className="btn btn-warning" onClick={() => requestMatchClicked(player.username)}>Accept Request</button>}
//											</td>
//                                        </tr>
//                                )
//                            }
//                        </tbody>
//                    </table>
//					<button className="btn btn-success" onClick={refreshPlayersClicked}>Update</button>
//                </div>
//            </div>
//        )
//    }
//    else
//        return (
//            <>
//                <div className="container">
//					<p>No other players are online</p>
//					<p><button className="btn btn-success" onClick={refreshPlayersClicked}>Update Player List</button></p>
//                </div>
//            </>
//        )        
//}
//

	//useEffect(() =>
	//{
	//	setCustomHeaders({"Authorization": token});
	//}, [token])
	//const [data, setData] = useState([]);
	//const [state, setState] = useState({
	//  players: [],
	//  message: "",
	//  loadedPlayers: false,
	//});

	//const refreshPlayersClicked = () =>
	//{
	//	//getPlayers();
	//	setListening(false);
	//}

	///const requestMatchClicked = (username) =>
	///{
	///}
	//sendMessage = (msg, selfMsg) => {
	//	setState(SockJsClient

	// randomUserId is used to emulate a unique user id for this demo usage

	//const onMessageReceive = (msg) => {
	//	console.log("msg: " + msg)
	//	//console.log("topic: " + topic)
	//	setMessage(msg);
	//}

	//const sendMessage = (msg) => {
	//	this.clientRef.sendMessage('topics/all', msg)
	//}

	//const sendMessage = (msg, selfMsg) => {
	//	try {
	//		this.clientRef.sendMessage("/app/all", JSON.stringify(selfMsg));
	//		return true;
	//	} catch(e) {
	//		return false;
	//	}
	//}

	//componentWillMount() {
	//	Fetch("/history", {
	//		method: "GET"
	//	}).then((response) => {
	//		this.setState({ messages: response.body });
	//	});
	//}

	//const onConnect = () => 
	//{
	//	console.log("Connected");
	//}

	//const onDisconnect = () => 
	//{
	//	console.log("disconnected");
	//}

	//const onError = () => 
	//{
	//	console.log("error");
	//}

	//const { isAuthenticated, getSession, username, token } = useAuth();
	//const [listening, setListening] = useState(false);
    //const [data, setData] = useState({
    //  message: "",
    //  loadedPlayers: false,
    //});
    //const [playerList, setPlayerList] = useState([])
	//const [loadedPlayers, setLoadedPlayers] = useState(false);
	//const [connected, setConnected] = useState(false);
	//const [message, setMessage] = useState("");
	//const [customHeaders, setCustomHeaders] = useState({});

	//const [socket, setSocket] = useState();
	//const [socketConnected, setSocketConnected] = useState(false);
	//const [stompClient, setStompClient] = useState();
	//const [messageBox, setMessageBox] = useState("");
	//const { } = useAuth();
	//const { loading, client, getMessages, sendMessage, getPlayerList, playerList, messages, error } = useTictactoe();
	//const { getPlayerListSession, playerList } = useAuth();
	//useEffect(() => 
	//{
	//	if (messages)
	//		setLocalMessage(messages);
	//},[messages]);


	////const onMessageReceived = (payload) => {
	////	console.log("onmessagerecv");
	////	var message = JSON.parse(payload.body);
	////	console.log("message: " + message)
	////}

	////const onConnected = () => {
	////	stompClient.subscribe("/topic/messages", onMessageReceived)
	////}

	////const onError = (error) => {
	////	console.log("error: " + error)
	////}

	////// working-connect
	////useEffect(() =>
	////{
	////	if (!connected)
	////	{
	////		// Try to set up WebSocket connection with the handshake at "http://localhost:8080/stomp"
	////		let newSocket = new SockJS("http://localhost:8080/stomp")
	////		console.log("setSocket")

	////		// Create a new StompClient object with the WebSocket endpoint
	////		//setStompClient(Stomp.over(socket))
	////		let newStompClient = Stomp.over(newSocket)
	////		setStompClient(newStompClient);
	////		newStompClient.connect({},  onConnected, onError)
	////		//newStompClient.connect({}, (() => onConnected), (() => onError))
	////		console.log("setStomp")
	////		setConnected(true);
	////	}

	//////		//let newStompClient = Stomp.Client("ws://localhost:8080/stomp")

	//////		// Start the STOMP communications, provide a callback for when the CONNECT frame arrives.
	//////		newStompClient.connect({}, frame => {
	//////			let messageBox = "test";
	//////			newStompClient.send('/app/chat', {}, JSON.stringify({message: messageBox}));
	//////			newStompClient.send('/app/chat', "test123");
	//////			console.log("connected")
	//////			// Subscribe to "/topic/messages". Whenever a message arrives add the text in a list-item element in the unordered list.
	//////			newStompClient.subscribe("/topic/messages", payload => {
	//////				console.log("payload body: " + payload.body)
	//////				//setMessage(message,JSON.parse(payload.body).message);
	//////			})
	//////		})
	//////		//}, error => {
	//////		//	console.log("connect fails");
	//////		//	console.log(error);
	//////		//	//connected = false;
	//////		//});
	//////		setSocket(newSocket)
	//////		setStompClient(newStompClient)
	//////		setConnected(true);
	////}, [connected]);

	//// working-connect
	//const connectClicked = () => 
	//{
	//	if (connected)
	//	{
	//		let messageBox = "test";
	//		stompClient.send('/app/chat', {}, JSON.stringify({message: messageBox}));
	//		//stompClient.send('/app/chat', {}, JSON.stringify({message: messageBox}));
	//	}
	//}
	//useEffect(() =>
	//{
	//	if (!connected)
	//	{
	//		// Try to set up WebSocket connection with the handshake at "http://localhost:8080/stomp"
	//		let newSocket = new SockJS("http://localhost:8080/stomp")
	//		console.log("setSocket")

	//		// Create a new StompClient object with the WebSocket endpoint
	//		//setStompClient(Stomp.over(socket))
	//		let newStompClient = Stomp.over(newSocket)
	//		console.log("setStomp")

	//		//let newStompClient = Stomp.Client("ws://localhost:8080/stomp")

	//		// Start the STOMP communications, provide a callback for when the CONNECT frame arrives.
	//		newStompClient.connect({}, frame => {
	//			let messageBox = "test";
	//			newStompClient.send('/app/chat', {}, JSON.stringify({message: messageBox}));
	//			newStompClient.send('/app/chat', "test123");
	//			console.log("connected")
	//			// Subscribe to "/topic/messages". Whenever a message arrives add the text in a list-item element in the unordered list.
	//			newStompClient.subscribe("/topic/messages", payload => {
	//				console.log("payload body: " + payload.body)
	//				//setMessage(message,JSON.parse(payload.body).message);
	//			})
	//		})
	//		//}, error => {
	//		//	console.log("connect fails");
	//		//	console.log(error);
	//		//	//connected = false;
	//		//});
	//		setSocket(newSocket)
	//		setStompClient(newStompClient)
	//		setConnected(true);
	//	}
	//}, [connected]);

	// Take the value in the ‘message-input’ text field and send it to the server with empty headers.
	//function sendMessage(){
	//	if (stompClient)
	//	{
	//		//let input = messageBox;
	//		stompClient.send('/app/chat', {}, JSON.stringify({message: messageBox}));
	//	}
	//	
	//}

    // handle login form data update
    //const handleMessageBox = (e) => 
    //{
	//	//e.preventDefault();

    //    //const value = e.target.value
	//	//setMessageBox(value)
    //}

		//const client = new Client({
		//	brokerURL: 'http://localhost:8080/stomp',
		//	connectHeaders: {}, 
		//	debug: function (str) {
		//			console.log(str)
		//		},
		//	reconnectDelay: 5000,
		//	heartbeatIncoming: 4000,
		//	heartbeatOutgoing: 4000,
		//});
		//setStompClient(client)
//				<p><button className="btn btn-success" onClick={sendMessage}>Send Message</button></p>
//				<p>Message: <input type="text" name="message" value={messageBox} onChange={handleMessageBox} /></p>
//	const onConnect = () => 
//	{
//		console.log("Connected");
//	}
//
//	const onDisconnect = () => 
//	{
//		console.log("disconnected");
//	}
//
//	const onError = () => 
//	{
//		console.log("error");
//	}
//
//	const onMessageReceive = (msg) => {
//		//console.log("topic: " + topic)
//		console.log("msg:" + msg)
//		//setPlayerList(msg)
//	}
//
	//const getPlayerListSession = useCallback(async () => 
	//{
	//	if (stompSession == null)
	//	{
	//		var _client = new Client();
	//		var headerValues = {
	//			Authorization: token
	//		}

	//		_client.configure({
	//			brokerURL: 'ws://localhost:8080/stomp',
	//			connectHeaders: headerValues,
	//			onConnect: () => 
	//				{
	//					_client.subscribe
	//					(
	//						'/topic/playerList', 
	//						playerListMessageHandler,
	//						headerValues,
	//					)
	//				},
	//			onStompError: (frame) => {
	//				console.log("stompError: " + frame);
	//			},
	//			// debug messages
	//			//debug: (str) => {
	//			//	console.log(new Date(), str);
	//			//}
	//		});

	//		_client.activate();
	//		setStompSession(_client);
	//	}
	//}, [token, stompSession])


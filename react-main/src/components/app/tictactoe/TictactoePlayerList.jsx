import moment from 'moment'
import React, { useEffect, useCallback, useState } from "react";
import { useNavigate } from "react-router-dom";
import useAuth from '../../authentication/AuthProvider.tsx';
import { PLAYER_LIST_API_URL } from '../../../Constants';
//import SockJsClient from 'react-stomp';
//import SockJS from 'sockjs-client';
//import Stomp from 'stompjs';
import { Client } from '@stomp/stompjs';
import useTictactoe from './TictactoeProvider.tsx'
//import {StompSessionProvider, useSubscription} from 'react-stomp';
//import { Stomp, Client } from 'stompjs';


export default function TictactoePlayerList()
{
	//const navigate = useNavigate();
	const { username, token } = useAuth();
	const [localMessage, setLocalMessage] = useState("");
	const [messages, setMessages] = useState("");
	const [stompSession, setStompSession] = useState(null);
	const [playerList, setPlayerList] = useState(null)
	const [initialLoad, setInitialLoad] = useState(true)

	useEffect(() => 
    {
		if (initialLoad)
		{
			setInitialLoad(false);

			var _client = new Client();
			var headerValues = {
				login: username,
				passcode: token,
				'Authorization': token
			}

			_client.configure({
				brokerURL: 'ws://localhost:8080/stomp',
				connectHeaders: headerValues,
				onConnect: () => 
					{
						_client.subscribe
						(
							'/topic/playerList', 
							playerListMessageHandler,
							headerValues,
						)
					},
				onStompError: (frame) => {
					console.log("stompError: " + frame);
				},
				// debug messages
				//debug: (str) => {
				//	console.log(new Date(), str);
				//}
			});

			_client.activate();
			setStompSession(_client);
		}
	}, [initialLoad, stompSession, username, token])

	const refreshPlayerListClicked = () => {
		stompSession.publish({destination: '/app/playerList', body: 'test playerList'});
	}

	const playerListMessageHandler = (message) =>
	{
		let newPlayerList = null;

		try {
			newPlayerList = JSON.parse(message.body)
			console.log("Recv playerlist: " + newPlayerList);
		}
		catch (e) {
			console.log("Invalid PlayerList response: " + e)
		}

		setPlayerList(newPlayerList);
	}


	return (
		<div>
			<div>
				<h1>Player List</h1>
				<p /><button className="btn btn-success" onClick={refreshPlayerListClicked}>Refresh</button>
				<div className="container">
					<table className="table">
						<thead>
							<tr>
								<th>Username</th>
								<th>IsRequesting</th>
								<th>YouRequested</th>
								<th>InitiateRequest</th>
							</tr>
						</thead>
						<tbody>
							{
								playerList && playerList.map(
									player =>
										<tr key={player.username}>
											<td>{player.username}</td>
											<td>{player.myRequest ? "Yes" : "No"}</td>
											<td>{player.theirRequest ? "Yes" : "No"}</td>
										</tr>
								)
							}
						</tbody>
					</table>
				</div>
			</div>
		</div>
	)

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
}
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


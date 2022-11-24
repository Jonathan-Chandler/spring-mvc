import React, { ReactNode, useCallback, useState, useEffect, useMemo, createContext, useContext } from 'react';
import axios, { Axios, AxiosError } from 'axios'
import { LOGIN_API_URL, REGISTER_API_URL } from '../../Constants'
import { Stomp, Client } from '@stomp/stompjs';
//import { RxStomp } from '@stomp/rx-stomp';
//import { Client } from '@stomp/stompjs';

//interface PlayerListEntry
//{
//	username: String;
//	myRequest: boolean;
//	theirRequest: boolean;
//}

// atuh context vars/functions
interface AuthContextType {
	username: string;
	token: string;
	loading: boolean;
	isAuthenticated: () => boolean;
	login: (username: string, password: string) => void;
	register: (username: string, email: string, password: string) => void;
	logout: () => void;
	getSession: () => Axios;
	getStompSession: () => Client;
	//getStompSession: () => RxStomp;
	//getPlayerListSession: () => void;
	playerList: any;
	error?: any;
}

// shared context
const AuthContext = createContext<AuthContextType>(
	{} as AuthContextType
);

class StompProvider
{
	username: string;
	token: string;
	currentStompSession: Client;
	//getSession: (username, token) => Client;

	constructor(username, token)
	{
		this.username = username;
		this.token = token;
		this.currentStompSession = null;

		// initialize
		this.getSession(username, token);
	}

	getSession(newUsername, newToken)
	{
		if (this.currentStompSession)
		{
			try
			{
				console.log("startstomp - disconnect current stomp");
				//const usernameTopic = '/topic/user.' + username;
				//const playerListTopic = '/topic/playerlist';
				//playerlistSubscription.unsubscribe();
				//userSubscription.unsubscribe();
				//stompSession.unsubscribe(usernameTopic, stomp_headers);
				//stompSession.unsubscribe(playerListTopic, stomp_headers);

				//stompSession.forceDisconnect();
				(async () => 
					{
						await this.currentStompSession.deactivate();
					})()
				this.currentStompSession = null;
			}
			catch(err)
			{
			}
		}

		if (newUsername && newToken 
			&& newUsername !== this.username 
			&& newToken !== this.token)
		{
			this.username = newUsername;
			this.token = newToken;

			let pass = this.token.split(' ')[1]
			//let stompTopic = '/topic/hello';
			const stomp_headers = {login: this.username, passcode: pass};
			const usernameTopic = '/topic/user.' + this.username;
			const playerListTopic = '/topic/playerlist';
			//console.log("stomp login: " + new_username + " pass: " + pass)

			//if (stompSession)
			//{
			//	// remove existing session
			//	try
			//	{
			//		console.log("startstomp - disconnect current stomp");
			//		//const usernameTopic = '/topic/user.' + username;
			//		//const playerListTopic = '/topic/playerlist';
			//		//playerlistSubscription.unsubscribe();
			//		//userSubscription.unsubscribe();
			//		//stompSession.unsubscribe(usernameTopic, stomp_headers);
			//		//stompSession.unsubscribe(playerListTopic, stomp_headers);

			//		//stompSession.forceDisconnect();
			//		(async () => 
			//		{
			//			await stompSession.deactivate();
			//			setStompSession(null);
			//		})()
			//	}
			//	catch(err)
			//	{
			//	}
			//}

			//const client = new StompJs.Client({
			//passcode: token
			//login: new_username,
			let client = new Client({
				brokerURL: 'ws://172.17.0.3:61611/ws',
				connectHeaders: {
					//login: new_username,
					login: this.username,
					passcode: pass,
					//'heart-beat': '4000,4000'
				},
				disconnectHeaders: {
					//login: new_username,
					login: this.username,
					passcode: pass,
					//'heart-beat': '4000,4000'
				},

				debug: function (str) 
				{
					console.log(str);
				},
				reconnectDelay: 5000,
				//heartbeatIncoming: 4000,
				//heartbeatOutgoing: 4000,
			});

			//client.onConnect = onConnectTest;
			client.onConnect = function (frame) 
			{
				console.log("onConnect")

				//const subUsername = this.currentStompSession.subscribe(
				client.subscribe(
					usernameTopic,
					function (msg) {
						console.log(usernameTopic + " msg = " + msg);
					},
					stomp_headers
				);

				//const subPlayerlist = this.currentStompSession.subscribe(
				client.subscribe(
					playerListTopic,
					function (msg) {
						console.log(playerListTopic + " msg = " + msg);
					},
					stomp_headers
				);

				//setUserSubscription(subUsername);
				//setPlayerlistSubscription(subPlayerlist);
			};

			//client.onStompError = onStompErrorTest;
			client.onStompError = function (frame) {
				// error encountered at Broker
				console.log('Stomp Broker reported error: ' + frame.headers['message']);
				console.log('Additional details: ' + frame.body);
				//client.unsubscribe(usernameTopic);
				//client.unsubscribe(playerListTopic);
				//client.deactivate();
				//setStompSession(null);
			};

			//client.onWebSocketError = onWebSocketErrorTest;
			client.onWebSocketError = function (closeEvent) {
				// error encountered at Broker
				console.log('web socket reported error: ' + closeEvent);
				//client.unsubscribe(usernameTopic);
				//client.unsubscribe(playerListTopic);
				//client.deactivate();
				//setStompSession(null);
			};

			//client.onWebSocketClose = onWebSocketCloseTest;
			client.onWebSocketClose = function (closeEvent) {
				// error encountered at Broker
				console.log('callback closed websocket: ' + closeEvent);
				//client.unsubscribe(usernameTopic);
				//client.unsubscribe(playerListTopic);
				//client.deactivate();
				//setStompSession(null);
			};

			//client.onDisconnect = onDisconnectTest
			client.onDisconnect = function (closeEvent) {
				// error encountered at Broker
				console.log('callback disconnected stomp: ' + closeEvent);
				//client.unsubscribe(usernameTopic);
				//client.unsubscribe(playerListTopic);
				//client.deactivate();
				//setStompSession(null);
			};

			//// use a WebSocket
			//client.webSocketFactory = function () {
			//  return new WebSocket("ws://broker.329broker.com:15674/ws");
			//};

			client.activate();
			this.currentStompSession = client;

			return client;
		}

		return this.currentStompSession;
	}

}

// Export the provider as we need to wrap the entire app with it
export function AuthProvider({
	children,
}: {
	children: ReactNode;
}): JSX.Element {
	const [username, setUsername] = useState<string>(sessionStorage.getItem("username"));
	const [token, setToken] = useState<string>(sessionStorage.getItem("Authorization"));
	const [loading, setLoading] = useState<boolean>(false);
	const [error, setError] = useState<any>();
	const [loadingInitial, setLoadingInitial] = useState<boolean>(true);
	//const [stompSession, setStompSession] = useState(null);
	const [playerList, setPlayerList] = useState([{}]);
	const [stompProvider] = useState<StompProvider>(new StompProvider(null,null));
	//const [playerlistSubscription, setPlayerlistSubscription] = useState(null);
	//const [userSubscription, setUserSubscription] = useState(null);
	//const [webSockSession, setWebSockSession] = useState(null);

	useEffect(() => {
		// initialize done
		setLoadingInitial(false)
	}, []);

	// check if user is logged in
	const isAuthenticated = useCallback( () => {
		return (username && username !== "" && token && token !== "");
	}, [username, token]);

	// send login request
	const login = useCallback(async (username: string, password: string) => {
		setLoading(true);
		const userData = {username: username, password: password};

		await axios
			.post(`${LOGIN_API_URL}`, userData)
			.then((response) => 
				{
					// if returned valid auth token then update context with new token/username
					if (response.data.Authorization)
					{
						console.log("success login; token=" + response.data.Authorization)
						setToken(response.data.Authorization)

						// set username and login success
						setUsername(username)
						sessionStorage.setItem("Authorization", response.data.Authorization);
						sessionStorage.setItem("username", username);
						setError("");
						//startStompSession();
						setLoading(false);
					}
				})
				.catch((error) => {
					// update error from axios
					let errorMessage = "";
					if (error.response) {
						console.log(error.response);
						errorMessage = "ErrorResponse: " + error.request;
					} else if (error.request) {
						console.log("network error");
						errorMessage = "ErrorRequest: " + error.request;
					} else {
						errorMessage = "Error: " + error;
					}

					console.log("Fail login: " + errorMessage);
					setUsername(null);
					setToken(null);
					setError(errorMessage);
					setLoading(false);
				});
	}, []);

	// create new user
	const register = useCallback(async (username: string, email:string, password: string) => {
		setLoading(true);
		const registerData = {username: username, email: email, password: password};

		await axios
			.post(`${REGISTER_API_URL}`, registerData)
			.then((response) => 
				{
					console.log("success register; response=" + response)
					setUsername(null);
					setToken(null);
					setError(null);
					setLoading(false);
				})
				.catch((error) => {
					// update error from axios
					let errorMessage = "";
					if (error.response) {
						console.log(error.response);
						errorMessage = "ErrorResponse: " + error.request;
					} else if (error.request) {
						console.log("network error");
						errorMessage = "ErrorRequest: " + error.request;
					} else {
						errorMessage = "Error: " + error;
					}
					console.log("Fail login: " + errorMessage);

					// log out and set error response message
					setUsername(null);
					setToken(null);
					setError(errorMessage);
					setLoading(false);
				});
	}, []);

	// logout and remove username/token
	const logout = useCallback(async () => {
		console.log("logging out");
		setLoading(true);
		//if (stompSession !== null)
		//{
		//try
		//{
		//	console.log("disconnect stomp");
		//	//const stomp_headers = {login: username, passcode: token};
		//	//const usernameTopic = '/topic/user.' + username;
		//	//const playerListTopic = '/topic/playerlist';
		//	//stompSession.unsubscribe(usernameTopic, stomp_headers);
		//	//stompSession.unsubscribe(playerListTopic, stomp_headers);
		//	//playerlistSubscription.unsubscribe();
		//	//userSubscription.unsubscribe();
		//	//stompSession.forceDisconnect();
		//	await stompSession.deactivate();
		//}
		//catch(err)
		//{
		//}
		//try
		//{
		//	console.log("close ws");
		//	//const usernameTopic = '/topic/user.' + username;
		//	//const playerListTopic = '/topic/playerlist';
		//	//stompSession.unsubscribe(usernameTopic);
		//	//stompSession.unsubscribe(playerListTopic);
		//	//stompSession.forceDisconnect();
		//	webSockSession.close();
		//}
		//catch(err)
		//{
		//}
		//}
		sessionStorage.removeItem("Authorization");
		sessionStorage.removeItem("username");
		setUsername(null);
		setToken(null);
		setError(null);
		//setStompSession(null);
		stompProvider.getSession(null, null);
		//setWebSockSession(null);
		setLoading(false);
	}, [stompProvider]);

	// interceptor updates token from response header
	const handleResponseHeaders = (response) => 
	{
		//// update token if exists in response header
		//if (response.headers.authorization)
		//{
		//	// update token for session and local storage
		//	sessionStorage.setItem("Authorization", response.data.Authorization);
		//	setToken(response.headers.authorization);
		//	//console.log("captured response header and set auth: " + response.headers.authorization);
		//}

		// return intercepted response to caller
		return response;
	}

	// interceptor forces logout if backend returns authentication failure
	const handleResponseAuthFailure = useCallback((error: AxiosError) => 
		{
			console.log("Auth Failure: " + error);
			if (error.response.status === 401)
			{
				console.log("error code Failure: " + error);
				logout();
			}

			// remove token from session if auth rejected
			if (String(error).indexOf("401") !== -1)
			{
				// update error message
				setError("HandleResponseFailure: " + error)

				// force logout on auth failure
				logout();
			}

			// return intercepted error to caller
			return Promise.reject(error);
		}, [logout]);

	// get session with response handlers and auth token header
	const getSession = useCallback(() =>
		{
			let axiosSession = axios.create()

			// add auth token header
			axiosSession.defaults.headers.common['Authorization'] = token;

			// update token from response headers with updated auth tokens and handle failed authentication
			axiosSession.interceptors.response.use(handleResponseHeaders, handleResponseAuthFailure);

			console.log("using auth header token: " + token)

			return axiosSession;
		}, [handleResponseAuthFailure, token]);

	//const getPlayerList = useCallback(() =>
	//	{
	//		return playerList;
	//	},[playerList]);

	const onMessage = useCallback((d) =>
	{
		try 
		{
			var parsed = JSON.parse(d.body);
			//var message = parsed.message;
			//setStompMessage(stompMessage.push(message));
			setPlayerList((playerList) => [...playerList, parsed]);
		}
		catch (err)
		{
			console.log("Failed to get message: " + err);
		}
	},[])

	const getStompSession = useCallback(() =>
		{
			return stompProvider.getSession(username, token);
			//return stompProvider(username, token)
			//return stompSession;
		},[stompProvider, username, token]);

	//const startStompSession = useCallback((new_username, new_token) =>
	//{
	//	const ws = new WebSocket("ws://172.17.0.3:61611/ws");
	//	const client = Stomp.over(ws);
	//	const usernameTopic = '/topic/user.' + new_username;
	//	const playerListTopic = '/topic/playerlist';
	//	var on_error;
	//	let pass = new_token.split(' ')[1]
	//	let stomp_headers = {login: new_username, passcode: pass};

	//	try
	//	{
	//		console.log("disconnect stomp");
	//		//const usernameTopic = '/topic/user.' + username;
	//		//const playerListTopic = '/topic/playerlist';
	//		//stompSession.unsubscribe(usernameTopic);
	//		//stompSession.unsubscribe(playerListTopic);
	//		//stompSession.forceDisconnect();
	//		stompSession.deactivate();
	//	}
	//	catch(err)
	//	{
	//	}
	//	try
	//	{
	//		console.log("close ws");
	//		//const usernameTopic = '/topic/user.' + username;
	//		//const playerListTopic = '/topic/playerlist';
	//		//stompSession.unsubscribe(usernameTopic);
	//		//stompSession.unsubscribe(playerListTopic);
	//		//stompSession.forceDisconnect();
	//		webSockSession.close();
	//	}
	//	catch(err)
	//	{
	//	}

	//	// subscribe to stomp topic on web socket connection
	//	var on_connect = function(x) 
	//	{
	//		client.subscribe(
	//			playerListTopic, 
	//			function (msg) {
	//				console.log(playerListTopic + " msg = " + msg);
	//			},
	//			stomp_headers);

	//		client.subscribe(
	//			usernameTopic, 
	//			function (msg) {
	//				console.log(usernameTopic + " msg = " + msg);
	//			},
	//			stomp_headers);
	//	};

	//	var on_close = function()
	//	{
	//		console.log("close connection");
	//	};

	//	// error logging to console
	//	on_error = function(err) 
	//	{
	//		console.log("STOMP Error: " + err);
	//	};

	//	// web socket debug logging
	//	client.debug = function(msg) 
	//	{
	//		console.log("client.debug: " + msg);
	//	}

	//	// connect using guest credentials in header
	//	client.connect(new_username, pass, on_connect, on_error, '/');

	//	// set ws/stomp session after initialized
	//	setWebSockSession(ws);
	//	setStompSession(client);
	//},[])

	////const onConnectTest = useCallback((frame) =>
	////{
	////	//if (token && stompSession)
	////	//{
	////		let pass = token.split(' ')[1]
	////		let stomp_headers = {login: username, passcode: pass};
	////		const usernameTopic = '/topic/user.' + username;
	////		const playerListTopic = '/topic/playerlist';
	////		console.log("onConnect")

	////		const subUsername = stompSession.subscribe(
	////			usernameTopic,
	////			function (msg) {
	////				console.log(usernameTopic + " msg = " + msg);
	////			},
	////			stomp_headers
	////		);

	////		const subPlayerlist = stompSession.subscribe(
	////			playerListTopic,
	////			function (msg) {
	////				console.log(playerListTopic + " msg = " + msg);
	////			},
	////			stomp_headers
	////		);

	////		setUserSubscription(subUsername);
	////		setPlayerlistSubscription(subPlayerlist);
	////	//}
	////},[stompSession, token, username]);

	////const onStompErrorTest = useCallback((frame) =>
	////{
	////	// error encountered at Broker
	////	console.log('Stomp Broker reported error: ' + frame.headers['message']);
	////	console.log('Additional details: ' + frame.body);
	////	//client.unsubscribe(usernameTopic);
	////	//client.unsubscribe(playerListTopic);
	////	//client.deactivate();
	////	//setStompSession(null);
	////},[]);

	////const onWebSocketErrorTest = useCallback((closeEvent) =>
	////{
	////	// error encountered at Broker
	////	console.log('web socket reported error: ' + closeEvent);
	////	//client.unsubscribe(usernameTopic);
	////	//client.unsubscribe(playerListTopic);
	////	//client.deactivate();
	////	//setStompSession(null);
	////},[]);

	////const onWebSocketCloseTest = useCallback((closeEvent) =>
	////{
	////	// error encountered at Broker
	////	console.log('callback closed websocket: ' + closeEvent);
	////	//client.unsubscribe(usernameTopic);
	////	//client.unsubscribe(playerListTopic);
	////	//client.deactivate();
	////	//setStompSession(null);
	////},[]);

	////const onDisconnectTest = useCallback((closeEvent) => 
	////{
	////	// error encountered at Broker
	////	console.log('callback disconnected stomp: ' + closeEvent);
	////	//client.unsubscribe(usernameTopic);
	////	//client.unsubscribe(playerListTopic);
	////	//client.deactivate();
	////	//setStompSession(null);
	////},[]);

	useEffect(() =>
		{
			//// init and save stomp session
			//if (stompSession)
			//{
			//	try
			//	{
			//		console.log("disconnect stomp");
			//		//const stomp_headers = {login: username, passcode: token};
			//		//const usernameTopic = '/topic/user.' + username;
			//		//const playerListTopic = '/topic/playerlist';
			//		//stompSession.unsubscribe(usernameTopic, stomp_headers);
			//		//stompSession.unsubscribe(playerListTopic, stomp_headers);
			//		//playerlistSubscription.unsubscribe();
			//		//userSubscription.unsubscribe();
			//		//stompSession.forceDisconnect();
			//		(async () => {await stompSession.deactivate()})();
			//	}
			//	catch(err)
			//	{
			//	}
			//}

		},[username, token])

	////const startStompSession = useCallback(async () =>
	//useEffect(() =>
	//	{
	//		if (token)
	//		{
	//			let pass = token.split(' ')[1]
	//			//let stompTopic = '/topic/hello';
	//			const stomp_headers = {login: username, passcode: pass};
	//			const usernameTopic = '/topic/user.' + username;
	//			const playerListTopic = '/topic/playerlist';
	//			//console.log("stomp login: " + new_username + " pass: " + pass)

	//			//if (stompSession)
	//			//{
	//			//	// remove existing session
	//			//	try
	//			//	{
	//			//		console.log("startstomp - disconnect current stomp");
	//			//		//const usernameTopic = '/topic/user.' + username;
	//			//		//const playerListTopic = '/topic/playerlist';
	//			//		//playerlistSubscription.unsubscribe();
	//			//		//userSubscription.unsubscribe();
	//			//		//stompSession.unsubscribe(usernameTopic, stomp_headers);
	//			//		//stompSession.unsubscribe(playerListTopic, stomp_headers);

	//			//		//stompSession.forceDisconnect();
	//			//		(async () => 
	//			//		{
	//			//			await stompSession.deactivate();
	//			//			setStompSession(null);
	//			//		})()
	//			//	}
	//			//	catch(err)
	//			//	{
	//			//	}
	//			//}

	//			//const client = new StompJs.Client({
	//			//passcode: token
	//			//login: new_username,
	//			const client = new Client({
	//				brokerURL: 'ws://172.17.0.3:61611/ws',
	//				connectHeaders: {
	//					//login: new_username,
	//					login: username,
	//					passcode: pass,
	//					//'heart-beat': '4000,4000'
	//				},
	//				disconnectHeaders: {
	//					//login: new_username,
	//					login: username,
	//					passcode: pass,
	//					//'heart-beat': '4000,4000'
	//				},

	//				debug: function (str) 
	//				{
	//					console.log(str);
	//				},
	//				reconnectDelay: 5000,
	//				//heartbeatIncoming: 4000,
	//				//heartbeatOutgoing: 4000,
	//			});

	//			//client.onConnect = onConnectTest;
	//			client.onConnect = function (frame) 
	//			{
	//				console.log("onConnect")

	//				const subUsername = client.subscribe(
	//					usernameTopic,
	//					function (msg) {
	//						console.log(usernameTopic + " msg = " + msg);
	//					},
	//					stomp_headers
	//				);

	//				const subPlayerlist = client.subscribe(
	//					playerListTopic,
	//					function (msg) {
	//						console.log(playerListTopic + " msg = " + msg);
	//					},
	//					stomp_headers
	//				);

	//				setUserSubscription(subUsername);
	//				setPlayerlistSubscription(subPlayerlist);
	//			};

	//			//client.onStompError = onStompErrorTest;
	//			client.onStompError = function (frame) {
	//				// error encountered at Broker
	//				console.log('Stomp Broker reported error: ' + frame.headers['message']);
	//				console.log('Additional details: ' + frame.body);
	//				//client.unsubscribe(usernameTopic);
	//				//client.unsubscribe(playerListTopic);
	//				//client.deactivate();
	//				//setStompSession(null);
	//			};

	//			//client.onWebSocketError = onWebSocketErrorTest;
	//			client.onWebSocketError = function (closeEvent) {
	//				// error encountered at Broker
	//				console.log('web socket reported error: ' + closeEvent);
	//				//client.unsubscribe(usernameTopic);
	//				//client.unsubscribe(playerListTopic);
	//				//client.deactivate();
	//				//setStompSession(null);
	//			};

	//			//client.onWebSocketClose = onWebSocketCloseTest;
	//			client.onWebSocketClose = function (closeEvent) {
	//				// error encountered at Broker
	//				console.log('callback closed websocket: ' + closeEvent);
	//				//client.unsubscribe(usernameTopic);
	//				//client.unsubscribe(playerListTopic);
	//				//client.deactivate();
	//				//setStompSession(null);
	//			};

	//			//client.onDisconnect = onDisconnectTest
	//			client.onDisconnect = function (closeEvent) {
	//				// error encountered at Broker
	//				console.log('callback disconnected stomp: ' + closeEvent);
	//				//client.unsubscribe(usernameTopic);
	//				//client.unsubscribe(playerListTopic);
	//				//client.deactivate();
	//				//setStompSession(null);
	//			};

	//			//// use a WebSocket
	//			//client.webSocketFactory = function () {
	//			//  return new WebSocket("ws://broker.329broker.com:15674/ws");
	//			//};

	//			// init and save stomp session
	//			setStompSession(client);
	//		}
	//	},[token, stompSession, username])
	//	//},[token, stompSession, playerlistSubscription, userSubscription, username])
	//	//},[onDisconnectTest, onStompErrorTest, onWebSocketCloseTest, onWebSocketErrorTest, onConnectTest, token, stompSession, playerlistSubscription, userSubscription, username])
	//	//})

	//const connectPlayerList = (currentClient, headerValues) =>
	//{
	//	currentClient.subscribe
	//	(
	//		'/topic/playerList', 
	//		message => {
	//			let newPlayerList = null;
	//			try {
	//				newPlayerList = JSON.parse(message.body)
	//				console.log("Recv playerlist: " + newPlayerList);
	//			}
	//			catch (e) {
	//				console.log("Invalid PlayerList response: " + e)
	//			}

	//			setPlayerList(newPlayerList);
	//		},
	//		headerValues,
	//	)
	//}

	////const playerListMessageHandler = (message) =>
	////{
	////	let newPlayerList = null;

	////	try {
	////		newPlayerList = JSON.parse(message.body)
	////		console.log("Recv playerlist: " + newPlayerList);
	////	}
	////	catch (e) {
	////		console.log("Invalid PlayerList response: " + e)
	////	}

	////	setPlayerList(newPlayerList);
	////}

	////const getPlayerListSession = useCallback(async () => 
	////{
	////	setLoading(true)
	////	if (stompSession == null)
	////	{
	////		var _client = new Client();
	////		var headerValues = {
	////			Authorization: token
	////		}

	////		_client.configure({
	////			brokerURL: 'ws://localhost:8080/stomp',
	////			connectHeaders: headerValues,
	////			onConnect: () => 
	////				{
	////					_client.subscribe
	////					(
	////						'/topic/playerList', 
	////						playerListMessageHandler,
	////						headerValues,
	////					)
	////				},
	////			onStompError: (frame) => {
	////				console.log("stompError: " + frame);
	////			},
	////			// debug messages
	////			//debug: (str) => {
	////			//	console.log(new Date(), str);
	////			//}
	////		});

	////		_client.activate();
	////		setStompSession(_client);
	////	}

	////	await stompSession.connect
	////	await stompSession.publish({destination: '/app/playerList', body: 'test playerList'});
	////	setLoading(false);
	////}, [token, stompSession])

	//message => {
	//	let newPlayerList = null;
	//	try {
	//		newPlayerList = JSON.parse(message.body)
	//		console.log("Recv playerlist: " + newPlayerList);
	//	}
	//	catch (e) {
	//		console.log("Invalid PlayerList response: " + e)
	//	}

	//	setPlayerList(newPlayerList);
	//},

	// use memo for AuthContext to reduce rendering
	const memoedValue = useMemo(
		() => ({
			username,
			token,
			loading,
			isAuthenticated,
			login,
			register,
			logout,
			getSession,
			getStompSession,
			//getPlayerList,
			//getPlayerListSession,
			playerList,
			error,
		}),
		[ username, token, loading, isAuthenticated, login, register, logout, getSession, getStompSession, playerList, error ]
	);

	// render components after initialization
	return (
		<AuthContext.Provider value={memoedValue}>
			{!loadingInitial && children}
		</AuthContext.Provider>
	);
}

// only handle context through useAuth
export default function useAuth() {
	return useContext(AuthContext);
}


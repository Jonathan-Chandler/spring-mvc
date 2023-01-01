import React, { ReactNode, useCallback, useState, useEffect, useMemo, createContext, useContext } from 'react';
import axios, { Axios, AxiosError } from 'axios'
import { LOGIN_API_URL, REGISTER_API_URL } from '../../Constants'
import { IMessage, ActivationState, Stomp, Client } from '@stomp/stompjs';
//import { RxStomp } from '@stomp/rx-stomp';
//import { Client } from '@stomp/stompjs';

interface PlayerListEntry
{
	username: String;
	myRequest: boolean;
	theirRequest: boolean;
}

interface TictactoeGame
{
	xPlayer: String;
	oPlayer: String;
	gameBoard: String;
	
}

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
	playerList: PlayerListEntry[];
	tictactoeGame: TictactoeGame;
	//playerList: {"availableUsers": string[], "requestingUsers": string[], "requestedUsers": string[]};
	error?: any;
}

// shared context
const AuthContext = createContext<AuthContextType>(
	{} as AuthContextType
);

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
	const [playerList, setPlayerList] = useState<PlayerListEntry[]>([]);
	const [tictactoeGame, setTictactoeGame] = useState<TictactoeGame>();
	//const [playerList, setPlayerList] = useState<{"availableUsers": string[], "requestingUsers": string[], "requestedUsers": string[]}>({"availableUsers": [], "requestingUsers": [], "requestedUsers": []});
	const [availablePlayers, setAvailablePlayers] = useState([]);
	const [requestedPlayers, setRequestedPlayers] = useState([]);
	const [requestingPlayers, setRequestingPlayers] = useState([]);
	//const [playerlistSubscription, setPlayerlistSubscription] = useState(null);
	//const [userSubscription, setUserSubscription] = useState(null);
	const [stompClientSession, setStompClientSession] = useState<Client>(new Client());

	//const [stompProvider] = useState<StompProvider>(new StompProvider(null,null));

	//const initializeStomp = (() => 
	//{
	//	 let client = Stomp.client("ws://172.17.0.3:61611/ws")
	//	  // this allows to display debug logs directly on the web page
	//	  client.debug = function(str) {
	//		  console.log("Debug: " + str)
	//	  };
	//	  return client;
	//});

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
		setLoading(true);
		sessionStorage.removeItem("Authorization");
		sessionStorage.removeItem("username");
		setUsername(null);
		setToken(null);
		setError(null);
		setLoading(false);
	}, []);

	// interceptor updates token from response header
	const handleResponseHeaders = (response) => 
	{
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

	const handlePlayerlistRx = useCallback(async (message: IMessage) => {
		console.log("handleplayerlistrx: " + message);
		let messageBody = JSON.parse(message.body);
		let stringMessageBody = JSON.stringify(messageBody);
		let newPlayerListParse = JSON.parse(stringMessageBody);
		let newStringPlayerListParse = JSON.stringify(newPlayerListParse);
		console.log("newPlayerListParse: " + newPlayerListParse)
		console.log("newStringPlayerListParse: " + newStringPlayerListParse)

		//let newPlayerList = JSON.parse(message.body.usernames);
		let newPlayerList = messageBody.usernames;
		console.log("messageBody: " + messageBody);
		console.log("stringMessageBody: " + stringMessageBody);
		console.log("newPlayerList: " + newPlayerList);
		console.log("stringNewPlayerList: " + JSON.stringify(newPlayerList));
		const regularArray = ["abc", "def", "ghi"];
		console.log("regular array: " + regularArray);

		// don't return this username in playerlist
		const index = newPlayerList.indexOf(username)
		if (index > -1)
		{
			newPlayerList.splice(index, 1); // 2nd parameter means remove one item only
			console.log("new player list: " + newPlayerList)
		}

		//setPlayerList(newPlayerList);
		// console.log("jsonparse newPlayerList: " + JSON.parse(newPlayerList)); - not valid json

		//setPlayerList(JSON.stringify(newPlayerList));
		

		//for (var i = 0; i < newPlayerList.counters.length; i++) {
		//	var counter = newPlayerList.counters[i];
		//	console.log(counter.counter_name);
		//}

		//console.log("rx playerlist: " + JSON.stringify(newPlayerList))
		//console.log("rx playerlist: " + newPlayerList)
	},[username]);

	const handleUserRx = useCallback(async (message: IMessage) => {
		console.log("receive message");
		let loginHeader = message.headers["login"];
		let messageType = message.headers["__TypeId__"];
		console.log("loginHeader: " + loginHeader);
		console.log("handleUserRx: message: " + message);
		console.log("messageType: " + messageType);

		if (messageType === "com.jonathan.web.resources.TictactoePlayerListDto")
		{
			console.log("get player list");
			let messageBody = JSON.parse(message.body);
			setAvailablePlayers(messageBody.availableUsers);
			setRequestedPlayers(messageBody.requestedUsers);
			setRequestingPlayers(messageBody.requestingUsers);
			let newPlayerList = [];
			for (let i = 0; i < messageBody.availableUsers.length; i++)
			{
				let currentPlayer = {username: messageBody.availableUsers[i], myRequest: false, theirRequest: false};

				// this user's request to play against current player
				if (messageBody.requestedUsers.includes(currentPlayer.username))
				{

					currentPlayer.myRequest = true;
				}

				// other player has request to play against this user
				if (messageBody.requestingUsers.includes(currentPlayer.username))
				{
					currentPlayer.theirRequest = true;
				}

				console.log("currentPlayer: " + JSON.stringify(currentPlayer));
				newPlayerList.push(currentPlayer)
			}

			console.log("newPlayerList: " + JSON.stringify(newPlayerList));
			setPlayerList(newPlayerList);
			console.log("Message body: " + JSON.stringify(messageBody));
		}

	},[username]);

//interface PlayerListEntry
//{
//	username: String;
//	myRequest: boolean;
//	theirRequest: boolean;
//}


	useEffect(() =>
	{
		if (loading || loadingInitial)
		{
			return;
		}

		// deactivate every time login/password change if active
		if (stompClientSession.state === ActivationState.ACTIVE)
		{
			// wait until deactivate finishes before continuing
			const deactivateStomp = async () => {
				await stompClientSession.deactivate();
			}
			deactivateStomp().catch(console.error);
		}

		//// wait until transition is done
		//function waitForDeactivate(stompClient, callback){
		//	setTimeout(
		//		function () {
		//			if (stompClient.state !== ActivationState.DEACTIVATING) {
		//				console.log("Done deactivating")
		//				if (callback != null){
		//					callback();
		//				}
		//			} else {
		//				console.log("wait for connection...")
		//				waitForDeactivate(stompClient, callback);
		//			}

		//		}, 500); // wait 500ms
		//}
		//
		//if (stompClientSession.state === ActivationState.DEACTIVATING)
		//{
		//	waitForDeactivate(stompClientSession, null);
		//}

		if (username && token)
		{
			const pass = token.split(' ')[1]
			const stompHeaders = {login: username, passcode: pass};
			//const stompHeaders2 = {login: username, passcode: pass, ack: 'client'};
			const usernameTopic = '/topic/user.' + username;
			const toUsernameTopic = '/topic/to.user.' + username;

			stompClientSession.configure({
				brokerURL: "ws://172.17.0.2:61611/ws",
				connectHeaders: stompHeaders,
				onConnect: () => 
					{
					//stompClientSession.subscribe
					//(
					//	fromUsernameTopic,
					//	handleUserRx,
					//	stompHeaders2,
					//)
					stompClientSession.subscribe
					(
						toUsernameTopic,
						handleUserRx,
						stompHeaders,
					)
					//stompClientSession.subscribe
					//(
					//	'/topic/playerlist', 
					//	handlePlayerlistRx,
					//	stompHeaders,
					//)
				},
				onStompError: (frame) => {
					console.log("stompError: " + frame);
				},
				//debug: (frame) => {
				//	console.log("stompDebug: " + frame);
				//},
			});

			stompClientSession.activate();

			// check in every 5 seconds for playerlist
			const interval = setInterval(() => 
				{
					//const data = {message: "test_message"}
					////const data = "test";
					//console.log("send " + data)

					//// Additional headers
					//let pass = token.split(' ')[1]
					//const msg_destination = "/topic/from.user." + username
					////stompSession.send("/topic/user."+username, {}, JSON.stringify(data));
					//stompClientSession.publish({
					//	destination: msg_destination,
					//	body: JSON.stringify(data),
					//	//body: "{\"message\":\"data\"}",
					//	headers: {
					//		login: username, 
					//		passcode: pass, 
					//		"content-type":"application/json", 
					//		"content-encoding":"UTF-8", "__TypeId__":"com.jonathan.web.resources.TestDto"
					//	}
					//});
					////body: JSON.stringify(data),
					//console.log("send body: " + JSON.stringify(data))

					// request
					//let stompSession = getStompSession();
					// Additional headers
					//client.publish({
					//stompSession.send("/topic/user."+username, {}, JSON.stringify(data));
					//stompSession.publish({

					const data = {requestType: 0, requestedUser: "", moveLocation: -1}

					let pass = token.split(' ')[1]
					const msg_destination = "/topic/from.user." + username
					stompClientSession.publish({
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
					console.log("send playerlist refresh to '" + msg_destination + "' - body: " + JSON.stringify(data))
				}, 5000);
			return () => {
				clearInterval(interval);
			};
			//return () => {
			//  stompClientSession.deactivate();
			//}
		}
	},[stompClientSession, username, token, loading, loadingInitial, handleUserRx, handlePlayerlistRx]);


	const getStompSession = useCallback(() =>
	{
		//// wait until transition is done
		//function waitForActivation(stompClient, callback){
		//	setTimeout(
		//		function () {
		//			if (stompClient.state !== ActivationState.DEACTIVATING) {
		//				console.log("Done deactivating")
		//				if (callback != null){
		//					callback();
		//				}
		//			} else {
		//				console.log("wait for connection...")
		//				waitForActivation(stompClient, callback);
		//			}

		//		}, 500); // wait 500ms
		//}

		return stompClientSession;
	},[stompClientSession])

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
			tictactoeGame,
			error,
		}),
		[ username, token, loading, isAuthenticated, login, register, logout, getSession, getStompSession, playerList, tictactoeGame, error ]
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


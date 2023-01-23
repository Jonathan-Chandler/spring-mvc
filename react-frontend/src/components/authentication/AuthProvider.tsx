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
	//lastMoveTimeMs: Number;
	xPlayerName: String;
	oPlayerName: String;
	xPlayerReady: boolean;
	oPlayerReady: boolean;
	gameState: String;
	gameBoard: String;
	gameOverMessage: String;
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
	sendGameRequest: (name: String) => void;
	tictactoeGame: TictactoeGame;
	sendGameCheckIn: () => void;
	sendGameRefresh: () => void;
	sendGameMove: (index: Number) => void;
	sendGameForfeit: () => void;
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
	const [tictactoeGame, setTictactoeGame] = useState<TictactoeGame>({xPlayerName: "", oPlayerName: "", xPlayerReady: true, oPlayerReady: true, gameState: "GAME_OVER_ERROR", gameBoard: "_________", gameOverMessage: ""});
	//const [tictactoeGame, setTictactoeGame] = useState<TictactoeGame>({lastMoveTimeMs: 0, xPlayerName: "", oPlayerName: "", xPlayerReady: true, oPlayerReady: true, gameState: "GAME_OVER_ERROR", gameBoard: "_________", gameOverMessage: ""});
	//const [playerList, setPlayerList] = useState<{"availableUsers": string[], "requestingUsers": string[], "requestedUsers": string[]}>({"availableUsers": [], "requestingUsers": [], "requestedUsers": []});
	//const [availablePlayers, setAvailablePlayers] = useState([]);
	//const [requestedPlayers, setRequestedPlayers] = useState([]);
	//const [requestingPlayers, setRequestingPlayers] = useState([]);
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
						//console.log("success login; token=" + response.data.Authorization)
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

			//console.log("using auth header token: " + token)

			return axiosSession;
		}, [handleResponseAuthFailure, token]);

	const handlePlayerlistRx = useCallback(async (message: IMessage) => {
		//console.log("handleplayerlistrx: " + message);
		let messageBody = JSON.parse(message.body);
		//let stringMessageBody = JSON.stringify(messageBody);
		//let newPlayerListParse = JSON.parse(stringMessageBody);
		//let newStringPlayerListParse = JSON.stringify(newPlayerListParse);
		//console.log("newPlayerListParse: " + newPlayerListParse)
		//console.log("newStringPlayerListParse: " + newStringPlayerListParse)

		//let newPlayerList = JSON.parse(message.body.usernames);
		let newPlayerList = messageBody.usernames;
		//console.log("messageBody: " + messageBody);
		//console.log("stringMessageBody: " + stringMessageBody);
		//console.log("newPlayerList: " + newPlayerList);
		//console.log("stringNewPlayerList: " + JSON.stringify(newPlayerList));

		// don't return this username in playerlist
		const index = newPlayerList.indexOf(username)
		if (index > -1)
		{
			newPlayerList.splice(index, 1); // 2nd parameter means remove one item only
			//console.log("new player list: " + newPlayerList)
		}

	},[username]);

	const handleUserRx = useCallback(async (message: IMessage) => {
		//console.log("receive message");
		//const loginHeader = message.headers["login"];
		const messageType = message.headers["__TypeId__"];
		//console.log("loginHeader: " + loginHeader);
		//console.log("handleUserRx: message: " + message);
		//console.log("messageType: " + messageType);

		if (messageType === "com.jonathan.web.resources.TictactoePlayerListDto")
		{
			//console.log("get player list");
			let newPlayerList = [];
			let messageBody = JSON.parse(message.body);

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

				//console.log("currentPlayer: " + JSON.stringify(currentPlayer));
				newPlayerList.push(currentPlayer)
			}

			//console.log("newPlayerList: " + JSON.stringify(newPlayerList));
			setPlayerList(newPlayerList);
			//console.log("Message body: " + JSON.stringify(messageBody));
		}
		else if (messageType === "com.jonathan.web.resources.TictactoeGameDto")
		{
			// server sent game information
			//console.log("get game state");
			let messageBody = JSON.parse(message.body);

			if (messageBody)
			{
				//lastMoveTimeMs: messageBody.lastMoveTimeMs,
				let currentGame = {
					xPlayerName: messageBody.xplayerName,
					oPlayerName: messageBody.oplayerName,
					xPlayerReady: messageBody.xplayerReady,
					oPlayerReady: messageBody.oplayerReady,
					gameState: messageBody.gameState,
					gameBoard: messageBody.gameBoard,
					gameOverMessage: messageBody.gameOverMessage,
				};

				// converted information
				setTictactoeGame(currentGame);
				//console.log("converted game information: " + JSON.stringify(currentGame));
			}

			// message information
			//console.log("receive game information: " + JSON.stringify(messageBody));
		}

	},[]);

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
				//await stompClientSession.forceDisconnect();
			}

			deactivateStomp().catch(console.error);
		}


		if (username && token)
		{
			const pass = token.split(' ')[1]
			const stompHeaders = {login: username, passcode: pass};
			const usernameTopic = '/topic/user.' + username;
			const toUsernameTopic = '/topic/to.user.' + username;

			stompClientSession.configure({
				brokerURL: "ws://172.18.0.2:61611/ws",
				connectHeaders: stompHeaders,
				onConnect: () => 
				{
					stompClientSession.subscribe
					(
						toUsernameTopic,
						handleUserRx,
						stompHeaders,
					)
				},
				//onStompError: (frame) => {
				//	console.log("stompError: " + frame);
				//},
				//debug: (frame) => {
				//	console.log("stompDebug: " + frame);
				//},
			});

			stompClientSession.activate();

			// check in every 5 seconds for playerlist
			const interval = setInterval(() => 
				{
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
					//console.log("send playerlist refresh to '" + msg_destination + "' - body: " + JSON.stringify(data))
				}, 5000);
			return () => {
				clearInterval(interval);
				//stompClientSession.deactivate();
			};
		}
	},[stompClientSession, username, token, loading, loadingInitial, handleUserRx, handlePlayerlistRx]);

	const sendGameRequest = useCallback((name) => 
	{
			let stompSession = stompClientSession;
			const data = {requestType: 1, requestedUser: name, moveLocation: -1}
			//console.log("send request: " + JSON.stringify(data))

			// Additional headers
			let pass = token.split(' ')[1]
			const msg_destination = "/topic/from.user." + username
			stompSession.publish({
			  destination: msg_destination,
			  body: JSON.stringify(data),
			  headers: {
				  login: username, 
				  passcode: pass, 
				  "content-type":"application/json", 
				  "content-encoding":"UTF-8", 
				  "__TypeId__":"com.jonathan.web.frontend.RequestDto"
			  }
			});
	}, [stompClientSession, username, token]);

	const sendGameRefresh = useCallback(() => 
	{
		//console.log("requesting refresh");
		let stompSession = stompClientSession
		//let stompSession = getStompSession();
		const data = {requestType: 4, requestedUser: username, moveLocation: -1}
		//console.log("send request: " + JSON.stringify(data))

		let pass = token.split(' ')[1]
		const msg_destination = "/topic/from.user." + username
		stompSession.publish({
		  destination: msg_destination,
		  body: JSON.stringify(data),
		  headers: {
			  login: username, 
			  passcode: pass, 
			  "content-type":"application/json", 
			  "content-encoding":"UTF-8", 
			  "__TypeId__":"com.jonathan.web.frontend.RequestDto"
		  }
		});
	}, [stompClientSession, username, token]);

	const sendGameCheckIn = useCallback(() => 
	{
		let stompSession = stompClientSession
		const data = {requestType: 2, requestedUser: username, moveLocation: -1}
		//console.log("send request: " + JSON.stringify(data))

		let pass = token.split(' ')[1]
		const msg_destination = "/topic/from.user." + username
		stompSession.publish({
		  destination: msg_destination,
		  body: JSON.stringify(data),
		  headers: {
			  login: username, 
			  passcode: pass, 
			  "content-type":"application/json", 
			  "content-encoding":"UTF-8", 
			  "__TypeId__":"com.jonathan.web.frontend.RequestDto"
		  }
		});
	}, [stompClientSession, username, token]);

	const sendGameMove = useCallback((index) => 
	{
		//console.log("requesting move index: " + index);
		//let stompSession = getStompSession();
		let stompSession = stompClientSession;
		const data = {requestType: 3, requestedUser: username, moveLocation: index}
		//console.log("send request: " + JSON.stringify(data))

		let pass = token.split(' ')[1]
		const msg_destination = "/topic/from.user." + username
		stompSession.publish({
		  destination: msg_destination,
		  body: JSON.stringify(data),
		  headers: {
			  login: username, 
			  passcode: pass, 
			  "content-type":"application/json", 
			  "content-encoding":"UTF-8", 
			  "__TypeId__":"com.jonathan.web.frontend.RequestDto"
		  }
		});
	}, [stompClientSession, username, token]);

	const sendGameForfeit = useCallback(() => 
	{
		//console.log("requesting forfeit for user: " + username);
		let stompSession = stompClientSession;
		const data = {requestType: 5, requestedUser: username, moveLocation: -1}

		let pass = token.split(' ')[1]
		const msg_destination = "/topic/from.user." + username
		stompSession.publish({
		  destination: msg_destination,
		  body: JSON.stringify(data),
		  headers: {
			  login: username, 
			  passcode: pass, 
			  "content-type":"application/json", 
			  "content-encoding":"UTF-8", 
			  "__TypeId__":"com.jonathan.web.frontend.RequestDto"
		  }
		});

	}, [stompClientSession, username, token]);

	const getStompSession = useCallback(() =>
	{
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
			playerList,
			sendGameRequest,
			tictactoeGame,
			sendGameCheckIn,
			sendGameRefresh,
			sendGameMove,
			sendGameForfeit,
			error,
		}),
		[ username, token, loading, isAuthenticated, login, register, logout, getSession, getStompSession, playerList, sendGameRequest, tictactoeGame, sendGameCheckIn, sendGameRefresh, sendGameMove, sendGameForfeit, error ]
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


import React, { ReactNode, useCallback, useState, useEffect, useMemo, createContext, useContext } from 'react';
import axios, { Axios, AxiosError } from 'axios'
import { LOGIN_API_URL, REGISTER_API_URL } from '../../Constants'
import { Stomp, Client } from '@stomp/stompjs';
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
	//getPlayerListSession: () => void;
	playerList: any;
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
	const [stompSession, setStompSession] = useState(null);
	const [playerList, setPlayerList] = useState([{}]);

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
						startStompSession();
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
	const logout = useCallback(() => {
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
		// update token if exists in response header
		if (response.headers.authorization)
		{
			// update token for session and local storage
			sessionStorage.setItem("Authorization", response.data.Authorization);
			setToken(response.headers.authorization);
			//console.log("captured response header and set auth: " + response.headers.authorization);
		}

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
			return stompSession;
		},[stompSession]);

	const startStompSession = useCallback(() =>
		{
			let stompTopic = '/topic/hello';
			let stomp_headers = {login: 'test_user123', passcode: 'password123'};

			//const client = new StompJs.Client({
			//passcode: token
			//login: username,
			const client = new Client({
				brokerURL: 'ws://172.17.0.3:61611/ws',
				connectHeaders: {
					login: 'test_user123',
					passcode: 'password123',
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

			client.onConnect = function (frame) 
			{
				console.log("onConnect")

				// all subscribes must be done here for reconnect
				client.subscribe(
					stompTopic, 
					onMessage, 
					stomp_headers
				);
			};

			client.onStompError = function (frame) {
				// error encountered at Broker
				console.log('Broker reported error: ' + frame.headers['message']);
				console.log('Additional details: ' + frame.body);
			};

			// init and save stomp session
			client.activate();
			setStompSession(client);
		},[onMessage])

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


//import React, { ReactNode, useCallback, useState, useEffect, useMemo, createContext, useContext } from 'react';
//import axios, { Axios, AxiosError } from 'axios'
////import { PLAYER_LIST_API_URL, LOGIN_API_URL, REGISTER_API_URL } from '../../Constants'
////import useAuth from '../../authentication/AuthProvider';
//import useAuth from '../../authentication/AuthProvider';
//
////import { Client } from '@stomp/stompjs';
//interface PlayerListEntry
//{
//	username: String;
//	myRequest: boolean;
//	theirRequest: boolean;
//}
//
//// context vars/functions
//interface TictactoeContextType {
//	loadingTictactoe: boolean;
//	client: any;
//	getMessages: () => void;
//	sendMessage: () => void;
//	getPlayerList: () => void;
//	playerList: PlayerListEntry[];
//	messages: String;
//	error?: any;
//}
//
//// shared context
//const TictactoeContext = createContext<TictactoeContextType>(
//	{} as TictactoeContextType
//);
//
//// Export the provider as we need to wrap the entire app with it
//export function TictactoeProvider({
//	children,
//}: {
//	children: ReactNode;
//}): JSX.Element {
//	const [loadingInitial, setLoadingInitial] = useState<boolean>(true);
//	const [loadingTictactoe, setLoadingTictactoe] = useState<boolean>(false);
//	const [client, setClient] = useState<any>();
//	const [messages, setMessages] = useState<String>("")
//	const [playerList, setPlayerList] = useState<PlayerListEntry[]>()
//	const [error, setError] = useState<any>();
//	const authContext = useAuth();
//	//const { isAuthenticated, getSession, username, token } = useAuth();
//
//
//	const onMessage = useCallback((msg) => {
//		console.log("msg.body:")
//		if (msg.body)
//			console.log(JSON.parse(msg.body).message);
//			console.log(msg.body)
//		//setMessages(msg);
//	}, []);
//
//	const onDebug = useCallback((err) => {
//		//console.log("onDebug")
//		//console.log(new Date(), err)
//	}, []);
//
//	const onWsConnect = useCallback(() => {
//		//client.subscribe('/topic/messages', onMessage)
//	}, []);
//		//console.log('onConnect');
//
//		
//    // const client = new Client({
//    //   brokerURL: SOCKET_URL,
//    //   reconnectDelay: 5000,
//    //   heartbeatIncoming: 4000,
//    //   heartbeatOutgoing: 4000,
//    //   onConnect: onConnected,
//    //   onDisconnect: onDisconnected
//    // });
//
//	//	//client.subscribe('/queue/now', message => {
//	//	//client.subscribe('/topic/messages', message => {
//	//	client.subscribe('/topic/messages', onMessage)
//	////		//console.log(message);
//	////		//setMessages(message);
//	////		//setState({mess: message.body});
//	////		alert(message.body)
//	////	});
//
//	//	//client.subscribe('/topic/greetings', message => {
//	//	//	console.log(message.body);
//	//	//});
//	//}, 
//	useEffect(() => {
//		if (loadingInitial && !loadingTictactoe && authContext.isAuthenticated())
//		{
//			setLoadingTictactoe(true)
//			var _client = new Client();
//			//var headerValues = {
//			//	Authorization: authContext.token
//			//}
//
//			_client.configure({
//				brokerURL: 'ws://localhost:8080/stomp',
//				onConnect: onWsConnect,
//				onStompError: (frame) => {
//					console.log("stompError: " + frame);},
//				// Helps during debugging, remove in production
//				debug: onDebug
//				//debug: (str) => {
//				//	console.log(new Date(), str);
//				//}
//			});
//
//			_client.activate();
//			setClient(_client);
//
//			// initialize done
//			setLoadingInitial(false)
//			setLoadingTictactoe(false)
//		}
//	}, [authContext, loadingInitial, onMessage, loadingTictactoe, onWsConnect, onDebug]);
//
////	useEffect(() => {
////		if (loadingInitial && !loadingTictactoe && authContext.isAuthenticated())
////		{
////			setLoadingTictactoe(true)
////			var _client = new Client();
////			var headerValues = {
////				Authorization: authContext.token
////			}
////
////			_client.configure({
////				brokerURL: 'ws://localhost:8080/stomp',
////				//onConnect: () => onConnect(),
////				onConnect: 
////					() => {
////						_client.subscribe(
////							'/topic/greetings', 
////							message => {
////								const messageBody = message.body
////								//console.log(JSON.parse(message.body).message);
////								console.log("msg")
////								console.log(messageBody)
////								setMessages(messageBody)
////							},
////							headerValues
////						)
////						_client.subscribe(
////							'/topic/playerList', 
////							message => {
////								var newPlayerList = null;
////								try {
////									newPlayerList = JSON.parse(message.body)
////									console.log("Recv playerlist: " + newPlayerList);
////								}
////								catch (e) {
////									console.log("Invalid PlayerList response: " + e)
////								}
////
////								setPlayerList(newPlayerList)
////							},
////							headerValues
////						)
////					},
////				onStompError: (frame) => {
////					console.log("stompError: " + frame);},
////				// Helps during debugging, remove in production
////				debug: onDebug
////				//debug: (str) => {
////				//	console.log(new Date(), str);
////				//}
////			});
////
////			_client.activate();
////			setClient(_client);
////
////			// initialize done
////			setLoadingInitial(false)
////			setLoadingTictactoe(false)
////		}
////	}, [authContext, loadingInitial, onMessage, loadingTictactoe, onConnect, onDebug]);
//
//	const getMessages = useCallback(async () => {
//		return messages;
//	}, [messages])
//
//	const sendMessage = useCallback(async () => {
//		client.publish({destination: '/app/greetings', body: 'Hello world'});
//	}, [client])
//
//	const getPlayerList = useCallback(async () => {
//		console.log("getplayerlist");
//		if (authContext.isAuthenticated())
//		{
//			let authHeader = {Authorization: authContext.token};
//			let newPlayerList = null;
//			console.log("getplayerlist-auth");
//			client.subscribe(
//				'/topic/playerList', 
//				message => 
//				{
//					try {
//						newPlayerList = JSON.parse(message.body)
//						console.log("Recv playerlist: " + newPlayerList);
//						setPlayerList(newPlayerList)
//					}
//					catch (e) {
//						console.log("Invalid PlayerList response: " + e)
//					}
//				},
//				authHeader
//			)
//		client.publish({destination: '/app/playerList', body: 'test playerList'});
//
//				//const playerList = message.body
//				//console.log("playerList")
//				//console.log(playerList)
//				//console.log(JSON.parse(message.body).message);
//				//setPlayerList(playerList)
//			//})
//		}
//		//client.publish({destination: '/app/greetings', body: 'Hello world'});
//	}, [client, authContext])
//
//	// use memo for AuthContext to reduce rendering
//	const memoedValue = useMemo(
//		() => ({
//			loadingTictactoe,
//			client,
//			getMessages,
//			sendMessage,
//			getPlayerList,
//			playerList,
//			messages,
//			error,
//		}),
//		[ loadingTictactoe, client, getMessages, sendMessage, getPlayerList, playerList, messages, error ]
//	);
//
//	// render components after initialization
//	return (
//		<TictactoeContext.Provider value={memoedValue}>
//			{!loadingInitial && children}
//		</TictactoeContext.Provider>
//	);
//}
//
//// only handle context through useAuth
//export default function useTictactoe() {
//	return useContext(TictactoeContext);
//}
//
//

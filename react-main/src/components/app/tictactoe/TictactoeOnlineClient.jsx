import React, { useState, useEffect } from 'react';
//import { io } from "socket.io-client"

//const socket = io("ws://localhost:3000");
//const socket = io();

//export default function TictactoeOnlineClient() 
//{
//	const [process, setProcess] = useState({});
//}
//	const [messages, setMessages] = useState({});
//	useEffect(() => {
//
//		const messageListener = (message) => 
//		{
//			setMessages((prevMessages) => 
//			{
//				const newMessages = {...prevMessages};
//				newMessages[message.id] = message;
//				return newMessages;
//			});
//		};
//
//		const deleteMessageListener = (messageID) => {
//			setMessages((prevMessages) => {
//				const newMessages = {...prevMessages};
//				delete newMessages[messageID];
//				return newMessages;
//			});
//		};
//
//		socket.on('message', messageListener);
//		socket.on('deleteMessage', deleteMessageListener);
//		socket.emit('getMessages');
//
//		return () => {
//		  socket.off('message', messageListener);
//		  socket.off('connect', deleteMessageListener);
//		};
//	}, [socket]);
//
//	const sendMessage1 = () => {
//		socket.emit('button1');
//	}
//
//	const sendMessage2 = () => {
//		socket.emit("PLAYER_LIST", "username joined");
//	}
//
//	const sendMessage3 = () => {
//		socket.emit('button3');
//	}
//
//	return (
//		<div className="message-list">
//			{[...Object.values(messages)]
//					.sort((a, b) => a.time - b.time)
//					.map((message) => (
//						<div
//							key={message.id}
//							className="message-container"
//							title={`Sent at ${new Date(message.time).toLocaleTimeString()}`}
//						>
//							<span className="user">{message.user.name}:</span>
//							<span className="message">{message.value}</span>
//							<span className="date">{new Date(message.time).toLocaleTimeString()}</span>
//						</div>
//					))
//			}
//		</div>
//	);
//
	//return (
	//	<div>
	//		<p>Connected: { '' + isConnected }</p>
	//		<p>Test Message: { testMessage || '-' }</p>
	//		<p><button onClick={sendMessage1 }>Send message1</button></p>
	//		<p><button onClick={sendMessage2 }>Send message2</button></p>
	//		<p><button onClick={sendMessage3 }>Send message3</button></p>
	//	</div>
	//);

package com.jonathan.web.configuration;

import org.slf4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
//import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
//import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
//import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
//import org.springframework.messaging.simp.config.MessageBrokerRegistry;
//import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.beans.factory.annotation.Autowired;
//import java.nio.file.attribute.UserPrincipal;


@Configuration
//@EnableWebSocketMessageBroker
//@Order(Ordered.HIGHEST_PRECEDENCE + 99)
//public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer
//public class WebSocketConfig implements WebSocketMessageBrokerConfigurer 
public class WebSocketConfig
{
	@Autowired
	Logger logger;

	//@Override
	//public void configureMessageBroker(MessageBrokerRegistry registry) 
	//{
	//	// prefix for endpoint that client will receive messages from
	//	//registry.enableStompBrokerRelay("/queue", "/topic", "/tictactoe");
	//	registry.enableSimpleBroker("/topic");
	//	// stompclient client will listen for messages with prefix:
	//	// stompClient = Stomp.over(socket)
	//	// stompClient.connect({}, {stompClient.subscribe('topic/greetings')

	//	// prefix for endpoint that client will send messages to
	//	registry.setApplicationDestinationPrefixes("/app");
	//	// stompClient.send("/tictactoe/hello", {}, JSON.stringify({'name':$("#name").val()}))
	//}

	//@Override
	//public void registerStompEndpoints(StompEndpointRegistry registry) 
	//{
	//	// endpoint where client socket will connect
	//	//registry.addEndpoint("/tictactoe/playerlist")
	//	registry.addEndpoint("/stomp")
	//		// CORS allowed origin from port 3000
	//		//.setAllowedOrigins("http://localhost:3000")
	//		.setAllowedOrigins("http://localhost:3000");
	//		// sockJs fallback
	//		//.withSockJS();
	//	// var socket = new SockJS('portfolio');
	//}

//	@Override
//	public void configureClientInboundChannel(ChannelRegistration registration) 
//	{
//		registration.setInterceptors(
//			new ChannelInterceptorAdapter() 
//			{
//				@Override
//				public Message<?> preSend(Message<?> message, MessageChannel channel) 
//				{
//					StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
//					if (accessor.getCommand() == StompCommand.SUBSCRIBE) 
//					{
//						LOGGER.log(Level.INFO, String.format("%s: %s", channel, message));
//					}
//					return message;
//				}
//			}
//		);
//	}

	//@Override
	//public void configureClientInboundChannel(ChannelRegistration registration)
	//{
	//}


	// authorize stomp headers for socket connection
	//@Override
	//public void configureClientInboundChannel(ChannelRegistration registration) 
	//{
	//	registration.interceptors(
	//			new ChannelInterceptor() 
	//			{
	//				@Override
	//				public Message<?>  preSend(Message<?> message, MessageChannel channel)
	//				{
	//					StompHeaderAccessor accessor = 
	//						MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class)
	//						if (StompCommand.CONNECT.equals(accessor.getCommand()))
	//						{
	//							Authenication user = // get header
	//							accessor.setUser(user);
	//						}
	//				}
	//			}
	//}

	//@Override
	//public void configureMessageBroker(MessageBrokerRegistry config) 
	//{
	//	config.enableSimpleBroker("/topic");
	//	config.setApplicationDestinationPrefixes("/app");
	//}
}

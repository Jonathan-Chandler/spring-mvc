package com.jonathan.web.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.*;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import static org.springframework.security.config.Customizer.withDefaults;
import org.bouncycastle.crypto.params.Argon2Parameters;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.authentication.ProviderManager;
import com.jonathan.web.dao.UserRepository;

import javax.servlet.http.HttpServletResponse;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import java.security.SecureRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import javax.servlet.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import com.jonathan.web.service.UserDetailsServiceImpl;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsConfiguration;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.jms.connection.CachingConnectionFactory;
import com.jonathan.web.controllers.RSender;



import com.jonathan.web.controllers.TReceiver;

//import com.jonathan.web.filters.JwtTokenFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration
{
	static final String topicExchangeName = "spring-boot-exchange";
	static final String queueName = "spring-boot";

	@Bean
    public DirectExchange direct() 
	{
        return new DirectExchange("tut.direct");
    }

	@Bean
	public MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean public Queue hello() 
	{
		return new Queue(queueName, false);
	}

	@Bean
	TopicExchange exchange() 
	{
		return new TopicExchange(topicExchangeName);
	}

	@Bean
	Binding binding(Queue queue, TopicExchange exchange) 
	{
		return BindingBuilder.bind(queue).to(exchange).with("foo.bar.#");
	}

	//@Bean
	//SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
	//		MessageListenerAdapter listenerAdapter) {
	//	SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
	//	container.setConnectionFactory(connectionFactory);
	//	container.setQueueNames(queueName);
	//	container.setMessageListener(listenerAdapter);
	//	return container;
	//}

	//MessageListenerAdapter listenerAdapter(TReceiver receiver) {
	//@Bean
	//MessageListenerAdapter listenerAdapter(String receiver) {
	//	return new MessageListenerAdapter(receiver, "receiveMessage");
	//}
	//@Bean
	//Binding binding(Queue queue, DirectExchange exchange) {
	//	return BindingBuilder.bind(queue).to(exchange).with("hello");
	//}

	@Bean
	public RSender sender() {
        return new RSender();
    }

	@Bean
	public TReceiver receiver() {
        return new TReceiver();
    }

	//@Autowired
	//private Filter jwtTokenFilter;

	//static final String topicExchangeName = "spring-boot-exchange";
	//static final String queueName = "spring-boot";

	//@Bean
	//public Queue hello() 
	//{
	//	return new Queue("hello");
	//}

	//@Value("127.0.0.1")
    //String host;

    //@Value("guest")
    //String username;

    //@Value("guest")
    //String password;

//    @Bean
//    CachingConnectionFactory connectionFactory() {
//        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory("127.0.0.1");
//        cachingConnectionFactory.setUsername("guest");
//        cachingConnectionFactory.setPassword("guest");
//        return cachingConnectionFactory;
//    }
//
//    @Bean
//    public MessageConverter jsonMessageConverter() {
//        return new Jackson2JsonMessageConverter();
//    }
//
//    @Bean
//    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
//        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//        rabbitTemplate.setMessageConverter(jsonMessageConverter());
//        return rabbitTemplate;
//    }
	
	//@Value("${javainuse.rabbitmq.queue}")
	//String queueName;

	//@Value("${javainuse.rabbitmq.exchange}")
	//String exchange;

	//@Value("${javainuse.rabbitmq.routingkey}")
	//private String routingkey;

	//@Bean
	//Queue queue() {
	//	return new Queue(queueName, false);
	//}

	//@Bean
	//DirectExchange exchange() {
	//	return new DirectExchange(exchange);
	//}


	
	//@Bean
	//public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
	//	final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
	//	rabbitTemplate.setMessageConverter(jsonMessageConverter());
	//	return rabbitTemplate;
	//}

	//@Bean
	//TopicExchange exchange() 
	//{
	//	return new TopicExchange(topicExchangeName);
	//}

	//@Bean
	//Binding binding(Queue queue, TopicExchange exchange) 
	//{
	//	return BindingBuilder.bind(queue).to(exchange).with("foo.bar.#");
	//}

	//@Bean
	//SimpleMessageListenerContainer container
	//		(
	//			ConnectionFactory connectionFactory,
	//			MessageListenerAdapter listenerAdapter
	//		) 
	//{
	//	SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
	//	container.setConnectionFactory(connectionFactory);
	//	container.setQueueNames(queueName);
	//	container.setMessageListener(listenerAdapter);
	//	return container;
	//}

	//@Bean
	//public TReceiver receiver()
	//{
	//	return new TReceiver();
	//}

	//MessageListenerAdapter listenerAdapter(TReceiver receiver) 
	//{
	//	return new MessageListenerAdapter(receiver, "receiveMessage");
	//}



	@Autowired
	private UserDetailsService userDetailsService;

	@Bean
	@Scope("prototype")
	public Logger produceLogger(InjectionPoint injectionPoint) {
		Class<?> classOnWired = injectionPoint.getMember().getDeclaringClass();
		return LoggerFactory.getLogger(classOnWired);
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring()
			//      .antMatchers("/todos/**")
			// don't filter login or register api
			.antMatchers("/login")
			.antMatchers("/tictactoe/*")
			.antMatchers("/tictactoe/playerlist")
			.antMatchers("/tictactoe/playerlist/*")
			.antMatchers("/tictactoe/playerlist/info")
			.antMatchers("/topic/greeting")
			.antMatchers("/app/greeting")
			.antMatchers("/gs-guide-websocket")
			.antMatchers("/topic/gs-guide-websocket")
			.antMatchers("/app/gs-guide-websocket")
			.antMatchers("/topic")
			.antMatchers("/chat")
			.antMatchers("/topic/chat")
			.antMatchers("/topic/messages")
			.antMatchers("/stomp")
			.antMatchers("/stomp/**")
			.antMatchers("/register");
	}

	@Bean
	public AuthenticationProvider daoAuthenticationProvider() throws Exception 
	{
		// DAO Authentication Provider used to get a json web token
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(userDetailsService);
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return daoAuthenticationProvider;
	}

	//@Bean
	//CorsConfigurationSource corsConfigurationSource() {
	//  UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	//  source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
	//  return source;
	//}

	//@Bean
	//public CorsFilter corsFilter() {
	//    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	//    CorsConfiguration config = new CorsConfiguration();
	//    config.setAllowCredentials(true);
	//    config.addAllowedOrigin("*");
	//    config.addAllowedHeader("*");
	//    config.addAllowedMethod("OPTIONS");
	//    config.addAllowedMethod("GET");
	//    config.addAllowedMethod("POST");
	//    config.addAllowedMethod("PUT");
	//    config.addAllowedMethod("DELETE");
	//    source.registerCorsConfiguration("/**", config);
	//    return new CorsFilter(source);
	//}
	//@Bean
	//public WebMvcConfigurer corsConfigurer() {
	//	return new WebMvcConfigurer() {
	//		@Override
	//		public void addCorsMappings(CorsRegistry registry) {
	//			registry.addMapping("/**").allowedOrigins("http://localhost:3000");
	//			registry.addMapping("/todos/**").allowedOrigins("http://localhost:3000");
	//		}
	//	};
	//}

	//@Bean
	//public WebMvcConfigurer corsConfigurer() {
	//	return new WebMvcConfigurer() {
	//		@Override
	//		public void addCorsMappings(CorsRegistry registry) {
	//			registry.addMapping("/**").allowedOrigins("http://localhost:3000");
	//		}
	//	};
	//}

	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowedHeaders(List.of("*","Authorization", "Cache-Control", "Content-Type"));
		corsConfiguration.setAllowedOriginPatterns(List.of("*","http://localhost:3000", "http://localhost:3000/*"));
		corsConfiguration.setAllowedMethods(List.of("*"));
		//corsConfiguration.setAllowedMethods(List.of("POST", "PUT", "DELETE", "PUT","OPTIONS","PATCH", "DELETE"));
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.setExposedHeaders(List.of("Authorization"));

		http.csrf().disable().cors().configurationSource(request -> corsConfiguration);
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		//  .antMatchers("/auth/login", "/docs/**", "/users").permitAll()
		http.authorizeRequests()
			.anyRequest().authenticated();

		// unauthorized if returned any exception
		http.exceptionHandling()
			.authenticationEntryPoint(
					(request, response, ex) -> {
						response.sendError(
								HttpServletResponse.SC_UNAUTHORIZED,
								ex.getMessage()
								);
					}
					);

		// TODO: FILTER
		//// Filter JWT if exists before trying to authenticate with username/password
		//http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
	//  @Bean
	//  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	//    http.authorizeRequests().antMatchers("/public/**").permitAll().anyRequest()
	//      .hasRole("USER").and()
	//      // Possibly more configuration ...
	//      .formLogin() // enable form based log in
	//                   // set permitAll for all URLs associated with Form Login
	//      .permitAll();
	//    return http.build();
	//  }

	// using bcrypt password encoder for all authentication (recommended over argon2 for webapps with ~1 sec auth)
	@Bean
	public PasswordEncoder passwordEncoder() {
		// default is strength=10; range 4-31
		return new BCryptPasswordEncoder(14);
	}
}



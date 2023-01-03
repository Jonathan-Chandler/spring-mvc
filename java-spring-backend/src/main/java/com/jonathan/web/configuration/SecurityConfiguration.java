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
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import com.jonathan.web.controllers.RSender;
//import com.jonathan.web.controllers.TReceiver;
import com.jonathan.web.service.TictactoePlayerListService;
import com.jonathan.web.service.TictactoePlayerListServiceImpl;
import com.jonathan.web.service.TictactoeGameService;
import com.jonathan.web.service.TictactoeGameServiceImpl;
import com.jonathan.web.controllers.TictactoeController;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableWebSecurity
@EnableScheduling
@Profile("production")
public class SecurityConfiguration
{
	//static final String topicExchangeName = "spring-boot-exchange";
	//static final String topicExchangeName = "hello";
	static final String topicExchangeName = "amq.topic";
	static final String queueName = "spring-boot";

	@Bean
	@Scope("singleton")
	public TictactoeGameService tictactoeGameService() {
		return new TictactoeGameServiceImpl();
	}

	@Bean
	@Scope("singleton")
	public TictactoePlayerListService tictactoePlayerListService() {
		return new TictactoePlayerListServiceImpl();
		//return new TictactoePlayerListServiceImpl(tictactoeGameService());
	}

	////@Bean
    ////public TopicExchange playerList() {
    ////    return new TopicExchange("tut.topic");
    ////}

	////@Bean
	////public Queue autoDeleteQueue1() {
	////	return new AnonymousQueue();
	////}

	//@Bean
    //public DirectExchange direct() 
	//{
    //    return new DirectExchange("tut.direct");
    //}

	@Bean
	public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory)
	{
		final var rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(jsonMessageConverter());
		return rabbitTemplate;
	}

	@Bean
	public MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	//@Bean public Queue hello() 
	//{
	//	return new Queue(queueName, false);
	//}

	//@Bean
	//TopicExchange exchange() 
	//{
	//	return new TopicExchange(topicExchangeName);
	//}

	//@Bean
	//Binding binding(Queue queue, TopicExchange exchange) 
	//{
	//	return BindingBuilder.bind(queue).to(exchange).with("user.#");
	//}

	////@Bean
	////public RSender sender() {
    ////    return new RSender();
    ////}
	////@Bean
	////public TReceiver receiver() {
    ////    return new TReceiver();
    ////}

	@Bean
	public TictactoeController receiver() {
        return new TictactoeController();
    }

	@Autowired
	private UserDetailsService userDetailsService;

	//@Bean
	//@Scope("prototype")
	//public Logger produceLogger(InjectionPoint injectionPoint) {
	//	Class<?> classOnWired = injectionPoint.getMember().getDeclaringClass();
	//	return LoggerFactory.getLogger(classOnWired);
	//}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring()
			//      .antMatchers("/todos/**")
			// don't filter login or register api
			.antMatchers("/login")
			.antMatchers("/rabbitmq/user")
			.antMatchers("/rabbitmq/vhost")
			.antMatchers("/rabbitmq/resource")
			.antMatchers("/rabbitmq/topic")
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

	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowedHeaders(List.of("*","Authorization", "Cache-Control", "Content-Type"));
		corsConfiguration.setAllowedOriginPatterns(List.of("*","http://localhost:3000", "http://localhost:3000/*"));
		corsConfiguration.setAllowedMethods(List.of("*"));
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.setExposedHeaders(List.of("Authorization"));

		http.csrf().disable().cors().configurationSource(request -> corsConfiguration);
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.authorizeRequests()
			.anyRequest().authenticated();

		// unauthorized if returned any exception
		http.exceptionHandling()
			.authenticationEntryPoint(
					(request, response, ex) -> 
					{
						response.sendError(
								HttpServletResponse.SC_UNAUTHORIZED,
								ex.getMessage()
								);
					}
				);

		return http.build();
	}

	// using bcrypt password encoder for all authentication (recommended over argon2 for webapps with ~1 sec auth)
	@Bean
	public PasswordEncoder passwordEncoder() {
		// default is strength=10; range 4-31
		return new BCryptPasswordEncoder(14);
	}
}


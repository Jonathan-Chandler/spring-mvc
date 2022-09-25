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

import com.jonathan.web.controllers.authentication.AuthorizationUserDetails;
import com.jonathan.web.controllers.authentication.AuthorizationPasswordService;
import com.jonathan.web.controllers.authentication.CustomAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.authentication.AuthenticationManager;

//import org.springframework.security.oauth2.jwt.JwtDecoder;
//import org.springframework.security.oauth2.jwt.JwtEncoder;
//import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
//import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
//import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
//import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;

import org.springframework.security.authentication.ProviderManager;
//import org.springframework.context.annotation.Lazy;
import com.jonathan.web.dao.UserRepository;

import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration
{
  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) -> web.ignoring()
      // Spring Security should completely ignore URLs starting with /resources/
      .antMatchers("/login/**");
  }
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeRequests().antMatchers("/public/**").permitAll().anyRequest()
      .hasRole("USER").and()
      // Possibly more configuration ...
      .formLogin() // enable form based log in
                   // set permitAll for all URLs associated with Form Login
      .permitAll();
    return http.build();
  }

	// using argon2 password encoder for all authentication
	@Bean
	public PasswordEncoder passwordEncoder() {
		// .949 seconds (second recommended config + 2 iterations)
		// 32 byte(128-bit) salt, 64 byte(256-bit) hash, 4 = degree of parallelism, 64Mb memory (in kibibytes), 5 iterations
		//encoders.put(encodingId, new Argon2PasswordEncoder(32, 64, 4, 65536, 5));
		return new Argon2PasswordEncoder(32, 64, 4, 65536, 5);

		//return new DelegatingPasswordEncoder(encodingId, encoders);
	}

  //@Bean
  //public AuthenticationProvider customAuthenticationProvider()
  //{
  //  return new CustomAuthenticationProvider();
  //}
  //public class CustomAuthenticationProvider implements AuthenticationProvider 

  //@Bean
  //public AuthenticationManager authManager() 
  //{
  //  return AuthenticationManagerBuilder.authenticationProvider(CustomAuthenticationProvider.class).build();
  //  //auth.authenticationProvider(customAuthenticationProvider);

  //    //We can register as many providers as we may have
  //    //auth.authenticationProvider(customProviderTwo);
  //    //auth.authenticationProvider(customProviderThree);
  //}

}

////@EnableWebSecurity
//@Configuration
//class SecurityConfiguration
//{
//	private final AuthorizationUserDetails authorizationUserDetails;
//	private final AuthorizationPasswordService authorizationPasswordService;
//
//  public SecurityConfiguration(
//			AuthorizationUserDetails authorizationUserDetails,
//			AuthorizationPasswordService authorizationPasswordService) {
//    this.authorizationUserDetails = authorizationUserDetails;
//    this.authorizationPasswordService = authorizationPasswordService;
//  }
//
//	// create default dao auth provider
//  @Bean
//  public AuthenticationProvider daoAuthenticationProvider() {
//    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//    provider.setPasswordEncoder(passwordEncoder());
//    provider.setUserDetailsPasswordService(this.authorizationPasswordService);
//    provider.setUserDetailsService(this.authorizationUserDetails);
//    return provider;
//  }
//
//	// require authentication for all pages except registration and login
//	@Bean
//	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//		http
//			.authorizeHttpRequests((authz) -> authz
//					.mvcMatchers("/registration").permitAll()
//					.mvcMatchers("/login").permitAll()
//					.anyRequest().authenticated()
//					)
//			.httpBasic(withDefaults());
//		return http.build();
//	}
//
////	  protected void configure(HttpSecurity http) throws Exception {  // (2)
////      http
////        .authorizeRequests()
////          .antMatchers("/", "/home").permitAll() // (3)
////          .anyRequest().authenticated() // (4)
////          .and()
////       .formLogin() // (5)
////         .loginPage("/login") // (5)
////         .permitAll()
////         .and()
////      .logout() // (6)
////        .permitAll()
////        .and()
////      .httpBasic(); // (7)
////  }
////}
//
//	// ignore authentication for if registering or logging in
//	@Bean
//	public WebSecurityCustomizer webSecurityCustomizer() {
//			return (web) -> web.ignoring().antMatchers("/registration", "/login");
//	}
//
//	// using argon2 password encoder for all authentication
//	@Bean
//	public PasswordEncoder passwordEncoder() {
//		String encodingId = "argon2";
//
//		Map<String, PasswordEncoder> encoders = new HashMap<>();
//
//		// from argon2 RFC - https://www.rfc-editor.org/rfc/rfc9106.html
//		// 7.13 seconds (first recommended config)
//		// 32 byte(128-bit) salt, 64 byte(256-bit) hash, 4 = degree of parallelism, 2Gb memory (in kibibytes), 1 iteration
//		//encoders.put(encodingId, new Argon2PasswordEncoder(32, 64, 4, 2097152, 1));
//
//		// .617 seconds (second recommended config)
//		// 32 byte(128-bit) salt, 64 byte(256-bit) hash, 4 = degree of parallelism, 64Mb memory (in kibibytes), 3 iterations
//		//encoders.put(encodingId, new Argon2PasswordEncoder(32, 64, 4, 65536, 3));
//
//		// .949 seconds (second recommended config + 2 iterations)
//		// 32 byte(128-bit) salt, 64 byte(256-bit) hash, 4 = degree of parallelism, 64Mb memory (in kibibytes), 5 iterations
//		encoders.put(encodingId, new Argon2PasswordEncoder(32, 64, 4, 65536, 5));
//
//		return new DelegatingPasswordEncoder(encodingId, encoders);
//	}
//
///*
//		http
//		// authentication required for all services except registration/login/logout
//		// authentication redirects to "/login" if invalid auth
//			.authorizeRequests()
//				.antMatchers("/registration", "/login").permitAll()
//				.anyRequest().authenticated()
//				.and
//			.formLogin()
//				.loginPage("/login")
//				.permitAll()
//				.and
//			.logout()
//				.permitAll();
//			// allow http basic auth
//			//	.and()
//			//.httpBasic();
//		return http.build();
//*/
//
//}
  //@Override
  //public void configure(WebSecurity web) throws Exception {
  //    web.ignoring().antMatchers("/login");
  //}
	//@Value("${jwt.public.key}")
	//RSAPublicKey key;

	//@Value("${jwt.private.key}")
	//RSAPrivateKey priv;

	//private final AuthorizationUserDetails authorizationUserDetails;
	//private final AuthorizationPasswordService authorizationPasswordService;
	////private final AuthenticationProvider customAuthenticationProvider;

  //public SecurityConfiguration(
	//		AuthorizationUserDetails authorizationUserDetails,
	//		AuthorizationPasswordService authorizationPasswordService)
  //    //AuthenticationProvider customAuthenticationProvider) 
  //{
  //  this.authorizationUserDetails = authorizationUserDetails;
  //  this.authorizationPasswordService = authorizationPasswordService;
  //  //this.customAuthenticationProvider = customAuthenticationProvider;
  //}

  //@Bean 
  //public CustomAuthenticationProvider authProvider() {
  //  CustomAuthenticationProvider authenticationProvider = new CustomAuthenticationProvider();
  //  return authenticationProvider;
  //}
  //@Bean
  //public AuthenticationManager authenticationManager() throws Exception 
  //{
  //  return new AuthenticationManager();
  //}
//;  @Bean
//;  public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception 
//;	{
//;//    // don't require csrf token
//;//		httpSecurity.csrf().disable();
//;//
//;//    // enable cross origin resource sharing security
//;//    httpSecurity.cors();
//;//
//;		//httpSecurity.authorizeHttpRequests(authorize -> authorize
//;    //    // allow unauthenticated users to access register and login pages
//;		//		.antMatchers("/login", "/register").permitAll()
//;    //    // all other requests require authentication
//;		//		.anyRequest().authenticated());
//;    httpSecurity.authorizeRequests()
//;      .anyRequest().authenticated()
//;      .and()
//;      .addFilterBefore(BasicAuthenticationFilter);
//;      //.addFilterBefore(new EAccessAuthenticationFilter(), BasicAuthenticationFilter.class);
//;    //httpSecurity
//;		//// ...
//;		//.authorizeHttpRequests(authorize -> authorize                                  
//;		//	.mvcMatchers("/resources/**", "/login", "/register").permitAll()         
//;		//	.anyRequest().denyAll()
//;		//);
//;		//httpSecurity.authorizeHttpRequests()
//;    //    // allow unauthenticated users to access register and login pages
//;		//		.antMatchers("/login").permitAll()
//;		//		.and().antMatchers("/register").permitAll()
//;		//		.and().anyRequest().permitAll();
//;    //    // all other requests require authentication
//;		//		//.anyRequest().authenticated();
//;
//;    // redirect to login on access denied
//;    //httpSecurity.exceptionHandling().accessDeniedPage("/login");
//;
//;    // use JWT token filtering
//;    //httpSecurity.apply(new JwtTokenFilterConfigurer(jwtTokenProvider);
//;
//;    // disable cross frame origin
//;    //httpSecurity.headers().frameOptions().disable();
//;		return httpSecurity.build();
//;	}
				//.mvcMatchers("/register").anyRequest().authenticated());

  //@Override
  //protected void configure(HttpSecurity httpSecurity) throws Exception 
  //{
  //  // enable cross origin resource sharing security
  //  httpSecurity.cors();

  //  // don't require csrf token
  //  httpSecurity.csrf().disable();

  //  httpSecurity.authorizeRequests() 
  //      // allow unauthenticated users to access register and login pages
  //      .antMatchers("/register").permitAll()
  //      .antMatchers("/login").permitAll()
  //      // all other requests require authentication
  //      .anyRequest().authenticated();

  //  // redirect to login on access denied
  //  //httpSecurity.exceptionHandling().accessDeniedPage("/login");

  //  // use JWT token filtering
  //  //httpSecurity.apply(new JwtTokenFilterConfigurer(jwtTokenProvider);

  //  // disable cross frame origin
  //  //httpSecurity.headers().frameOptions().disable();
  //}

	//@Bean
	//JwtDecoder jwtDecoder() {
	//	return NimbusJwtDecoder.withPublicKey(this.key).build();
	//}

	//@Bean
	//JwtEncoder jwtEncoder() {
	//	JWK jwk = new RSAKey.Builder(this.key).privateKey(this.priv).build();
	//	JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
	//	return new NimbusJwtEncoder(jwks);
	//}

		//String encodingId = "argon2";

		//Map<String, PasswordEncoder> encoders = new HashMap<>();

		// from argon2 RFC - https://www.rfc-editor.org/rfc/rfc9106.html
		// 7.13 seconds (first recommended config)
		// 32 byte(128-bit) salt, 64 byte(256-bit) hash, 4 = degree of parallelism, 2Gb memory (in kibibytes), 1 iteration
		//encoders.put(encodingId, new Argon2PasswordEncoder(32, 64, 4, 2097152, 1));

		// .617 seconds (second recommended config)
		// 32 byte(128-bit) salt, 64 byte(256-bit) hash, 4 = degree of parallelism, 64Mb memory (in kibibytes), 3 iterations
		//encoders.put(encodingId, new Argon2PasswordEncoder(32, 64, 4, 65536, 3));

  //@Bean
  //public ProviderManager providerManager()
  //{
  //  List<AuthenticationProvider> authenticationProviders = 
  //    new ArrayList<AuthenticationProvider>(Arrays.asList(customAuthenticationProvider));
  //  //Arrays.asList(customAuthenticationProvider);
  //  return new ProviderManager(authenticationProviders);
  //}

  //@Override
  //protected void configure(AuthenticationManagerBuilder auth) throws Exception 
  //{
  //  auth.authenticationProvider(authProvider());
  //}

  //@Bean
  //public AuthenticationProvider daoAuthenticationProvider() 
  //{
  //  return customAuthenticationProvider;
  //}

	// create default dao auth provider
  //@Bean
  //public AuthenticationProvider daoAuthenticationProvider() {
  //  DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
  //  provider.setPasswordEncoder(passwordEncoder());
  //  provider.setUserDetailsPasswordService(this.authorizationPasswordService);
  //  provider.setUserDetailsService(this.authorizationUserDetails);
  //  return provider;
  //}

	//@Bean
	//public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	//	// @formatter:off
	//	http
	//			.authorizeHttpRequests((authorize) -> authorize
	//					.anyRequest().authenticated()
	//			)
	//			.csrf((csrf) -> csrf.ignoringAntMatchers("/token"))
	//			.httpBasic(Customizer.withDefaults())
	//			.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
	//			.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	//			.exceptionHandling((exceptions) -> exceptions
	//					.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
	//					.accessDeniedHandler(new BearerTokenAccessDeniedHandler())
	//			);
	//	// @formatter:on
	//	return http.build();
	//}

	//@Bean
	//UserDetailsService users() {
	//	// @formatter:off
	//	return new InMemoryUserDetailsManager(
	//		User.withUsername("user")
	//			.password("{noop}password")
	//			.authorities("app")
	//			.build()
	//	);
	//	// @formatter:on
	//}


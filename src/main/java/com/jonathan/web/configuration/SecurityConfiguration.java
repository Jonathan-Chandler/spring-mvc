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
import com.jonathan.web.filters.JwtTokenFilter;
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

@Configuration
@EnableWebSecurity
public class SecurityConfiguration
{
  @Autowired
  private Filter jwtTokenFilter;

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
	//public WebMvcConfigurer corsConfigurer() {
	//	return new WebMvcConfigurer() {
	//		@Override
	//		public void addCorsMappings(CorsRegistry registry) {
	//			registry.addMapping("/**").allowedOrigins("http://localhost:3000");
	//			registry.addMapping("/todos/**").allowedOrigins("http://localhost:3000");
	//		}
	//	};
	//}

  @Bean
  public SecurityFilterChain configure(HttpSecurity http) throws Exception {
    http.csrf().disable();
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

    // Filter JWT if exists before trying to authenticate with username/password
    http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

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



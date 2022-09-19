package com.jonathan.web.configuration;

//import io.reflectoring.passwordencoding.authentication.DatabaseUserDetailPasswordService;
//import io.reflectoring.passwordencoding.authentication.DatabaseUserDetailsService;
//import io.reflectoring.passwordencoding.workfactor.BcCryptWorkFactorService;
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
import static org.springframework.security.config.Customizer.withDefaults;
import org.bouncycastle.crypto.params.Argon2Parameters;

import java.util.HashMap;
import java.util.Map;

//@EnableWebSecurity
@Configuration
class SecurityConfiguration
{
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests((authz) -> authz
					.mvcMatchers("/registration").permitAll()
					.mvcMatchers("/login").permitAll()
					.anyRequest().authenticated()
					)
			.httpBasic(withDefaults());
		return http.build();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
			return (web) -> web.ignoring().antMatchers("/registration", "/login");
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		// we must use deprecated encoder to support their encoding
		String encodingId = "argon2";

		Map<String, PasswordEncoder> encoders = new HashMap<>();

		// 3 sec
		// 32 byte salt, 64 byte hash, 1 parallelism, 128M memory, 10 iterations

		// 1.2 sec
		// 32 byte salt, 64 byte hash, 1 parallelism, 128M memory, 3 iterations
		encoders.put(encodingId, new Argon2PasswordEncoder(32, 64, 4, 131072, 3));
		//encoders.put(encodingId, new Argon2PasswordEncoder());

		return new DelegatingPasswordEncoder(encodingId, encoders);
	}
}

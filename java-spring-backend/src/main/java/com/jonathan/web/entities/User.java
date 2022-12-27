package com.jonathan.web.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Collection;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user")
public class User implements UserDetails
{
	@Id
	@NotEmpty
	@Column(unique=true, name="id")
	//@Size(min=8, max=50)
	private int id;

	@NotEmpty
	@Column(unique=true, name="username", length=36)
	@Size(min=8, max=36)
	private String username;

	@NotEmpty
	@Column(unique=true, name="email", length=254)
	@Size(min=8, max=254)
	private String email;

	@NotEmpty
	@Column(name="password")
	private String password;

	@Column(name="active")
	private boolean enabled;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> authorities = new HashSet<>();
		SimpleGrantedAuthority userAuth = new SimpleGrantedAuthority("User");
		authorities.add(userAuth);

		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	//@ElementCollection
	//@JoinTable(
	//    name = "authorities",
	//    joinColumns = {@JoinColumn(name = "username")})
	//@Column(name = "authority")
	//private Set<String> roles;
}

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
@Table(name = "tictactoe_game")
public class TictactoeGame
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int game_id;

	@Column(name="player1")
	private int player1;

	@Column(name="player2")
	private int player2;

	// false for player 1 / true for player 2
	@Column(name="active_player")
	private boolean active_player;

	// false for games that can be resumed
	@Column(name="realtime_game")
	private boolean realtime_game;

	@Column(name="start_time")
	private java.sql.Date start_time;

	// 9-character string showing board state
	@Column(name="board", length=9)
	@Size(min=9, max=9)
	private String board;
}

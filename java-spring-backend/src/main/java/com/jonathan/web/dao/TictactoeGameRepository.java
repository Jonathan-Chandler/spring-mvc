package com.jonathan.web.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jonathan.web.entities.User;
import com.jonathan.web.entities.TictactoeGame;

import org.springframework.data.jpa.repository.JpaRepository;

@Service
public interface TictactoeGameRepository extends JpaRepository<User, Integer>
{
}


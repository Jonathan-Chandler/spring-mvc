package com.jonathan.web.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jonathan.web.entities.UserData;

import org.springframework.data.jpa.repository.JpaRepository;
import com.jonathan.web.resources.UserLoginDto;

@Service
public interface UserRepository extends JpaRepository<UserData, String>
{
  UserLoginDto findByUsername(String username);
}


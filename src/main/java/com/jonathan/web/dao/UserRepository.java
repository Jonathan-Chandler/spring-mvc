package com.jonathan.web.dao;

import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jonathan.web.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;
import com.jonathan.web.resources.UserLoginDto;

@Service
public interface UserRepository extends JpaRepository<User, String>
{
  Optional<User> findOneByUsername(String username);
}


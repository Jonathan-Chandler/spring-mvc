package com.jonathan.web.service;

import java.util.List;

import com.jonathan.web.entities.Todo;

public interface TodoService
{
  public List<Todo> findAll();

  public Todo findById(int id);

  public void save(Todo todo);

  public void deleteById(int id);
}


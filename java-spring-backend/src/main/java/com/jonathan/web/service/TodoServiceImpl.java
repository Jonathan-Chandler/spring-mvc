package com.jonathan.web.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.jonathan.web.service.TodoService;
import com.jonathan.web.dao.TodoRepository;
import com.jonathan.web.entities.Todo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TodoServiceImpl implements TodoService
{
  private TodoRepository todoRepository;

  //@Autowired
  //private Logger logger;
  final Logger logger = LoggerFactory.getLogger(this.getClass());


  @Autowired
  public TodoServiceImpl(TodoRepository todoRepository)
  {
    this.todoRepository = todoRepository;
  }

  @Override
  public List<Todo> findAll()
  {
    List<Todo> todos = todoRepository.findAll();
    return todos;
  }

  @Override
  public Todo findById(int id)
  {
    Optional<Todo> result = todoRepository.findById(id);
    Todo todo = null;

    if (result.isPresent())
    {
      todo = result.get();
    }
    else
    {
      throw new RuntimeException("Fail to find todo with id " + id);
    }

    return todo;
  }

  @Override
  public void save(Todo todo)
  {
    todoRepository.save(todo);
  }

  @Override
  public void deleteById(int id)
  {
    todoRepository.deleteById(id);
  }
}



package com.jonathan.web.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.jonathan.web.entities.Todo;
import com.jonathan.web.service.TodoService;

@RestController
@CrossOrigin(origins = "http://localhost:3000/todos")
public class TodoController
{
  @Autowired
  private TodoService todoService;

  @GetMapping("/todos")
  public List<Todo> findAll()
  {
    System.out.println("Todos - find all");
    return todoService.findAll();
  }

  @GetMapping("/todo/{todoId}")
  public Todo getTodo(@PathVariable int todoId)
  {
    Todo todo = todoService.findById(todoId);
    if (todo == null)
      throw new RuntimeException("Todo id not found " + todoId);

    return todo;
  }

  @PostMapping("/todos")
  public Todo addTodo(@RequestBody Todo todo)
  {
    todo.setId(0);

    todoService.save(todo);

    return todo;
  }

  @PutMapping("/todos")
  public Todo updateTodo(@RequestBody Todo todo)
  {
    todoService.save(todo);

    return todo;
  }

  @DeleteMapping("/todos/{todoId}")
  public String deleteTodo(@PathVariable int id)
  {
    Todo todo = todoService.findById(id);

    if (todo == null)
      throw new RuntimeException("Deleted todo id not found " + id);

    todoService.deleteById(id);

    return "Deleted todo id " + id;
  }
}


package com.jonathan.web.service;

import java.util.List;

import com.jonathan.web.entities.Employee;

public interface EmployeeService
{
  public List<Employee> findAll();

  public Employee findById(int id);

  public void save(Employee employee);

  public void deleteById(int id);
}


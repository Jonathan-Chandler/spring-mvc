//package com.jonathan.web.service;
//
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Service;
//
//import com.jonathan.web.service.EmployeeService;
//import com.jonathan.web.dao.EmployeeRepository;
//import com.jonathan.web.entities.Employee;
//
//@Service
//public class EmployeeServiceImpl implements EmployeeService
//{
//  private EmployeeRepository employeeRepository;
//
//  @Autowired
//  public EmployeeServiceImpl(EmployeeRepository employeeRepository)
//  {
//    this.employeeRepository = employeeRepository;
//  }
//
//  @Override
//  public List<Employee> findAll()
//  {
//    return employeeRepository.findAll();
//  }
//
//  @Override
//  public Employee findById(int id)
//  {
//    Optional<Employee> result = employeeRepository.findById(id);
//    Employee employee = null;
//
//    if (result.isPresent())
//    {
//      employee = result.get();
//    }
//    else
//    {
//      throw new RuntimeException("Fail to find employee with id " + id);
//    }
//
//    return employee;
//  }
//
//  @Override
//  public void save(Employee employee)
//  {
//    employeeRepository.save(employee);
//  }
//
//  @Override
//  public void deleteById(int id)
//  {
//    employeeRepository.deleteById(id);
//  }
//}
//

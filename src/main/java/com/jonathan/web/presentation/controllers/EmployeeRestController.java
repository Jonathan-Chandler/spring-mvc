//package com.jonathan.web.presentation.controllers;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestBody;
//
//import com.jonathan.web.entities.Employee;
//import com.jonathan.web.service.EmployeeService;
//
//@RestController
//@CrossOrigin(origins="http://localhost:3000")
//@RequestMapping("/api")
//public class EmployeeRestController
//{
//  private EmployeeService employeeService;
//
//  @Autowired
//  EmployeeRestController(EmployeeService employeeService)
//  {
//    this.employeeService = employeeService;
//  }
//
//  @GetMapping("/employees")
//  public List<Employee> findAll()
//  {
//    return employeeService.findAll();
//  }
//
//  @GetMapping("/employees/{employeeId}")
//  public Employee getEmployee(@PathVariable int employeeId)
//  {
//    Employee employee = employeeService.findById(employeeId);
//    if (employee == null)
//      throw new RuntimeException("Employee id not found " + employeeId);
//
//    return employee;
//  }
//
//  @PostMapping("/employees")
//  public Employee addEmployee(@RequestBody Employee employee)
//  {
//    employee.setId(0);
//
//    employeeService.save(employee);
//
//    return employee;
//  }
//
//  @PutMapping("/employees")
//  public Employee updateEmployee(@RequestBody Employee employee)
//  {
//    employeeService.save(employee);
//
//    return employee;
//  }
//
//  @DeleteMapping("/employees/{employeeId}")
//  public String deleteEmployee(@PathVariable int employeeId)
//  {
//    Employee employee = employeeService.findById(employeeId);
//
//    if (employee == null)
//      throw new RuntimeException("Deleted employee id not found " + employeeId);
//
//    employeeService.deleteById(employeeId);
//
//    return "Deleted employee id " + employeeId;
//  }
//}

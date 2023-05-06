package com.company.foreignTradeOperationsWebApp.controllers;

import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/")
public class EmployeeController {

//    @Autowired
//    private EmployeeRepository employeeRepository;
//
//    @GetMapping("/employees")
//    public List<Employee> getAllEmployees(){
//        return employeeRepository.findAll();
//    }
//
//    @PostMapping("/employees")
//    public Employee createEmployee(@RequestBody Employee employee){
//        return employeeRepository.save(employee);
//    }
}

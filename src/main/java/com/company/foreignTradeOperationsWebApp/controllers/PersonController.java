package com.company.foreignTradeOperationsWebApp.controllers;

import com.company.foreignTradeOperationsWebApp.models.PersonEntity;
import com.company.foreignTradeOperationsWebApp.payloads.response.MessageResponse;
import com.company.foreignTradeOperationsWebApp.repositories.PersonRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/employees")
public class PersonController {
    @Autowired
    PersonRepository personRepository;

    @GetMapping("")
    public ResponseEntity<List<PersonEntity>> getAllEmployees(){
        List<PersonEntity> employees = personRepository.findAll();

        if (employees.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("{id}")
    public ResponseEntity<PersonEntity> deleteEmployee(@PathVariable("id") Long id){
        PersonEntity employee = personRepository.findById(id).orElse(null);

        if (employee == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        personRepository.deleteById(id);
        System.out.println("Employee to be deleted: " + employee);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }


    @PostMapping("")
    public ResponseEntity<?> addEmployee(@RequestBody PersonEntity employee) {
        if (employee == null){
            return new ResponseEntity<>(new MessageResponse("Некорректное тело запроса!"), HttpStatus.BAD_REQUEST);
        }

        System.out.println(employee);

        if (personRepository.existsByWorkEmail(employee.getWorkEmail())) {
            return new ResponseEntity<>(new MessageResponse("Ошибка: Данная почта уже привязана к другому сотруднику!"), HttpStatus.BAD_REQUEST);
        }

        PersonEntity newEmployee = new PersonEntity(employee.getSurname(), employee.getName(), employee.getPatronymic(), employee.getWorkEmail());
        personRepository.save(newEmployee);

        return ResponseEntity.ok(new MessageResponse("Сотрудник успешно добавлен в систему!"));
    }

    @PutMapping (value = "")
    public ResponseEntity<?> updateEmployee(@RequestBody PersonEntity employee) {

        if (employee == null){
            return new ResponseEntity<>(new MessageResponse("Некорректное тело запроса!"), HttpStatus.BAD_REQUEST);
        }

        try{

            PersonEntity updatingEmployee = personRepository.findById(employee.getPersonId()).orElse(null);
            if(updatingEmployee == null){
                throw new Exception();
            }

            personRepository.save(updatingEmployee);
            System.out.println("Employee was updated: " + employee);
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new MessageResponse("Не удалось изменить данные сотрудника!"), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(employee, HttpStatus.OK);
    }
}

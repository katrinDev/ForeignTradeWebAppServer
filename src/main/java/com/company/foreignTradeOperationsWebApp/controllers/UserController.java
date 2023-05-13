package com.company.foreignTradeOperationsWebApp.controllers;

import com.company.foreignTradeOperationsWebApp.models.RoleEntity;
import com.company.foreignTradeOperationsWebApp.models.UserEntity;

import com.company.foreignTradeOperationsWebApp.payloads.response.MessageResponse;
import com.company.foreignTradeOperationsWebApp.repositories.PersonRepository;
import com.company.foreignTradeOperationsWebApp.repositories.RoleRepository;
import com.company.foreignTradeOperationsWebApp.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    RoleRepository roleRepository;

    @GetMapping("")
    public ResponseEntity<List<UserEntity>> getAllUsers(){
        List<UserEntity> users = userRepository.findAll();

        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("{id}")
    public ResponseEntity<UserEntity> deleteUser(@PathVariable("id") Long id){
        UserEntity user = userRepository.findByUserId(id).orElse(null);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userRepository.deleteByUserId(id);
        System.out.println("User to be deleted: " + user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping (value = "")
    public ResponseEntity<?> updateUser(@RequestBody UserEntity user) {

        if (user == null){
            return new ResponseEntity<>(new MessageResponse("Некорректное тело запроса!"), HttpStatus.BAD_REQUEST);
        }

        try{
            System.out.println(user);

            UserEntity updatingUser = userRepository.findByUserId(user.getUserId()).orElse(null);
            if(updatingUser == null){
                throw new Exception();
            }
            updatingUser.setRole(roleRepository.findByRoleName(user.getRole().getRoleName()));

            userRepository.save(updatingUser);
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new MessageResponse("Не удалось изменить пользователя!"), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}

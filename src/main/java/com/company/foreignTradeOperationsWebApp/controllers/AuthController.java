package com.company.foreignTradeOperationsWebApp.controllers;

import com.company.foreignTradeOperationsWebApp.models.UserEntity;
import com.company.foreignTradeOperationsWebApp.models.enums.RoleEnum;
import com.company.foreignTradeOperationsWebApp.repositories.PersonRepository;
import com.company.foreignTradeOperationsWebApp.repositories.RoleRepository;
import com.company.foreignTradeOperationsWebApp.repositories.UserRepository;

import com.company.foreignTradeOperationsWebApp.payloads.request.LogInRequest;
import com.company.foreignTradeOperationsWebApp.payloads.request.SignUpRequest;
import com.company.foreignTradeOperationsWebApp.payloads.response.MessageResponse;
import com.company.foreignTradeOperationsWebApp.security.HashUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    RoleRepository roleRepository;

    @PostMapping("/sign-in")
    public ResponseEntity<?> authenticateUser(@Validated @RequestBody LogInRequest loginRequest) {

        System.out.println(loginRequest);

        if (loginRequest == null){
            return new ResponseEntity<>(new MessageResponse("Некорректное тело запроса!"), HttpStatus.BAD_REQUEST);
        }

        UserEntity currentUser = userRepository.findByUsername(loginRequest.getUsername()).orElse(null);

        MessageResponse responseMessage = new MessageResponse();

        if(currentUser != null){
            if(!HashUtils.verifyPassword(currentUser.getPassword(), loginRequest.getPassword())){
                responseMessage.setMessage("Некорректный пароль!");
            } else {
                return new ResponseEntity<>(currentUser, HttpStatus.OK);
            }
        } else {
            responseMessage.setMessage("Пользователя с таким логином не существует!");
        }

        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest) {
        if (signUpRequest == null){
            return new ResponseEntity<>(new MessageResponse("Некорректное тело запроса!"), HttpStatus.BAD_REQUEST);
        }

        System.out.println(signUpRequest);
        if (!personRepository.existsByWorkEmail(signUpRequest.getWorkEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Ошибка: Данного сотрудника нет в системе!"));
        }

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<>(new MessageResponse("Ошибка: Пользователь с данным логином существует!"), HttpStatus.BAD_REQUEST);
        }

        // Create new user's account
        UserEntity user = new UserEntity(signUpRequest.getUsername(), HashUtils.sha256(signUpRequest.getPassword()),
                roleRepository.findByRoleName(RoleEnum.ROLE_USER), personRepository.findByWorkEmail(signUpRequest.getWorkEmail()));

        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Пользователь успешно зарегистрирован!"));
    }
}

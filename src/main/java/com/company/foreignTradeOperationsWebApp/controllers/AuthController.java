package com.company.foreignTradeOperationsWebApp.controllers;


import com.company.foreignTradeOperationsWebApp.models.RoleEntity;
import com.company.foreignTradeOperationsWebApp.models.UserEntity;
import com.company.foreignTradeOperationsWebApp.models.enums.RoleEnum;
import com.company.foreignTradeOperationsWebApp.repositories.PersonRepository;
import com.company.foreignTradeOperationsWebApp.repositories.RoleRepository;
import com.company.foreignTradeOperationsWebApp.repositories.UserRepository;
import com.company.foreignTradeOperationsWebApp.secutiry.services.UserDetailsImpl;
import com.company.foreignTradeOperationsWebApp.secutiry.jwt.JwtUtils;
import com.company.foreignTradeOperationsWebApp.secutiry.payloads.request.LogInRequest;
import com.company.foreignTradeOperationsWebApp.secutiry.payloads.request.SignUpRequest;
import com.company.foreignTradeOperationsWebApp.secutiry.payloads.response.JwtResponse;
import com.company.foreignTradeOperationsWebApp.secutiry.payloads.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/sign-in")
    public ResponseEntity<?> authenticateUser(@Validated @RequestBody LogInRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        //set authentication to security context to use info about authenticated user
        //later from any part of the application

        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getUserId(),
                userDetails.getUsername(),
                userDetails.getRole()));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> registerUser(@Validated @RequestBody SignUpRequest signUpRequest) {
        if (!personRepository.existsByWorkEmail(signUpRequest.getWorkEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Ошибка: Данного сотрудника нет в системе!"));
        }

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Ошибка: Пользователь с данным логином существует!"));
        }

        // Create new user's account
        UserEntity user = new UserEntity(signUpRequest.getUsername(),  encoder.encode(signUpRequest.getPassword()),
                roleRepository.findByRoleName(RoleEnum.ROLE_USER), personRepository.findByWorkEmail(signUpRequest.getWorkEmail()));

        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Пользователь успешно зарегистрирован!"));
    }
}

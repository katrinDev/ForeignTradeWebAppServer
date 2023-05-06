package com.company.foreignTradeOperationsWebApp.secutiry.services;

import com.company.foreignTradeOperationsWebApp.models.UserEntity;
import com.company.foreignTradeOperationsWebApp.repositories.UserRepository;
import com.company.foreignTradeOperationsWebApp.secutiry.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с логином " + username + " не найден!"));

        return UserDetailsImpl.build(user);
    }

}

package com.company.foreignTradeOperationsWebApp.secutiry.services;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.company.foreignTradeOperationsWebApp.models.UserEntity;
import com.company.foreignTradeOperationsWebApp.models.enums.RoleEnum;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
public class UserDetailsImpl implements UserDetails{
    private static final long serialVersionUID = 1L;

    private Long userId;

    private String username;

    @JsonIgnore
    private String password;

    private RoleEnum role;

    public UserDetailsImpl(Long userId, String username, String password, RoleEnum role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public static UserDetailsImpl build(UserEntity user) {

        return new UserDetailsImpl(
                user.getUserId(),
                user.getUsername(),
                user.getPassword(),
                user.getRole().getRoleName());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities () {
        List<GrantedAuthority> list = new ArrayList<>();
        list.add (new SimpleGrantedAuthority (role.name()));
        return list;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(userId, user.userId);
    }
}

package com.kapuza.springSecurity.security;

import com.kapuza.springSecurity.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {

    private final User user;
    private static User userStatic;

    @Autowired
    public UserDetailsImpl(User user) {
        this.user = user;
        userStatic = user;
    }

    /**
     * Возвращаем роль или список действий
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }

    /**
     * проверка, что срок годности аккаунта не вышел
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * аккаунт не заблокирован
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * пароль не просрочен
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * аккаунт в рабочем состоянии
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Для получения данных аутентифицированного пользователя
     */
    public static User getUser() {
        return userStatic;
    }
}
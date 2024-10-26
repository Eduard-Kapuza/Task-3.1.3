package com.kapuza.springSecurity.security;

import com.kapuza.springSecurity.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class PersonDetails implements UserDetails {

    private final Person person;
    private static Person personStatic;

    @Autowired
    public PersonDetails(Person person) {
        this.person = person;
        personStatic = person;
    }

    /**
     * Возвращаем роль или список действий
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return person.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.person.getPassword();
    }

    @Override
    public String getUsername() {
        return this.person.getUsername();
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
    public static Person getPerson() {
        return personStatic;
    }
}
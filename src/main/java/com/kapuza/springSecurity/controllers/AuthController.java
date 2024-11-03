package com.kapuza.springSecurity.controllers;

import com.kapuza.springSecurity.models.User;
import com.kapuza.springSecurity.services.userService.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    public AuthController(UserDetailsServiceImpl userDetailsServiceImpl) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    @GetMapping("/login")
    public UserDetails loginPageData(@RequestBody User user) {
        log.info("\u001B[33m Что пришло с формы логина: " + user + "\u001B[0m");
        return userDetailsServiceImpl.loadUserByUsername(user.getUsername());
    }
}
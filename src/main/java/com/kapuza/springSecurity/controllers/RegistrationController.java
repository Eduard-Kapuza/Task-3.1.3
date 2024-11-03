package com.kapuza.springSecurity.controllers;

import com.kapuza.springSecurity.models.User;
import com.kapuza.springSecurity.services.registrationService.RegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/registration")
public class RegistrationController {

    private final RegistrationService registrationService;


    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    /**
     * registration new user
     */
    @PostMapping("/")
    public User registration(@RequestBody User user) {
        log.info("\u001B[33m Что пришло с формы регистрации: " + user + "\u001B[0m");
        return registrationService.register(user);
    }
}
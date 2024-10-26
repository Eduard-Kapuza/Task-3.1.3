package com.kapuza.springSecurity.controllers;

import com.kapuza.springSecurity.models.Person;
import com.kapuza.springSecurity.services.personService.PersonDetailService;
import com.kapuza.springSecurity.services.registrationService.RegistrationService;
import com.kapuza.springSecurity.util.PersonValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final PersonValidator personValidator;
    private final RegistrationService registrationService;
    private final PersonDetailService personDetailService;

    @Autowired
    public AuthController(PersonValidator personValidator, RegistrationService registrationService, PersonDetailService personDetailService) {
        this.personValidator = personValidator;
        this.registrationService = registrationService;

        this.personDetailService = personDetailService;
    }

    /**
     * Переход на страницу аутентификации
     */
    @GetMapping("/login")
    public String loginPage(@ModelAttribute("person") Person person) {
        return "auth/login";
    }

    @PostMapping("/login")
    public String loginPageData(@ModelAttribute("person") Person person) {
        personDetailService.loadUserByUsername(person.getUsername());
        return "/admin/persons";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("person") Person person) {
        return "/auth/registration";
    }

    @PostMapping("/registration")
    public String formRegistrationPage(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            return "/auth/registration";
        }
        registrationService.register(person);
        return "/auth/login";
    }
}
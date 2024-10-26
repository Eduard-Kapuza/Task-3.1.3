package com.kapuza.springSecurity.controllers;

import com.kapuza.springSecurity.models.Person;
import com.kapuza.springSecurity.models.Role;
import com.kapuza.springSecurity.services.personService.PersonServiceImpl;
import com.kapuza.springSecurity.services.roleService.RoleServiceImpl;
import com.kapuza.springSecurity.util.PersonValidator;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("/registration")
public class RegistrationController {

    private final PersonValidator personValidator;
    private final PersonServiceImpl personServiceImpl;

    private final RoleServiceImpl roleServiceImpl;

    @Autowired
    public RegistrationController(PersonValidator personValidator,
                                  PersonServiceImpl personServiceImpl,
                                  RoleServiceImpl roleServiceImpl) {
        this.personValidator = personValidator;
        this.personServiceImpl = personServiceImpl;
        this.roleServiceImpl = roleServiceImpl;
    }

    /**
     * Create
     */
    @GetMapping("/")
    public String addUser(Model model) {
        List<Role> listRoles = roleServiceImpl.findAll();
        model.addAttribute("person", new Person());
        model.addAttribute("listRoles", listRoles);
        log.info("\u001B[33mНачало регистрации пользователя\u001B[0m");
        return "/registration/registration";
    }

    @PostMapping("/")
    public String saveNewUser(@ModelAttribute("person") @Valid Person person,
                              BindingResult bindingResult) {
        log.info("\u001B[33mЧто пришло с формы при регистрации: " + person + "\u001B[0m");
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            log.info("\u001B[31mОШИБКИ ПРИ РЕГИСТРАЦИИ\u001B[0m");
            return "/registration/registration";
        }
        personServiceImpl.saveOrUpdate(person);
        return "/auth/login";
    }
}
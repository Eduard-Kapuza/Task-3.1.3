package com.kapuza.springSecurity.controllers;

import com.kapuza.springSecurity.exceptionsHandling.NotFoundUserException;
import com.kapuza.springSecurity.models.User;
import com.kapuza.springSecurity.services.userService.UserServiceImpl;
import com.kapuza.springSecurity.services.registrationService.RegistrationService;
import com.kapuza.springSecurity.util.validate.PersonValidator;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/admin/users")
public class AdminController {

    private final UserServiceImpl userServiceImpl;

    private final RegistrationService registrationService;

    private final PersonValidator personValidator;

    @Autowired
    public AdminController(UserServiceImpl userServiceImpl,
                           RegistrationService registrationService,
                           PersonValidator personValidator) {
        this.userServiceImpl = userServiceImpl;
        this.registrationService = registrationService;
        this.personValidator = personValidator;
    }

    @GetMapping()
    public List<User> getAllUsers() {
        log.info("\u001B[35mПОКАЗ ВСЕХ ПОЛЬЗОВАТЕЛЕЙ через админа\u001B[0m");
        return new ArrayList<>(userServiceImpl.findAll());
    }

    @GetMapping("/{id}")
    public User findUserById(@PathVariable Long id) {
        Optional<User> user = userServiceImpl.findById(id);
        if (user.isEmpty()) {
            throw new NotFoundUserException("This user by id=" + id + " not found!");
        }
        log.info("\u001B[35mПОКАЗ ВЫБРАННОГО ПОЛЬЗОВАТЕЛЯ через админа" + user + "\u001B[0m");
        return user.get();
    }

    @GetMapping("/amountUsers")
    public Integer getAmountUsers() {
        log.info("\u001B[35mПОКАЗ кол-ва пользователей через админа\u001B[0m");
        return userServiceImpl.findAll().size();
    }

    @PostMapping()
    public User saveNewUser(@RequestBody @Valid User user,
                            BindingResult bindingResult) {
        log.info("\u001B[35mСоздание нового пользователя через админа \u001B[0m");
        personValidator.validate(user, bindingResult);
        return registrationService.register(user);
    }

    @PutMapping()
    public User upDateDataUser(@RequestBody @Valid User user,
                               BindingResult bindingResult) {
        log.info("\u001B[35mобновление данных пользователя через админа\u001B[0m");
        personValidator.validate(user, bindingResult);
        return userServiceImpl.updateDataUser(user);
    }

    @DeleteMapping("/{id}")
    public User deleteUser(@PathVariable("id") Long id) {
        log.info("\u001B[35mУдаление пользователя через админа\u001B[0m");
        return userServiceImpl.delete(id);
    }
}
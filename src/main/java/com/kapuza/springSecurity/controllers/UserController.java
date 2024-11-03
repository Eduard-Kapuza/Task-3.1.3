package com.kapuza.springSecurity.controllers;

import com.kapuza.springSecurity.models.User;
import com.kapuza.springSecurity.security.UserDetailsImpl;
import com.kapuza.springSecurity.services.userService.UserServiceImpl;
import com.kapuza.springSecurity.util.validate.PersonValidator;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserServiceImpl userServiceImpl;
    private final PersonValidator personValidator;

    @Autowired
    public UserController(UserServiceImpl userServiceImpl, PersonValidator personValidator) {
        this.userServiceImpl = userServiceImpl;
        this.personValidator = personValidator;
    }

    @GetMapping()
    public User userPage() {
        log.info("\u001B[35mПросмотр данных о себе обычным юзером\u001B[0m");
        /**Возвращаем текущего юзера в системе*/
        return UserDetailsImpl.getUser();
    }

    /**
     * Юзер сам может редактировать Имя, год рождения, пароль.; редактирование будет по его ID
     */

    @PutMapping()
    public User upDate(@RequestBody @Valid User user,
                BindingResult bindingResult) {
        log.info("\u001B[35mобновление данных пользователя самим пользователем\u001B[0m");
        personValidator.validate(user, bindingResult);
        return userServiceImpl.updateDataUser(user);
    }
}
package com.kapuza.springSecurity.util;

import com.kapuza.springSecurity.models.Person;
import com.kapuza.springSecurity.services.personService.PersonDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
@Slf4j
@Component
public class PersonValidator implements Validator {

    private final PersonDetailService personDetailService;

    @Autowired
    public PersonValidator(PersonDetailService personDetailService) {
        this.personDetailService = personDetailService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        Person person = (Person) target;

        try {
            personDetailService.loadUserByUsername(person.getUsername());
        } catch (UsernameNotFoundException e) {
            log.info("\u001B[33mВалидация пройдена для: "+person+"\u001B[0m");
            return;  // всё ок, пользователь не найден (переделать, а то проверяем на наличие ошибки, что не найден)
        }
        log.info("\u001B[33mВалидация НЕ пройдена для: "+person);
        errors.rejectValue("username", "", "This name used. Это имя занято, попробуйте другое");
    }
}
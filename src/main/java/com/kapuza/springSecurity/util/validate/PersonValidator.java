package com.kapuza.springSecurity.util.validate;

import com.kapuza.springSecurity.exceptionsHandling.UserValidationException;
import com.kapuza.springSecurity.models.User;
import com.kapuza.springSecurity.services.userService.UserDetailsServiceImpl;
import com.kapuza.springSecurity.services.userService.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
@Slf4j
@Component
public class PersonValidator implements Validator {

    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final UserServiceImpl userServiceImpl;

    @Autowired
    public PersonValidator(UserDetailsServiceImpl userDetailsServiceImpl, UserServiceImpl userServiceImpl) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.userServiceImpl = userServiceImpl;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        User user = (User) target;
        log.info("\u001B[33mID VALIDATE: "+ user.getId() +"\u001B[0m");

        try {
            UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(user.getUsername());
            if(userDetails.getUsername()!=null & !userDetails.getUsername().equals(userServiceImpl.findById(user.getId()).get().getUsername())) {
                errors.rejectValue("username", "", "This name used. Это имя занято, попробуйте другое");
                validateHelper(errors);
            }
        } catch (UsernameNotFoundException e) {
            validateHelper(errors);
            log.info("\u001B[33mВалидация пройдена для: "+ user +"\u001B[0m");
        }
    }


    private void validateHelper(Errors errors){
        if (errors.hasErrors()) {
            log.info("\u001B[31mОШИБКИ ПРИ ВАЛИДАЦИИ:   " +errors+ " \u001B[0m");
            StringBuilder mistakes=new StringBuilder();
            errors.getAllErrors().forEach(f-> mistakes.append(f.getDefaultMessage()).append("    "));
            throw new UserValidationException(mistakes.toString());
        }
    }
}
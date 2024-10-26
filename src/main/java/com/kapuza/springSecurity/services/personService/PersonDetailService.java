package com.kapuza.springSecurity.services.personService;

import com.kapuza.springSecurity.models.Person;
import com.kapuza.springSecurity.repositories.PersonRepository;
import com.kapuza.springSecurity.security.PersonDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class PersonDetailService implements UserDetailsService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonDetailService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    /**
     * проверка наличия юзера в БД во время аутентификации
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> detectedByUserName = personRepository.findByUsername(username);
        log.info("\u001B[33mПоиск юзера в БД, результат:   " + detectedByUserName.orElse(null)+"\u001B[0m");
        detectedByUserName.ifPresent(Person::toString);
        if (detectedByUserName.isEmpty()) {
            log.info("\u001B[33mЮзер  не найден в БД\u001B[0m");
            throw new UsernameNotFoundException("User not Found");
        }
        log.info("\u001B[33mЮзер...найден в БД:   " + detectedByUserName.get()+"\u001B[0m");
        return new PersonDetails(detectedByUserName.get());
    }
}
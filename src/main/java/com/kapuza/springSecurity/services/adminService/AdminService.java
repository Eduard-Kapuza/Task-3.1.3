package com.kapuza.springSecurity.services.adminService;

import com.kapuza.springSecurity.models.Person;
import com.kapuza.springSecurity.models.Role;
import com.kapuza.springSecurity.repositories.PersonRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class AdminService {

    private final PersonRepository personRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminService(PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Добавляем в БД при запуске приложения одного админа и одного юзера
     */
    @PostConstruct
    public void addAdmin() {
        Person admin = new Person("admin1", 2000);
        Person person = new Person("Eduard", 2000);
        Optional<Person> byRoleName = personRepository.findByUsername(admin.getUsername());

        if (byRoleName.isEmpty()) {
            Role roleAdmin = new Role("ROLE_ADMIN");
            Role roleUser = new Role("ROLE_USER");

            admin.addRole(List.of(roleAdmin));
            person.addRole(List.of(roleUser));

            admin.setPassword(passwordEncoder.encode("admin1"));
            person.setPassword(passwordEncoder.encode("123"));
            personRepository.save(admin);
            personRepository.save(person);
        }
    }
}
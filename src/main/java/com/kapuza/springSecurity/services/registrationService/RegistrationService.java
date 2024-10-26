package com.kapuza.springSecurity.services.registrationService;

import com.kapuza.springSecurity.models.Person;
import com.kapuza.springSecurity.models.Role;
import com.kapuza.springSecurity.repositories.PersonRepository;
import com.kapuza.springSecurity.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RegistrationService {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public RegistrationService(PersonRepository personRepository,
                               PasswordEncoder passwordEncoder,
                               RoleRepository roleRepository) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public void register(Person person) {
        /**шифруем пароль, который ввёл в форме юзер*/
        String encodePassword = passwordEncoder.encode(person.getPassword());
        /**устанавливаем шифрованный пароль юзеру*/
        person.setPassword(encodePassword);
        /**Всем новым пользователям устанавливаем какую-то дефолтную роль*/
        Role role = new Role("ROLE_ADMIN");

        Optional<Role> byRoleName = roleRepository.findByRoleName(role.getRoleName());
        byRoleName.ifPresent(value -> person.addRole(byRoleName.get()));
        if (byRoleName.isEmpty()) {
            person.addRole(role);
        }
        personRepository.save(person);
    }
}
package com.kapuza.springSecurity.services.personService;

import com.kapuza.springSecurity.models.Person;
import com.kapuza.springSecurity.models.Role;
import com.kapuza.springSecurity.repositories.PersonRepository;
import com.kapuza.springSecurity.services.roleService.RoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final RoleServiceImpl roleServiceImpl;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository,
                             RoleServiceImpl roleServiceImpl,
                             PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.roleServiceImpl = roleServiceImpl;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Перед сохранением юзера смотрим в БД, если нашли роль
     * и она совпадает с ролью из view то берём её и назначаем
     * текущему юзеру
     */
    public void save(Person person, String[] listRoles) {

        List<Role> roleList = new ArrayList<>();

        for (String roleName : listRoles) {
            Optional<Role> byRoleName = roleServiceImpl.findByRoleName(roleName);
            byRoleName.ifPresent(r -> roleList.add(byRoleName.get()));
            if (byRoleName.isEmpty()) {
                roleList.add(new Role(roleName));
            }
        }

        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.addRole(roleList);
        personRepository.save(person);
    }

    @Override
    public void saveOrUpdate(Person person) {
        if(person.getRoles().size()==0){
            person.setRoles(List.of(roleServiceImpl.findByRoleName("ROLE_USER").get()));
        }
        if (person.getPassword().startsWith("$")) {
            personRepository.save(person);
            return;
        }
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        personRepository.save(person);
    }

    @Override
    public List<Person> findAll() {
        return personRepository.findAll();
    }

    @Override
    public Person findById(Integer id) {
        return personRepository.findById(id).get();
    }

    @Override
    public void delete(Person person) {
        personRepository.delete(person);
    }
}
package com.kapuza.springSecurity.services.personService;


import com.kapuza.springSecurity.models.Person;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PersonService {
    public void save(Person person, String[] listRoles);

    public void saveOrUpdate(Person person);

    List<Person> findAll();

    Person findById(Integer id);

    void delete(Person person);
}
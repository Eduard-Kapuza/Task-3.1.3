package com.kapuza.springSecurity.services.userService;


import com.kapuza.springSecurity.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    public User save(User user);

    List<User> findAll();

    Optional<User> findById(Long id);

    User delete(Long id);

    public User updateDataUser(User user);
    public Optional<User> findByUserName(User user);

}
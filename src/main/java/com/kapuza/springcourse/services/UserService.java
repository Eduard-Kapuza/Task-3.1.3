package com.kapuza.springcourse.services;

import com.kapuza.springcourse.model.User;

import java.util.List;

public interface UserService {
    public void saveUser(User user);

    public int getAmountUsers();

    public List<User> getAllUsers();

    public User getUserById(int id);

    public void updateUser(User user);
    public void deleteUser(User user);
}

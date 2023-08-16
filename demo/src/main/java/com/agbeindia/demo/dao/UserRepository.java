package com.agbeindia.demo.dao;

import com.agbeindia.demo.entities.User;
import com.agbeindia.demo.entities.UserResponse;
import com.agbeindia.demo.utility.UserAlreadyExistsException;

import java.util.List;

public interface UserRepository {
    UserResponse save(User newUser);

    List<User> getUsers();
}

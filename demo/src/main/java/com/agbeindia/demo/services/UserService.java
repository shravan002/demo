package com.agbeindia.demo.services;

import com.agbeindia.demo.entities.User;
import com.agbeindia.demo.entities.UserResponse;
import com.agbeindia.demo.utility.UserAlreadyExistsException;

public interface UserService {
    UserResponse createUser(User user) throws UserAlreadyExistsException;
}

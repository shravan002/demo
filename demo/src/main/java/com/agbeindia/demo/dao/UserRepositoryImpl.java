package com.agbeindia.demo.dao;

import com.agbeindia.demo.entities.User;
import com.agbeindia.demo.entities.UserResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserRepositoryImpl implements UserRepository {

    List<User> userList;

    public UserRepositoryImpl() {
        userList = new ArrayList<>();
    }

    public UserResponse save(User newUser) {

        UserResponse userResponse = new UserResponse();
        userResponse.setId(newUser.getId());
        userResponse.setFirstName(newUser.getFirstName());
        userResponse.setLastName(newUser.getLastName());
        userResponse.setUserName(newUser.getUserName());

        userList.add(newUser);
        return userResponse;
    }

    public List<User> getUsers() {
        return userList;
    }
}

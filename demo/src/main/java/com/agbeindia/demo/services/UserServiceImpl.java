package com.agbeindia.demo.services;

import com.agbeindia.demo.dao.UserRepository;
import com.agbeindia.demo.entities.User;
import com.agbeindia.demo.entities.UserResponse;
import com.agbeindia.demo.utility.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserResponse createUser(User newUser) throws UserAlreadyExistsException {

        List<User> userList = userRepository.getUsers();
        for (User user : userList) {
            if (user.getUserName().equals(newUser.getUserName()))
                throw new UserAlreadyExistsException("A user with the given username already exists");
        }
        System.out.println(newUser.toString());
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        newUser.setHashedPassword(bCryptPasswordEncoder.encode(newUser.getPlainTextPassword()));
        System.out.println(newUser);
        return userRepository.save(newUser);
    }
}

package com.agbeindia.demo;

import com.agbeindia.demo.controllers.UserController;
import com.agbeindia.demo.entities.User;
import com.agbeindia.demo.entities.UserResponse;
import com.agbeindia.demo.services.UserService;
import com.agbeindia.demo.utility.UserAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser_Success() throws UserAlreadyExistsException {
        User user = new User();

        when(userService.createUser(user)).thenReturn(new UserResponse());

        ResponseEntity<Object> responseEntity = userController.createUser(user);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testCreateUser_UserAlreadyExists() throws UserAlreadyExistsException {
        User user = new User();

        when(userService.createUser(user)).thenThrow(new UserAlreadyExistsException("USER_ALREADY_EXISTS",
                new Throwable("A user with the given username already exists")));

        ResponseEntity<Object> responseEntity = userController.createUser(user);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());

        Map<String, String> responseBody = (Map<String, String>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("USER_ALREADY_EXISTS", responseBody.get("code"));
        assertEquals("A user with the given username already exists", responseBody.get("description"));
    }
}


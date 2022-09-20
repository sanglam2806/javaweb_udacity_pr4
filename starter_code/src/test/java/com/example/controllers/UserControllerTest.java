package com.example.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.demo.SareetaApplication;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

@SpringBootTest(classes = SareetaApplication.class)
public class UserControllerTest {
    private UserController userController;
    private UserRepository userRepository;
    private CartRepository cartRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    public void initEachTest() {
        userRepository = mock(UserRepository.class);
        cartRepository = mock(CartRepository.class);
        bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);
        userController = new UserController(userRepository, cartRepository, bCryptPasswordEncoder);
    }

    @Test
    public void findByIdTest() {
        User user = createUser();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.findById(user.getId());
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        compareUser(user, response.getBody());
    }

    @Test
    public void findByIdUsername() {
        User user = createUser();

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        ResponseEntity<User> response = userController.findByUserName(user.getUsername());
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        compareUser(user, response.getBody());
    }

    @Test
    public void createUserTest() {
        CreateUserRequest createUserRequest = createUserRequest();

        when(bCryptPasswordEncoder.encode(createUserRequest.getPassword())).thenReturn(createUserRequest.getPassword());
        ResponseEntity<User> response = userController.createUser(createUserRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(createUserRequest.getUsername(), response.getBody().getUsername());
        assertEquals(createUserRequest.getPassword(), response.getBody().getPassword());

        createUserRequest.setPassword("123");
        when(bCryptPasswordEncoder.encode(createUserRequest.getPassword())).thenReturn(createUserRequest.getPassword());
        response = userController.createUser(createUserRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    private void compareUser(User user1, User user2) {
        assertEquals(user1.getId(), user2.getId());
        assertEquals(user1.getUsername(), user2.getUsername());
        assertEquals(user1.getPassword(), user2.getPassword());
    }

    public CreateUserRequest createUserRequest() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("Mitsukeru");
        createUserRequest.setPassword("12345678");
        createUserRequest.setConfirmPassword("12345678");

        return createUserRequest;
    }

    public User createUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("Mitsu");
        user.setPassword("123");

        return user;
    }
}

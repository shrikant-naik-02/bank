package com.excelfore.test.BankTransaction.controller;

import com.excelfore.test.BankTransaction.exception.UserAlreadyHasAccountException;
import com.excelfore.test.BankTransaction.model.User;
import com.excelfore.test.BankTransaction.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//Purpose: Test your /register endpoint.
@AutoConfigureMockMvc
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    void registerUser_ShouldReturn201_WhenSuccessful() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setName("Test");
        user.setUsername("testuser");
        user.setPassword("password");
        user.setRole("USER");

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userService.createUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "name": "Test",
                                "username": "testuser",
                                "password": "password"
                            }
                        """))
                .andExpect(status().isCreated())
                .andExpect(content().string("User registered successfully"));
    }

    @Test
    void registerUser_ShouldReturn400_WhenMissingFields() throws Exception {
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "username": "incompleteUser"
                            }
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerUser_ShouldReturn409_WhenUsernameExists() throws Exception {
        doThrow(new UserAlreadyHasAccountException("Username already taken."))
                .when(userService).createUser(any(User.class));

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "name": "Test",
                                "username": "duplicateUser",
                                "password": "password"
                            }
                        """))
                .andExpect(status().isConflict());
    }
}

package com.excelfore.test.BankTransaction.service;

import com.excelfore.test.BankTransaction.exception.UserAlreadyHasAccountException;
import com.excelfore.test.BankTransaction.model.User;
import com.excelfore.test.BankTransaction.repository.UserRepository;
import com.excelfore.test.BankTransaction.serviceImpl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

//Purpose: Unit test your createUser logic.
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_ShouldSave_WhenNewUser() {
        User user = new User();
        user.setUsername("newuser");

        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);

        User savedUser = userService.createUser(user);

        assertEquals("newuser", savedUser.getUsername());
    }

    @Test
    void createUser_ShouldThrow_WhenUsernameExists() {
        User user = new User();
        user.setUsername("existinguser");

        when(userRepository.findByUsername("existinguser"))
                .thenReturn(Optional.of(user));

        assertThrows(UserAlreadyHasAccountException.class, () -> userService.createUser(user));
    }
}

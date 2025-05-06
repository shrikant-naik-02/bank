package com.excelfore.test.BankTransaction.serviceImpl;

import com.excelfore.test.BankTransaction.model.Account;
import com.excelfore.test.BankTransaction.model.User;
import com.excelfore.test.BankTransaction.repository.AccountRepository;
import com.excelfore.test.BankTransaction.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//Purpose: Unit test deposit, withdraw, and auth logic.
@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("testuser", null)
        );
    }

    @Test
    void deposit_ShouldWork_WhenAuthorized() {
        User user = new User();
        user.setUsername("testuser");

        Account account = new Account();
        account.setId(1L);
        account.setUser(user);
        account.setBalance(1000);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account result = accountService.deposit(1L, 500);

        assertEquals(1500, result.getBalance());
    }

    @Test
    void deposit_ShouldFail_WhenUnauthorized() {
        User user = new User();
        user.setUsername("anotheruser");

        Account account = new Account();
        account.setId(1L);
        account.setUser(user);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        assertThrows(AccessDeniedException.class, () -> accountService.deposit(1L, 500));
    }

    @Test
    void withdraw_ShouldFail_WhenInsufficientFunds() {
        User user = new User();
        user.setUsername("testuser");

        Account account = new Account();
        account.setId(1L);
        account.setUser(user);
        account.setBalance(300);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        assertThrows(RuntimeException.class, () -> accountService.withdraw(1L, 500));
    }
}

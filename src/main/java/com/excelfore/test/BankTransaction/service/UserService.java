package com.excelfore.test.BankTransaction.service;

import com.excelfore.test.BankTransaction.model.User;

import java.util.List;

public interface UserService {
    User createUser(User user);

    List<User> getAllUsers();
}

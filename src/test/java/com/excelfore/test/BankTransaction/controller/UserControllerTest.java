package com.excelfore.test.BankTransaction.controller;

import com.excelfore.test.BankTransaction.controller.UserController;
import com.excelfore.test.BankTransaction.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Mock all dependencies (if any, like services or filters)
    @MockBean
    private UserService userService; // Replace with actual dependency if required

    @Test
    @WithMockUser // This mocks an authenticated user so Spring Security doesn't block the request
    void pingEndpoint_shouldReturn200() throws Exception {
        mockMvc.perform(get("/api/v1/test/ping")) // use correct URL
                .andExpect(status().isOk())
                .andExpect(content().string("pong"));
    }
}

package org.wsd.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.wsd.app.security.auth.resquest.SignInRequest;
import org.wsd.app.security.auth.resquest.SignUpRequest;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    void setup() throws Exception {
        final SignUpRequest signUpRequest = SignUpRequest.builder()
                .username("username@wsd.com")
                .password("Test1234!!@@")
                .build();

        final String payload = objectMapper.writeValueAsString(signUpRequest);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signUp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload)
        ).andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @Order(1)
    void signUp() throws Exception {
        final SignUpRequest signUpRequest = SignUpRequest.builder()
                .username("newuser@wsd.com")
                .password("Test1234!!@@")
                .build();

        final String payload = objectMapper.writeValueAsString(signUpRequest);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signUp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload)
        ).andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @Order(2)
    void signIn() throws Exception {
        final SignInRequest signInRequest = SignInRequest.builder()
                .username("username@wsd.com")
                .password("Test1234!!@@")
                .build();

        final String payload = objectMapper.writeValueAsString(signInRequest);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signIn")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    @Order(2)
    void userNotFound() throws Exception {
        final SignInRequest signInRequest = SignInRequest.builder()
                .username("partharaj.dev@gmail.com")
                .password("Test1234!!@@")
                .build();

        final String payload = objectMapper.writeValueAsString(signInRequest);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signIn")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }


    @Test
    @Order(3)
    void signUpDuplicate() throws Exception {
        final SignUpRequest signUpRequest = SignUpRequest.builder()
                .username("newuser@wsd.com")
                .password("Test1234!!@@")
                .build();

        final String payload = objectMapper.writeValueAsString(signUpRequest);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signUp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload)
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

}
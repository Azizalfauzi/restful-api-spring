package zuhaproject.restful.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import zuhaproject.restful.entity.User;
import zuhaproject.restful.model.LoginUserRequest;
import zuhaproject.restful.model.TokenResponse;
import zuhaproject.restful.model.WebResponse;
import zuhaproject.restful.repository.UserRepository;
import zuhaproject.restful.security.BCrypt;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void loginFailedUserNotFound() throws Exception {
        LoginUserRequest request = new LoginUserRequest();
        request.setUsername("test");
        request.setPassword("test");
        mockMvc.perform(post("/api/auth/login").
                        accept(MediaType.APPLICATION_JSON).
                        contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(request)))
                .andExpectAll(status().isUnauthorized()).andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    Assertions.assertNotNull(response.getErrors());
                });
    }

    @Test
    void loginFailedUserWrongPassword() throws Exception {
        User user = new User();
        user.setName("test");
        user.setUsername("test");
        user.setPassword(BCrypt.hashpw("test", BCrypt.gensalt()));
        userRepository.save(user);

        LoginUserRequest request = new LoginUserRequest();
        request.setUsername("test");
        request.setPassword("hello");
        mockMvc.perform(post("/api/auth/login").
                        accept(MediaType.APPLICATION_JSON).
                        contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(request)))
                .andExpectAll(status().isUnauthorized()).andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    Assertions.assertNotNull(response.getErrors());
                });
    }

    @Test
    void loginSuccess() throws Exception {
        User user = new User();
        user.setName("test");
        user.setUsername("test");
        user.setPassword(BCrypt.hashpw("test", BCrypt.gensalt()));
        userRepository.save(user);

        LoginUserRequest request = new LoginUserRequest();
        request.setUsername("test");
        request.setPassword("test");
        mockMvc.perform(post("/api/auth/login").
                        accept(MediaType.APPLICATION_JSON).
                        contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(request)))
                .andExpectAll(status().isOk()).andDo(result -> {
                    WebResponse<TokenResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    Assertions.assertNull(response.getErrors());
                    Assertions.assertNotNull(response.getData().getToken());
                    Assertions.assertNotNull(response.getData().getExpiredAt());

                    User user1 = userRepository.findById("test").orElse(null);
                    Assertions.assertNotNull(user1);
                    Assertions.assertEquals(user1.getToken(), response.getData().getToken());
                    Assertions.assertEquals(user1.getTokenExpiredAt(), response.getData().getExpiredAt());
                });
    }

    @Test
    void logoutFailed() throws Exception {
        mockMvc.perform(delete("/api/auth/logout").
                accept(MediaType.APPLICATION_JSON)).andExpectAll(status().isUnauthorized()).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            Assertions.assertNotNull(response.getErrors());
        });
    }

    @Test
    void logoutSuccess() throws Exception {
        User user = new User();
        user.setName("test");
        user.setUsername("test");
        user.setPassword(BCrypt.hashpw("test", BCrypt.gensalt()));
        user.setToken("test");
        user.setTokenExpiredAt(System.currentTimeMillis() + 10000000L);
        userRepository.save(user);

        mockMvc.perform(delete("/api/auth/logout").
                accept(MediaType.APPLICATION_JSON).
                header("X-API-TOKEN", "test")).andExpectAll(status().isOk()).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals("OK", response.getData());

            User userDb = userRepository.findById("test").orElse(null);
            assertNotNull(userDb);
            assertNull(userDb.getTokenExpiredAt());
            assertNull(userDb.getToken());
        });
    }
}
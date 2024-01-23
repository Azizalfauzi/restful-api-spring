package zuhaproject.restful.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import zuhaproject.restful.entity.User;
import zuhaproject.restful.model.RegisterUserRequest;
import zuhaproject.restful.model.UpdateUserRequest;
import zuhaproject.restful.model.UserReponse;
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
@Slf4j
class UserControllerTest {
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
    void testRegisterSuccess() throws Exception {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername("aziz123");
        request.setPassword("rahasia");
        request.setName("Aziz");

        mockMvc.perform(post("/api/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(status().isOk()).andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    Assertions.assertEquals("OK", response.getData());
                });
    }

    @Test
    void testRegisterBadRequest() throws Exception {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername("");
        request.setPassword("");
        request.setName("");

        mockMvc.perform(post("/api/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(status().isBadRequest()).andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    Assertions.assertNotNull(response.getErrors());
                });
    }

    @Test
    void testRegisterDuplicate() throws Exception {
        User user = new User();
        user.setName("Beta");
        user.setUsername("beta123");
        user.setPassword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));
        userRepository.save(user);


        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername("beta123");
        request.setPassword("rahasia");
        request.setName("Beta");

        mockMvc.perform(post("/api/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(status().isBadRequest()).andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    Assertions.assertNotNull(response.getErrors());
                });
    }

    @Test
    void getUserUnauthorized() throws Exception {
        mockMvc.perform(get("/api/users/current").
                        accept(MediaType.APPLICATION_JSON).
                        header("X-API-TOKEN", "notfound")).
                andExpectAll(status().isUnauthorized()).andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    Assertions.assertNotNull(response.getErrors());
                });
        ;
    }

    @Test
    void getUserUnauthorizedTokenNotSend() throws Exception {
        mockMvc.perform(get("/api/users/current").
                        accept(MediaType.APPLICATION_JSON)).
                andExpectAll(status().isUnauthorized()).andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    Assertions.assertNotNull(response.getErrors());
                });
        ;
    }

    @Test
    void getUserSuccess() throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setName("test");
        user.setToken("test");
        user.setPassword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));
        user.setTokenExpiredAt(System.currentTimeMillis() + 1000000L);
        userRepository.save(user);

        mockMvc.perform(get("/api/users/current").
                        accept(MediaType.APPLICATION_JSON).
                        header("X-API-TOKEN", "test")).
                andExpectAll(status().isOk()).andDo(result -> {
                    WebResponse<UserReponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    Assertions.assertEquals("test", response.getData().getUsername());
                    Assertions.assertEquals("test", response.getData().getName());
                });
        ;
    }

    @Test
    void getUserTokenExpired() throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setName("test");
        user.setToken("test");
        user.setPassword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));
        user.setTokenExpiredAt(System.currentTimeMillis() - 1000000L);
        userRepository.save(user);

        mockMvc.perform(get("/api/users/current").
                        accept(MediaType.APPLICATION_JSON).
                        header("X-API-TOKEN", "test")).
                andExpectAll(status().isUnauthorized()).andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    Assertions.assertNotNull(response.getErrors());
                });
        ;
    }

    @Test
    void updateUserUnauthorized() throws Exception {
        UpdateUserRequest request = new UpdateUserRequest();
        mockMvc.perform(patch("/api/users/current").
                        accept(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(request)).
                        contentType(MediaType.APPLICATION_JSON)).
                andExpectAll(status().isUnauthorized()).andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    Assertions.assertNotNull(response.getErrors());
                });
        ;
    }

    @Test
    void updateUserSuccess() throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setName("test");
        user.setToken("test");
        user.setPassword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));
        user.setTokenExpiredAt(System.currentTimeMillis() + 100000000000L);
        userRepository.save(user);

        UpdateUserRequest request = new UpdateUserRequest();
        request.setName("Aziz");
        request.setPassword("Aziz1234");
        mockMvc.perform(patch("/api/users/current").
                        accept(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(request)).
                        contentType(MediaType.APPLICATION_JSON).header("X-API-TOKEN", "test")).
                andExpectAll(status().isOk()).andDo(result -> {
                    WebResponse<UserReponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    Assertions.assertNull(response.getErrors());
                    Assertions.assertEquals("Aziz", response.getData().getName());
                    Assertions.assertEquals("test", response.getData().getUsername());

                    User userDb = userRepository.findById("test").orElse(null);
                    assertNotNull(userDb);
                    assertTrue(BCrypt.checkpw("Aziz1234", userDb.getPassword()));
                });
        ;
    }
}
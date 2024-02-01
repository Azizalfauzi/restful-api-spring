package zuhaproject.restful.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import zuhaproject.restful.entity.Contact;
import zuhaproject.restful.entity.User;
import zuhaproject.restful.model.ContactResponse;
import zuhaproject.restful.model.CreateContactRequest;
import zuhaproject.restful.model.UpdateContactRequest;
import zuhaproject.restful.model.WebResponse;
import zuhaproject.restful.repository.ContactRepository;
import zuhaproject.restful.repository.UserRepository;
import zuhaproject.restful.security.BCrypt;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class ContactControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        contactRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setName("test");
        user.setUsername("test");
        user.setPassword(BCrypt.hashpw("test", BCrypt.gensalt()));
        user.setToken("test");
        user.setTokenExpiredAt(System.currentTimeMillis() + 1000000L);
        userRepository.save(user);
    }

    @Test
    void createContactBadRequest() throws Exception {
        CreateContactRequest request = new CreateContactRequest();
        request.setFirstName("");
        request.setEmail("Salah");
        mockMvc.perform(post("/api/contacts").
                        accept(MediaType.APPLICATION_JSON).
                        contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(request)).
                        header("X-API-TOKEN", "test")).
                andExpectAll(status().isBadRequest()).
                andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                    });
                    assertNotNull(response.getErrors());
                });
    }

    @Test
    void createContactSuccess() throws Exception {
        CreateContactRequest request = new CreateContactRequest();
        request.setFirstName("aziz");
        request.setLastName("alfa");
        request.setEmail("aziz@gmail.com");
        request.setPhone("123456");
        mockMvc.perform(post("/api/contacts").
                        accept(MediaType.APPLICATION_JSON).
                        contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(request)).
                        header("X-API-TOKEN", "test")).
                andExpectAll(status().isOk()).
                andDo(result -> {
                    WebResponse<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertNull(response.getErrors());
                    assertEquals("aziz", response.getData().getFirstName());
                    assertEquals("alfa", response.getData().getLastName());
                    assertEquals("aziz@gmail.com", response.getData().getEmail());
                    assertEquals("123456", response.getData().getPhone());

                    assertTrue(contactRepository.existsById(response.getData().getId()));
                });
    }

    @Test
    void getContactNotFound() throws Exception {

        mockMvc.perform(get("/api/contacts/123123").
                        accept(MediaType.APPLICATION_JSON).
                        contentType(MediaType.APPLICATION_JSON).
                        header("X-API-TOKEN", "test")).
                andExpectAll(status().isNotFound()).
                andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                    });
                    assertNotNull(response.getErrors());
                });
    }

    @Test
    void getContactSuccess() throws Exception {
        User user = userRepository.findById("test").orElse(null);
        Contact contact = new Contact();
        contact.setId(UUID.randomUUID().toString());
        contact.setUser(user);
        contact.setFirstName("aziz");
        contact.setLastName("alfa");
        contact.setEmail("aziz@gmail.com");
        contact.setPhone("123123");
        contactRepository.save(contact);

        mockMvc.perform(get("/api/contacts/" + contact.getId()).
                        accept(MediaType.APPLICATION_JSON).
                        contentType(MediaType.APPLICATION_JSON).
                        header("X-API-TOKEN", "test")).
                andExpectAll(status().isOk()).
                andDo(result -> {
                    WebResponse<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertNull(response.getErrors());
                    assertEquals(contact.getId(), response.getData().getId());
                    assertEquals(contact.getFirstName(), response.getData().getFirstName());
                    assertEquals(contact.getLastName(), response.getData().getLastName());
                    assertEquals(contact.getEmail(), response.getData().getEmail());
                    assertEquals(contact.getPhone(), response.getData().getPhone());
                });
    }

    @Test
    void updateContactBadRequest() throws Exception {
        UpdateContactRequest request = new UpdateContactRequest();
        request.setFirstName("");
        request.setEmail("Salah");
        mockMvc.perform(put("/api/contacts/123").
                        accept(MediaType.APPLICATION_JSON).
                        contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(request)).
                        header("X-API-TOKEN", "test")).
                andExpectAll(status().isBadRequest()).
                andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                    });
                    assertNotNull(response.getErrors());
                });
    }

    @Test
    void updateContactSuccess() throws Exception {
        User user = userRepository.findById("test").orElse(null);
        Contact contact = new Contact();
        contact.setId(UUID.randomUUID().toString());
        contact.setUser(user);
        contact.setFirstName("aziz");
        contact.setLastName("alfa");
        contact.setEmail("aziz@gmail.com");
        contact.setPhone("123123");
        contactRepository.save(contact);


        UpdateContactRequest request = new UpdateContactRequest();
        request.setFirstName("alfauzi");
        request.setLastName("aziz");
        request.setEmail("alfa@gmail.com");
        request.setPhone("123456");
        mockMvc.perform(put("/api/contacts/" + contact.getId()).
                        accept(MediaType.APPLICATION_JSON).
                        contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(request)).
                        header("X-API-TOKEN", "test")).
                andExpectAll(status().isOk()).
                andDo(result -> {
                    WebResponse<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertNull(response.getErrors());
                    assertEquals(request.getFirstName(), response.getData().getFirstName());
                    assertEquals(request.getLastName(), response.getData().getLastName());
                    assertEquals(request.getEmail(), response.getData().getEmail());
                    assertEquals(request.getPhone(), response.getData().getPhone());

                    assertTrue(contactRepository.existsById(response.getData().getId()));
                });
    }

    @Test
    void deleteContactNotFound() throws Exception {
        mockMvc.perform(delete("/api/contacts/123123").
                        accept(MediaType.APPLICATION_JSON).
                        contentType(MediaType.APPLICATION_JSON).
                        header("X-API-TOKEN", "test")).
                andExpectAll(status().isNotFound()).
                andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                    });
                    assertNotNull(response.getErrors());
                });
    }

    @Test
    void deleteContactSuccess() throws Exception {
        User user = userRepository.findById("test").orElse(null);
        Contact contact = new Contact();
        contact.setId(UUID.randomUUID().toString());
        contact.setUser(user);
        contact.setFirstName("aziz");
        contact.setLastName("alfa");
        contact.setEmail("aziz@gmail.com");
        contact.setPhone("123123");
        contactRepository.save(contact);

        mockMvc.perform(delete("/api/contacts/" + contact.getId()).
                        accept(MediaType.APPLICATION_JSON).
                        contentType(MediaType.APPLICATION_JSON).
                        header("X-API-TOKEN", "test")).
                andExpectAll(status().isOk()).
                andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertNull(response.getErrors());
                    assertEquals("OK", response.getData());
                });
    }

    @Test
    void searchContactNotFound() throws Exception {
        mockMvc.perform(get("/api/contacts").
                        accept(MediaType.APPLICATION_JSON).
                        contentType(MediaType.APPLICATION_JSON).
                        header("X-API-TOKEN", "test")).
                andExpectAll(status().isOk()).
                andDo(result -> {
                    WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertNull(response.getErrors());
                    assertEquals(0, response.getData().size());
                    assertEquals(0, response.getPaging().getTotalPage());
                    assertEquals(0, response.getPaging().getCurrentPage());
                    assertEquals(10, response.getPaging().getSize());
                });
    }

    @Test
    void searchSuccess() throws Exception {
        User user = userRepository.findById("test").orElse(null);
        for (int i = 0; i < 100; i++) {
            Contact contact = new Contact();
            contact.setId(UUID.randomUUID().toString());
            contact.setUser(user);
            contact.setFirstName("aziz" + i);
            contact.setLastName("alfa");
            contact.setEmail("aziz@gmail.com");
            contact.setPhone("123123");
            contactRepository.save(contact);
        }

        mockMvc.perform(get("/api/contacts").
                        queryParam("name", "aziz").
                        accept(MediaType.APPLICATION_JSON).
                        contentType(MediaType.APPLICATION_JSON).
                        header("X-API-TOKEN", "test")).
                andExpectAll(status().isOk()).
                andDo(result -> {
                    WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertNull(response.getErrors());
                    assertEquals(10, response.getData().size());
                    assertEquals(10, response.getPaging().getTotalPage());
                    assertEquals(0, response.getPaging().getCurrentPage());
                    assertEquals(10, response.getPaging().getSize());
                });

        mockMvc.perform(get("/api/contacts").
                        queryParam("name", "alfa").
                        accept(MediaType.APPLICATION_JSON).
                        contentType(MediaType.APPLICATION_JSON).
                        header("X-API-TOKEN", "test")).
                andExpectAll(status().isOk()).
                andDo(result -> {
                    WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertNull(response.getErrors());
                    assertEquals(10, response.getData().size());
                    assertEquals(10, response.getPaging().getTotalPage());
                    assertEquals(0, response.getPaging().getCurrentPage());
                    assertEquals(10, response.getPaging().getSize());
                });

        mockMvc.perform(get("/api/contacts").
                        queryParam("email", "aziz@gmail.com").
                        accept(MediaType.APPLICATION_JSON).
                        contentType(MediaType.APPLICATION_JSON).
                        header("X-API-TOKEN", "test")).
                andExpectAll(status().isOk()).
                andDo(result -> {
                    WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertNull(response.getErrors());
                    assertEquals(10, response.getData().size());
                    assertEquals(10, response.getPaging().getTotalPage());
                    assertEquals(0, response.getPaging().getCurrentPage());
                    assertEquals(10, response.getPaging().getSize());
                });

        mockMvc.perform(get("/api/contacts").
                        queryParam("phone", "123").
                        accept(MediaType.APPLICATION_JSON).
                        contentType(MediaType.APPLICATION_JSON).
                        header("X-API-TOKEN", "test")).
                andExpectAll(status().isOk()).
                andDo(result -> {
                    WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertNull(response.getErrors());
                    assertEquals(10, response.getData().size());
                    assertEquals(10, response.getPaging().getTotalPage());
                    assertEquals(0, response.getPaging().getCurrentPage());
                    assertEquals(10, response.getPaging().getSize());
                });
    }
}
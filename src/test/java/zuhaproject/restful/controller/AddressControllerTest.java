package zuhaproject.restful.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import zuhaproject.restful.entity.Contact;
import zuhaproject.restful.entity.User;
import zuhaproject.restful.model.AddressResponse;
import zuhaproject.restful.model.CreateAddressRequest;
import zuhaproject.restful.model.WebResponse;
import zuhaproject.restful.repository.AddressRepository;
import zuhaproject.restful.repository.ContactRepository;
import zuhaproject.restful.repository.UserRepository;
import zuhaproject.restful.security.BCrypt;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AddressControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        addressRepository.deleteAll();
        contactRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setName("test");
        user.setUsername("test");
        user.setPassword(BCrypt.hashpw("test", BCrypt.gensalt()));
        user.setToken("test");
        user.setTokenExpiredAt(System.currentTimeMillis() + 1000000L);
        userRepository.save(user);

        Contact contact = new Contact();
        contact.setId("test");
        contact.setUser(user);
        contact.setFirstName("aziz");
        contact.setLastName("alfa");
        contact.setEmail("aziz@gmail.com");
        contact.setPhone("123123");
        contactRepository.save(contact);
    }

    @Test
    void createAddressBadRequest() throws Exception {
        CreateAddressRequest request = new CreateAddressRequest();
        request.setCountry("");
        mockMvc.perform(post("/api/contacts/test/addresses").
                        accept(MediaType.APPLICATION_JSON).
                        contentType(MediaType.APPLICATION_JSON).
                        header("X-API-TOKEN", "test").
                        content(objectMapper.writeValueAsString(request))).
                andExpectAll(status().isBadRequest()).andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertNotNull(response.getErrors());
                });
    }

    @Test
    void createAddressSuccess() throws Exception {
        CreateAddressRequest request = new CreateAddressRequest();
        request.setStreet("Jl.papandayan");
        request.setCity("Tulungagung");
        request.setProvince("Jawa Timur");
        request.setCountry("Indonesia");
        request.setPostalCode("123321");

        mockMvc.perform(post("/api/contacts/test/addresses").
                        accept(MediaType.APPLICATION_JSON).
                        contentType(MediaType.APPLICATION_JSON).
                        header("X-API-TOKEN", "test").
                        content(objectMapper.writeValueAsString(request))).
                andExpectAll(status().isOk()).andDo(result -> {
                    WebResponse<AddressResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertNull(response.getErrors());

                    assertEquals(request.getStreet(), response.getData().getStreet());

                    assertEquals(request.getCity(), response.getData().getCity());

                    assertEquals(request.getProvince(), response.getData().getProvince());

                    assertEquals(request.getCountry(), response.getData().getCountry());

                    assertEquals(request.getPostalCode(), response.getData().getPostalCode());

                    assertTrue(addressRepository.existsById(response.getData().getId()));

                });
    }

    @Test
    void getAddressBadRequest() throws Exception {
        mockMvc.perform(get("/api/contacts/test/addresses/test").
                        accept(MediaType.APPLICATION_JSON).
                        contentType(MediaType.APPLICATION_JSON).
                        header("X-API-TOKEN", "test")).
                andExpectAll(status().isNotFound()).andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertNotNull(response.getErrors());
                });
    }

}
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
import zuhaproject.restful.entity.Address;
import zuhaproject.restful.entity.Contact;
import zuhaproject.restful.entity.User;
import zuhaproject.restful.model.AddressResponse;
import zuhaproject.restful.model.CreateAddressRequest;
import zuhaproject.restful.model.UpdateAddressRequest;
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

    @Test
    void getAddressSuccess() throws Exception {
        Contact contact = contactRepository.findById("test").orElseThrow();
        Address address = new Address();

        address.setId("test");
        address.setContact(contact);
        address.setStreet("Jl.Papandayan");
        address.setCity("Jakarta");
        address.setProvince("DKI");
        address.setCountry("Indonesia");
        address.setPostalCode("123321");
        addressRepository.save(address);

        mockMvc.perform(get("/api/contacts/test/addresses/test").
                        accept(MediaType.APPLICATION_JSON).
                        contentType(MediaType.APPLICATION_JSON).
                        header("X-API-TOKEN", "test")).
                andExpectAll(status().isOk()).andDo(result -> {
                    WebResponse<AddressResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertNull(response.getErrors());

                    assertEquals(address.getId(), response.getData().getId());

                    assertEquals(address.getStreet(), response.getData().getStreet());

                    assertEquals(address.getCity(), response.getData().getCity());

                    assertEquals(address.getProvince(), response.getData().getProvince());

                    assertEquals(address.getPostalCode(), response.getData().getPostalCode());
                });
    }

    @Test
    void updateAddressBadRequest() throws Exception {
        UpdateAddressRequest request = new UpdateAddressRequest();
        request.setCountry("");
        mockMvc.perform(put("/api/contacts/test/addresses/test").
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
    void updateAddressSuccess() throws Exception {
        Contact contact = contactRepository.findById("test").orElseThrow();
        Address address = new Address();

        address.setId("test");
        address.setContact(contact);
        address.setStreet("Jl.last");
        address.setCity("Last");
        address.setProvince("last");
        address.setCountry("last");
        address.setPostalCode("123321");
        addressRepository.save(address);


        UpdateAddressRequest request = new UpdateAddressRequest();
        request.setStreet("Jl.papandayan");
        request.setCity("Tulungagung");
        request.setProvince("Jawa Timur");
        request.setCountry("Indonesia");
        request.setPostalCode("123321");

        mockMvc.perform(put("/api/contacts/test/addresses/test").
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
    void deleteAddressBadRequest() throws Exception {
        mockMvc.perform(delete("/api/contacts/test/addresses/test").
                        accept(MediaType.APPLICATION_JSON).
                        contentType(MediaType.APPLICATION_JSON).
                        header("X-API-TOKEN", "test")).
                andExpectAll(status().isNotFound()).andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertNotNull(response.getErrors());
                });
    }

    @Test
    void deleteAddressSuccess() throws Exception {
        Contact contact = contactRepository.findById("test").orElseThrow();
        Address address = new Address();

        address.setId("test");
        address.setContact(contact);
        address.setStreet("Jl.Papandayan");
        address.setCity("Jakarta");
        address.setProvince("DKI");
        address.setCountry("Indonesia");
        address.setPostalCode("123321");
        addressRepository.save(address);

        mockMvc.perform(delete("/api/contacts/test/addresses/test").
                        accept(MediaType.APPLICATION_JSON).
                        contentType(MediaType.APPLICATION_JSON).
                        header("X-API-TOKEN", "test")).
                andExpectAll(status().isOk()).andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertNull(response.getErrors());
                    assertEquals("OK", response.getData());
                    assertFalse(addressRepository.existsById("test"));
                });
    }

    @Test
    void listAddressBadRequest() throws Exception {
        mockMvc.perform(get("/api/contacts/wrong/addresses").
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
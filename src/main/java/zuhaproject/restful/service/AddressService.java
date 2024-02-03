package zuhaproject.restful.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import zuhaproject.restful.entity.Address;
import zuhaproject.restful.entity.Contact;
import zuhaproject.restful.entity.User;
import zuhaproject.restful.model.AddressResponse;
import zuhaproject.restful.model.CreateAddressRequest;
import zuhaproject.restful.repository.AddressRepository;
import zuhaproject.restful.repository.ContactRepository;

import java.util.UUID;

@Service
public class AddressService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired

    private AddressRepository addressRepository;

    @Autowired
    private ValidationService validationService;

    public AddressResponse create(User user, CreateAddressRequest request) {
        validationService.validate(request);

        Contact contact = contactRepository.findFirstByUserAndId(user, request.getContactId()).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact is not found!"));

        Address address = new Address();
        address.setId(UUID.randomUUID().toString());
        address.setContact(contact);
        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setCountry(request.getCountry());
        address.setProvince(request.getProvince());
        address.setPostalCode(request.getPostalCode());

        addressRepository.save(address);
        return toAddressResponse(address);
    }

    private AddressResponse toAddressResponse(Address address) {
        return AddressResponse.builder().
                id(address.getId()).
                street(address.getStreet()).
                city(address.getCity()).
                country(address.getCountry()).
                province(address.getProvince()).
                postalCode(address.getPostalCode()).
                build();
    }

    @Transactional(readOnly = true)
    public AddressResponse get(User user, String contactId, String addressId) {
        Contact contact = contactRepository.findFirstByUserAndId(user, contactId).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact is not found!"));

        Address address = addressRepository.findFirstByContactAndId(contact, addressId).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address is not found"));

        return toAddressResponse(address);
    }
}
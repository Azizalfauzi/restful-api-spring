package zuhaproject.restful.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zuhaproject.restful.entity.Contact;
import zuhaproject.restful.entity.User;
import zuhaproject.restful.model.ContactResponse;
import zuhaproject.restful.model.CreateContactRequest;
import zuhaproject.restful.repository.ContactRepository;

import java.util.UUID;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public ContactResponse create(User user, CreateContactRequest request) {
        validationService.validate(request);

        Contact contact = new Contact();
        contact.setId(UUID.randomUUID().toString());
        contact.setFirstName(request.getFirstName());
        contact.setLastName(request.getLastName());
        contact.setEmail(request.getEmail());
        contact.setPhone(request.getPhone());
        contact.setUser(user);

        contactRepository.save(contact);

        return ContactResponse.builder().
                id(contact.getId()).
                firstName(contact.getFirstName()).
                lastName(contact.getLastName()).
                email(contact.getEmail()).
                phone(contact.getPhone()).
                build();
    }
}

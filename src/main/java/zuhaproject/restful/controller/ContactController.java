package zuhaproject.restful.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import zuhaproject.restful.entity.User;
import zuhaproject.restful.model.ContactResponse;
import zuhaproject.restful.model.CreateContactRequest;
import zuhaproject.restful.model.WebResponse;
import zuhaproject.restful.service.ContactService;

@RestController
public class ContactController {

    @Autowired
    private ContactService contactService;


    @PostMapping(path = "/api/contacts",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<ContactResponse> create(User user, @RequestBody CreateContactRequest request) {
        ContactResponse contactResponse = contactService.create(user, request);
        return WebResponse.<ContactResponse>builder().data(contactResponse).build();
    }
}

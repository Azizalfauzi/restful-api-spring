package zuhaproject.restful.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zuhaproject.restful.model.LoginUserRequest;
import zuhaproject.restful.model.TokenResponse;
import zuhaproject.restful.repository.UserRepository;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ValidationService validationService;

//    public TokenResponse login(LoginUserRequest request) {
//        validationService.validate(request);
//
//    }
}

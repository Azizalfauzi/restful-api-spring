package zuhaproject.restful.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zuhaproject.restful.entity.User;
import zuhaproject.restful.exception.ApiException;
import zuhaproject.restful.model.RegisterUserRequest;
import zuhaproject.restful.repository.UserRepository;


import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Validator validator;

    public void register(RegisterUserRequest request) {
        Set<ConstraintViolation<RegisterUserRequest>> constraintViolations = validator.validate(request);
        if (constraintViolations.size() != 0) {
            throw new ConstraintViolationException(constraintViolations);
        }
        if (userRepository.existsById(request.getUsername())) {
            throw new ApiException("Username already registered!");
        }
        User user = new User();
        user.setUsername(request.getUsername());
//        user.setPassword();
    }
}

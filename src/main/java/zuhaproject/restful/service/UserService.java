package zuhaproject.restful.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import zuhaproject.restful.entity.User;
import zuhaproject.restful.model.RegisterUserRequest;
import zuhaproject.restful.model.UpdateUserRequest;
import zuhaproject.restful.model.UserReponse;
import zuhaproject.restful.repository.UserRepository;
import zuhaproject.restful.security.BCrypt;


import java.util.Objects;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public void register(RegisterUserRequest request) {
        validationService.validate(request);

        if (userRepository.existsById(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already registered!");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        user.setName(request.getName());

        userRepository.save(user);
    }

    public UserReponse get(User user) {
        return UserReponse.builder().
                username(user.getUsername()).
                name(user.getName()).build();
    }

    @Transactional
    public UserReponse update(User user, UpdateUserRequest request) {
        validationService.validate(request);
        if (Objects.nonNull(request.getName())) {
            user.setName(request.getName());
        }

        if (Objects.nonNull(request.getPassword())) {
            user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        }
        userRepository.save(user);

        return UserReponse.builder().
                name(user.getName())
                .username(user.getUsername())
                .build();
    }
}

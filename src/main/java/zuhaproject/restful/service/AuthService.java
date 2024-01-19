package zuhaproject.restful.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import zuhaproject.restful.entity.User;
import zuhaproject.restful.model.LoginUserRequest;
import zuhaproject.restful.model.TokenResponse;
import zuhaproject.restful.repository.UserRepository;
import zuhaproject.restful.security.BCrypt;

import java.util.UUID;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ValidationService validationService;

    @Transactional
    public TokenResponse login(LoginUserRequest request) {
        validationService.validate(request);

        User user = userRepository.findById(request.getUsername()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or Password wrong"));

        if (BCrypt.checkpw(request.getPassword(), user.getPassword())) {
//            sukses
            user.setToken(UUID.randomUUID().toString());
            user.setTokenExpiredAt(next30Day());
            userRepository.save(user);

            return TokenResponse.builder()
                    .token(user.getToken())
                    .expiredAt(user.getTokenExpiredAt())
                    .build();
        } else {
//            gagal
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or Password wrong");
        }
    }

    private Long next30Day() {
        return System.currentTimeMillis() + (1000 * 16 * 24 * 30);
    }
}

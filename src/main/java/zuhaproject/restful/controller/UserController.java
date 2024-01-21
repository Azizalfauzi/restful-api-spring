package zuhaproject.restful.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import zuhaproject.restful.entity.User;
import zuhaproject.restful.model.RegisterUserRequest;
import zuhaproject.restful.model.UserReponse;
import zuhaproject.restful.model.WebResponse;
import zuhaproject.restful.service.UserService;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping(path = "/api/users"
            , consumes = MediaType.APPLICATION_JSON_VALUE
            , produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<String> register(@RequestBody RegisterUserRequest request) {
        userService.register(request);
        return WebResponse.<String>builder().data("OK").build();
    }

    @GetMapping(path = "/api/users/current", produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<UserReponse> get(User user) {
        UserReponse userReponse = userService.get(user);
        return WebResponse.<UserReponse>builder().data(userReponse).build();
    }
}

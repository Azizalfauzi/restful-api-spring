package zuhaproject.restful.controller;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import zuhaproject.restful.entity.User;
import zuhaproject.restful.model.RegisterUserRequest;
import zuhaproject.restful.model.UpdateUserRequest;
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

    @PatchMapping(path = "/api/users/current",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<UserReponse> update(User user, @RequestBody UpdateUserRequest request) {
        UserReponse userReponse = userService.update(user, request);
        return WebResponse.<UserReponse>builder().data(userReponse).build();
    }
}

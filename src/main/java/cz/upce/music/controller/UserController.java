package cz.upce.music.controller;

import cz.upce.music.dto.SignUpUserDto;
import cz.upce.music.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/signup", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> processSignupUser(@RequestBody SignUpUserDto signUpUserDto) {
        try {
            SignUpUserDto newUser = userService.signUpUser(signUpUserDto);
            return ResponseEntity.ok(newUser);
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.getMessage());
        }
    }
}

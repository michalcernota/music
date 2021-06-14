package cz.upce.music.controller;

import cz.upce.music.dto.SignUpUserDto;
import cz.upce.music.entity.Users;
import cz.upce.music.service.interfaces.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/signup", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> signup(@RequestBody SignUpUserDto signUpUserDto) {
        try {
            SignUpUserDto newUser = userService.signUpUser(signUpUserDto);
            return ResponseEntity.ok(newUser);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @GetMapping("/user/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getUserRoles(@PathVariable String username) {
        Users user = userService.findUserByUsername(username);
        if (user != null) {
            return ResponseEntity.ok(user.getUserRole());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User was not found.");
        }
    }
}

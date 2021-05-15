package cz.upce.music.controller;

import cz.upce.music.dto.SignUpUserDto;
import cz.upce.music.entity.User;
import cz.upce.music.entity.UserRoleEnum;
import cz.upce.music.repository.UserRepository;
import cz.upce.music.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;

@Controller
public class UserController {

    private final UserRepository userRepository;

    private final UserService userService;

    private final ModelMapper mapper;

    public UserController(UserRepository userRepository, UserService userService, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.mapper = modelMapper;
    }

    @ExceptionHandler(RuntimeException.class)
    public String handlerException() {
        return "error";
    }

    @PostMapping(path = "/signup", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> processSignupUser(@RequestBody SignUpUserDto signUpUserDto) throws Exception {
        if (userRepository.findUserByUsername(signUpUserDto.getUsername()) == null) {
            if (!signUpUserDto.getPassword().equals(signUpUserDto.getRepeatPassword())) {
                throw new Exception("Passwords does not match.");
            }

            User user = new User();
            user.setUsername(signUpUserDto.getUsername());
            user.setPassword(signUpUserDto.getPassword());
            user.setUserRole(UserRoleEnum.ROLE_USER);
            user.setEmailAddress(signUpUserDto.getEmailAddress());
            user.setRegistrationDate(LocalDateTime.now());

            User newUser = userService.saveUser(user);
            signUpUserDto = mapper.map(newUser, SignUpUserDto.class);

            return ResponseEntity.ok(signUpUserDto);
        }
        else {
            throw new Exception("User with username " + signUpUserDto.getUsername() + " already exists.");
        }
    }
}

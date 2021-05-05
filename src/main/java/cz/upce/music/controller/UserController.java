package cz.upce.music.controller;

import cz.upce.music.dto.LoginUserDto;
import cz.upce.music.dto.SignUpUserDto;
import cz.upce.music.entity.User;
import cz.upce.music.entity.UserRoleEnum;
import cz.upce.music.repository.UserRepository;
import cz.upce.music.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;

@Controller
public class UserController {

    private final UserRepository userRepository;

    private final UserService userService;

    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @ExceptionHandler(RuntimeException.class)
    public String handlerException() {
        return "error";
    }

    @PostMapping("/login-form-process")
    public String processLoginUser(LoginUserDto loginUserDto) throws Exception {
        User user = userRepository.findUserByUsername(loginUserDto.getUserName());
        if (user != null) {
            if (user.getPassword().equals(loginUserDto.getPassword())) {
                //userService.setLoggedUser(user);
            }
        }
        else {
            throw new Exception("User " + loginUserDto.getUserName() + " already exists.");
        }

        return "redirect:/";
    }

    @PostMapping("/signup-form-process")
    public String processSignupUser(SignUpUserDto signUpUserDto) throws Exception {
        if (userRepository.findUserByUsername(signUpUserDto.getUserName()) == null) {
            User user = new User();
            user.setId(signUpUserDto.getId());
            user.setUsername(signUpUserDto.getUserName());
            user.setPassword(signUpUserDto.getPassword());
            user.setUserRole(UserRoleEnum.User);
            user.setEmailAddress(signUpUserDto.getEmail());
            user.setRegistrationDate(LocalDateTime.now());

            userService.saveUser(user);
        }
        else {
            throw new Exception("User " + signUpUserDto.getUserName() + " already exists.");
        }

        return "redirect:/";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new LoginUserDto());

        return "login";
    }

    @GetMapping({"/signup", "/signup/{id}"})
    public String signUp(@PathVariable(required = false) Long id, Model model) {
        SignUpUserDto signUpUserDto = new SignUpUserDto();
        if (id != null) {
            User user = userRepository.findById(id).get();
            signUpUserDto.setId(user.getId());
            signUpUserDto.setUserName(user.getUsername());
            signUpUserDto.setPassword(user.getPassword());
            signUpUserDto.setEmail(user.getEmailAddress());
        }
        model.addAttribute("user", signUpUserDto);

        return "signup";
    }

    @GetMapping("/account")
    public String getAccountInfo(Model model) {
        User loggedUser = userService.getLoggedUser();
        if (loggedUser != null) {
            model.addAttribute("user", loggedUser);
            return "user-detail";
        }
        else {
            return "redirect:/login";
        }
    }
}

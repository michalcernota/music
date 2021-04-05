package cz.upce.music.controller;

import cz.upce.music.dto.LoginUserDto;
import cz.upce.music.dto.SignUpUserDto;
import cz.upce.music.entity.User;
import cz.upce.music.entity.UserRoleEnum;
import cz.upce.music.repository.UserRepository;
import cz.upce.music.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @ExceptionHandler(RuntimeException.class)
    public String handlerException() {
        return "error";
    }

    @PostMapping("/login-form-process")
    public String processLoginUser(LoginUserDto loginUserDto) {
        User user = userRepository.findUserByUsername(loginUserDto.getUserName());
        if (user != null) {
            if (user.getPassword().equals(loginUserDto.getPassword())) {
                userService.setLoggedUser(user);
            }
        }
        else {
            // TODO: udělat nějak chybovou hlášku, když uživatel s tímto jménem neexistuje
        }

        return "redirect:/";
    }

    @PostMapping("/signup-form-process")
    public String processSignupUser(SignUpUserDto signUpUserDto) {
        if (userRepository.findUserByUsernameOrEmailAddress(signUpUserDto.getUserName(), signUpUserDto.getEmail()) == null) {
            User user = new User();
            user.setUsername(signUpUserDto.getUserName());
            user.setPassword(signUpUserDto.getPassword());
            user.setUserRole(UserRoleEnum.User);
            user.setEmailAddress(signUpUserDto.getEmail());
            user.setRegistrationDate(LocalDateTime.now());

            userRepository.save(user);
        }
        else {
            // TODO: udělat nějak chybovou hlášku, když uživatel už existuje
        }

        return "redirect:/";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new LoginUserDto());

        return "login";
    }

    @GetMapping("/signup")
    public String signUp(Model model) {
        model.addAttribute("user", new SignUpUserDto());

        return "signup";
    }
}

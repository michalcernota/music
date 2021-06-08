package cz.upce.music.service;

import cz.upce.music.dto.SignUpUserDto;
import cz.upce.music.entity.User;
import cz.upce.music.entity.UserRoleEnum;
import cz.upce.music.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.time.LocalDateTime;

@Service
@SessionScope
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final ModelMapper mapper;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = modelMapper;
    }

    @Override
    public User getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findUserByUsername(authentication.getName());
    }

    @Override
    public User saveUser(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());

        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    @Override
    public SignUpUserDto signUpUser(SignUpUserDto signUpUserDto) throws Exception {
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

            User newUser = saveUser(user);
            return mapper.map(newUser, SignUpUserDto.class);
        }
        else {
            throw new Exception("User with username " + signUpUserDto.getUsername() + " already exists.");
        }
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}

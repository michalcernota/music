package cz.upce.music.service.implementations;

import cz.upce.music.dto.SignUpUserDto;
import cz.upce.music.entity.Users;
import cz.upce.music.entity.UserRoleEnum;
import cz.upce.music.repository.UserRepository;
import cz.upce.music.service.interfaces.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import javax.persistence.EntityExistsException;
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
    public Users getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findUserByUsername(authentication.getName());
    }

    @Override
    public Users saveUser(Users user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        return userRepository.save(user);
    }

    @Override
    public SignUpUserDto signUpUser(SignUpUserDto signUpUserDto) {
        if (userRepository.findUserByUsername(signUpUserDto.getUsername()) == null) {
            if (!signUpUserDto.getPassword().equals(signUpUserDto.getRepeatPassword())) {
                throw new SecurityException("Passwords does not match.");
            }

            Users user = new Users();
            user.setUsername(signUpUserDto.getUsername());
            user.setPassword(signUpUserDto.getPassword());
            user.setUserRole(UserRoleEnum.ROLE_USER);
            user.setEmailAddress(signUpUserDto.getEmailAddress());
            user.setRegistrationDate(LocalDateTime.now());

            Users newUser = saveUser(user);
            return mapper.map(newUser, SignUpUserDto.class);
        }
        else {
            throw new EntityExistsException("User with username " + signUpUserDto.getUsername() + " already exists.");
        }
    }

    @Override
    public Users findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}

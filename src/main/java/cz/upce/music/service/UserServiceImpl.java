package cz.upce.music.service;

import cz.upce.music.entity.User;
import cz.upce.music.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

@Service
@SessionScope
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private User loggedUser;

    @Override
    public void setLoggedUser(User user) {
        loggedUser = user;
    }

    @Override
    public User getLoggedUser() {
        return loggedUser;
    }

    @Override
    public void saveUser(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());

        user.setPassword(encodedPassword);
        userRepository.save(user);
    }
}

package cz.upce.music.service.implementations;

import cz.upce.music.entity.Users;
import cz.upce.music.repository.UsersRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UsersRepository userRepository;

    public CustomUserDetailService(UsersRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findByUsername(username);
        if (user != null) {
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), Collections.singletonList(new SimpleGrantedAuthority(user.getUserRole().toString())));
        }

        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}

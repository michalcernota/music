package cz.upce.music.service.interfaces;

import cz.upce.music.dto.SignUpUserDto;
import cz.upce.music.entity.User;

public interface UserService {

    User getLoggedUser();

    User saveUser(User user);

    SignUpUserDto signUpUser(SignUpUserDto signUpUserDto) throws Exception;

    User findUserByUsername(String name);
}

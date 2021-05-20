package cz.upce.music.service;

import cz.upce.music.dto.SignUpUserDto;
import cz.upce.music.entity.User;

public interface UserService {

    User getLoggedUser();

    User saveUser(User user);

    SignUpUserDto signUpUser(SignUpUserDto signUpUserDto) throws Exception;
}

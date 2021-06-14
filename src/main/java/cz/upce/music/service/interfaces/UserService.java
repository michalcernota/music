package cz.upce.music.service.interfaces;

import cz.upce.music.dto.SignUpUserDto;
import cz.upce.music.entity.Users;

public interface UserService {

    Users getLoggedUser();

    Users saveUser(Users user);

    SignUpUserDto signUpUser(SignUpUserDto signUpUserDto) throws Exception;

    Users findUserByUsername(String name);
}

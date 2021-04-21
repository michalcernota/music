package cz.upce.music.service;

import cz.upce.music.entity.User;

public interface UserService {

    User getLoggedUser();

    void saveUser(User user);
}

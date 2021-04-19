package cz.upce.music.service;

import cz.upce.music.entity.User;

public interface UserService {

    void setLoggedUser(User user);

    User getLoggedUser();

    void saveUser(User user);
}

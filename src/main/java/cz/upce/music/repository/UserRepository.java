package cz.upce.music.repository;

import cz.upce.music.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = "playlists")
    User findUserByUsername(String username);

    @EntityGraph(attributePaths = "playlists")
    User findUserByEmailAddress(String emailAddress);
}

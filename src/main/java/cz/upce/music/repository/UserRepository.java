package cz.upce.music.repository;

import cz.upce.music.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = "playlists")
    User findUserByUsername(String username);

    @EntityGraph(attributePaths = "playlists")
    User findUserByEmailAddress(String emailAddress);

    @EntityGraph(attributePaths = "playlists")
    @Query("SELECT u from User u where u.username = ?1 or u.emailAddress = ?2")
    User findUserByUsernameOrEmailAddress(String username, String emailAddress);
}

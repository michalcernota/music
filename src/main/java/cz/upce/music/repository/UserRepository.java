package cz.upce.music.repository;

import cz.upce.music.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = "usersPlaylists")
    User findUserByUsername(String username);

    @EntityGraph(attributePaths = "usersPlaylists")
    User findByUsername(String username);

    @EntityGraph(attributePaths = "usersPlaylists")
    User findUserByEmailAddress(String emailAddress);

    @EntityGraph(attributePaths = "usersPlaylists")
    @Query("SELECT u from User u where u.username = ?1 or u.emailAddress = ?2")
    User findUserByUsernameOrEmailAddress(String username, String emailAddress);
}

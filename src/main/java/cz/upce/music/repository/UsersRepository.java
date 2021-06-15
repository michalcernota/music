package cz.upce.music.repository;

import cz.upce.music.entity.Users;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    @EntityGraph(attributePaths = "usersPlaylists")
    Users findUserByUsername(String username);

    @EntityGraph(attributePaths = "usersPlaylists")
    Users findByUsername(String username);

    @EntityGraph(attributePaths = "usersPlaylists")
    Users findUserByEmailAddress(String emailAddress);

    @EntityGraph(attributePaths = "usersPlaylists")
    @Query("SELECT u from Users u where u.username = ?1 or u.emailAddress = ?2")
    Users findUserByUsernameOrEmailAddress(String username, String emailAddress);
}

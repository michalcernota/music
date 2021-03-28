package cz.upce.music.repository;

import cz.upce.music.entity.Playlist;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    @EntityGraph(attributePaths = {"trackOfPlaylist", "user"})
    Playlist findByName(String name);

    @EntityGraph(attributePaths = {"trackOfPlaylist", "user"})
    Playlist findByUserUsername(String username);

    @EntityGraph(attributePaths = {"trackOfPlaylist", "user"})
    Playlist findByUserId(Long id);
}

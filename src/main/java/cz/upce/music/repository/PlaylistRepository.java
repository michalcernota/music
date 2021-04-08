package cz.upce.music.repository;

import cz.upce.music.entity.Playlist;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    @EntityGraph(attributePaths = {"trackOfPlaylist", "user"})
    Playlist findByName(String name);
}

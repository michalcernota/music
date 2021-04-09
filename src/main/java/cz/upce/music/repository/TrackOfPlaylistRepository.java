package cz.upce.music.repository;

import cz.upce.music.entity.TrackOfPlaylist;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface TrackOfPlaylistRepository extends JpaRepository<TrackOfPlaylist, Long> {
    @EntityGraph(attributePaths = {"playlist","track"})
    Set<TrackOfPlaylist> findByPlaylistId(Long id);

    int countAllByPlaylistId(Long id);
}

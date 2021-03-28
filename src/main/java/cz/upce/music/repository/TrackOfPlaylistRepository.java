package cz.upce.music.repository;

import cz.upce.music.entity.TrackOfPlaylist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackOfPlaylistRepository extends JpaRepository<TrackOfPlaylist, Long> {
}

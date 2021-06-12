package cz.upce.music.repository;

import cz.upce.music.entity.TrackOfPlaylist;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface TrackOfPlaylistRepository extends JpaRepository<TrackOfPlaylist, Long> {
    @EntityGraph(attributePaths = {"playlist","track"})
    Set<TrackOfPlaylist> findByPlaylistId(Long id);

    @Query("select t.track.id from TrackOfPlaylist t")
    Set<Long> getAllTrackIds();

    void deleteTrackOfPlaylistsByTrack_Id(Long id);

    void deleteTrackOfPlaylistsByPlaylist_Id(Long id);
}

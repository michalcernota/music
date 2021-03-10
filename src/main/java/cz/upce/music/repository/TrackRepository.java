package cz.upce.music.repository;

import cz.upce.music.entity.Track;
import cz.upce.music.entity.TrackType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TrackRepository extends JpaRepository<Track, Long> {
    @Query("select t from Track t where t.id between 1 and 2")
    List<Track> findTracksByIdBetween(Long start, Long end);

    Track findTrackByNameContains(String contains);

    List<Track> findTrackByTrackTypeIs(TrackType trackType);

    @EntityGraph(attributePaths = "played")
    Optional<Track> findById(Long id);
}

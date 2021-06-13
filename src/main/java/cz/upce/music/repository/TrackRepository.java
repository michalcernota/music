package cz.upce.music.repository;

import cz.upce.music.entity.Track;
import cz.upce.music.entity.TrackEnum;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface TrackRepository extends JpaRepository<Track, Long> {
    List<Track> findTrackByTrackTypeIs(TrackEnum trackType);

    Optional<Track> findById(Long id);

    List<Track> findTracksByArtist_Id(Long id);

    @EntityGraph(attributePaths = "artist")
    @Query("select t from Track t where t.id not in ?1")
    List<Track> findTracksByIdIsNotIn(Collection<Long> ids);

    void deleteTracksByArtist_Id(Long id);

    @Query("select max(t.id) from Track t")
    Optional<Long> getMaxId();
}

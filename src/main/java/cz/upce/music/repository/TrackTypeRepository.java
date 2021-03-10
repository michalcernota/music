package cz.upce.music.repository;

import cz.upce.music.entity.TrackType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrackTypeRepository extends JpaRepository<TrackType, Long> {
    @EntityGraph(attributePaths = "tracks")
    Optional<TrackType> findById(Long aLong);
}

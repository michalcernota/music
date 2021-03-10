package cz.upce.music.repository;

import cz.upce.music.entity.Album;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlbumRepository extends JpaRepository<Album, Long> {
    @EntityGraph(attributePaths = "tracks")
    Optional<Album> findById(Long id);
}

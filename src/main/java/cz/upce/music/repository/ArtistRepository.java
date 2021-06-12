package cz.upce.music.repository;

import cz.upce.music.entity.Artist;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
    Artist findArtistByNameIs(String name);

    @EntityGraph(attributePaths = {"tracks"})
    Optional<Artist> findById(Long id);

    List<Artist> findAllByNameIs(String name);
}

package cz.upce.music.repository;

import cz.upce.music.entity.Playlist;
import cz.upce.music.entity.Track;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    @Override
    @EntityGraph(attributePaths = {"trackOfPlaylist", "owner", "usersPlaylists"})
    List<Playlist> findAll();

    @EntityGraph(attributePaths = {"trackOfPlaylist", "owner", "usersPlaylists"})
    List<Playlist> findPlaylistsByIdNotIn(List<Long> id);
}

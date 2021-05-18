package cz.upce.music.repository;

import cz.upce.music.entity.UsersPlaylist;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsersPlaylistsRepository extends JpaRepository<UsersPlaylist, Long> {
    @EntityGraph(attributePaths = {"user", "playlist"})
    List<UsersPlaylist> findAllByUser_Id(Long userId);

    void deleteUsersPlaylistsByPlaylist_Id(Long id);
}

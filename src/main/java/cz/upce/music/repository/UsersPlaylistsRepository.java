package cz.upce.music.repository;

import cz.upce.music.entity.UsersPlaylist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersPlaylistsRepository extends JpaRepository<UsersPlaylist, Long> {
}

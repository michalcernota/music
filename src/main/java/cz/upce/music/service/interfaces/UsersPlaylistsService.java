package cz.upce.music.service.interfaces;

import cz.upce.music.dto.TrackDto;
import cz.upce.music.entity.Track;
import cz.upce.music.entity.UsersPlaylist;

import java.util.List;

public interface UsersPlaylistsService {

    UsersPlaylist addPlaylistsToMyPlaylists(Long playlistId);

    List<UsersPlaylist> getMyPlaylists();

    UsersPlaylist removeFromMyPlaylists(Long playlistId);

    List<TrackDto> getTracks(Long playlistId);
}

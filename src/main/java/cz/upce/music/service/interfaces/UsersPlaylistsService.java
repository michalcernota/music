package cz.upce.music.service.interfaces;

import cz.upce.music.dto.TrackDto;
import cz.upce.music.dto.UsersPlaylistDto;

import java.util.List;

public interface UsersPlaylistsService {

    UsersPlaylistDto addToMyPlaylists(Long playlistId);

    List<UsersPlaylistDto> getMyPlaylists();

    UsersPlaylistDto removeFromMyPlaylists(Long playlistId);

    List<TrackDto> getTracksOfPlaylist(Long playlistId);
}

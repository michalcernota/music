package cz.upce.music.service.interfaces;

import cz.upce.music.dto.PlaylistDto;
import cz.upce.music.dto.TrackOfPlaylistDto;

import java.util.List;

public interface PlaylistService {

    List<PlaylistDto> getAll();

    PlaylistDto create(PlaylistDto playlistDto);

    void delete(Long id);

    PlaylistDto getPlaylistDetail(Long id);

    TrackOfPlaylistDto addTrackToPlaylist(TrackOfPlaylistDto trackOfPlaylistDto);

    void removeTrack(TrackOfPlaylistDto trackOfPlaylistDto);

}

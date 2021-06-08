package cz.upce.music.service.interfaces;

import cz.upce.music.dto.PlaylistDto;
import cz.upce.music.dto.TrackOfPlaylistDto;

import java.util.List;
import java.util.Map;

public interface PlaylistService {

    List<PlaylistDto> getAll();

    PlaylistDto create(PlaylistDto playlistDto);

    boolean delete(Long id);

    Map<String, Object> getPlaylistDetail(Long id);

    TrackOfPlaylistDto addTrackToPlaylist(TrackOfPlaylistDto trackOfPlaylistDto);

    void removeTrack(TrackOfPlaylistDto trackOfPlaylistDto);

}

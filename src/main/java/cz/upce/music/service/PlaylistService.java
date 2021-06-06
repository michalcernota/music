package cz.upce.music.service;

import cz.upce.music.dto.PlaylistDto;
import cz.upce.music.dto.TrackOfPlaylistDto;
import cz.upce.music.entity.*;
import cz.upce.music.repository.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.*;

@Service
public class PlaylistService {

    private final PlaylistRepository playlistRepository;

    private final UserRepository userRepository;

    private final UsersPlaylistsRepository usersPlaylistsRepository;

    private final TrackOfPlaylistRepository trackOfPlaylistRepository;

    private final TrackRepository trackRepository;

    private final ModelMapper mapper;

    public PlaylistService(PlaylistRepository playlistRepository, UserRepository userRepository, UsersPlaylistsRepository usersPlaylistsRepository, TrackOfPlaylistRepository trackOfPlaylistRepository, TrackRepository trackRepository, ModelMapper mapper) {
        this.playlistRepository = playlistRepository;
        this.userRepository = userRepository;
        this.usersPlaylistsRepository = usersPlaylistsRepository;
        this.trackOfPlaylistRepository = trackOfPlaylistRepository;
        this.trackRepository = trackRepository;
        this.mapper = mapper;
    }

    public List<PlaylistDto> getAll() {
        List<Playlist> playlists = playlistRepository.findAll();
        Type listType = new TypeToken<List<PlaylistDto>>(){}.getType();
        List<PlaylistDto> playlistDtoList = mapper.map(playlists, listType);
        for (int i = 0; i < playlistDtoList.size(); i++) {
            playlistDtoList.get(i).setOwnerName(playlists.get(i).getOwner().getUsername());
        }
        return playlistDtoList;
    }

    public PlaylistDto create(PlaylistDto playlistDto) {
        User owner = userRepository.findByUsername(playlistDto.getOwnerName());

        Playlist playlist = new Playlist();
        playlist.setId(playlistDto.getId());
        playlist.setName(playlistDto.getName());
        playlist.setOwner(owner);

        UsersPlaylist usersPlaylist = new UsersPlaylist();
        usersPlaylist.setPlaylist(playlist);
        usersPlaylist.setUser(owner);

        Playlist result = playlistRepository.save(playlist);
        usersPlaylistsRepository.save(usersPlaylist);
        return mapper.map(result, PlaylistDto.class);
    }

    public boolean delete(Long id) {
        if (playlistRepository.findById(id).isPresent()) {
            usersPlaylistsRepository.deleteUsersPlaylistsByPlaylist_Id(id);
            trackOfPlaylistRepository.deleteTrackOfPlaylistsByPlaylist_Id(id);
            playlistRepository.deleteById(id);

            return true;
        }
        else {
            return false;
        }
    }

    public Map<String, Object> getPlaylistDetail(Long id) {
        Map<String, Object> map = new HashMap<>();

        Optional<Playlist> optionalPlaylist = playlistRepository.findById(id);
        if (!optionalPlaylist.isPresent()) {
            return null;
        }

        Playlist playlist = optionalPlaylist.get();

        Set<TrackOfPlaylist> trackOfPlaylists = trackOfPlaylistRepository.findByPlaylistId(playlist.getId());
        List<TrackOfPlaylistDto> dtoList = mapper.map(trackOfPlaylists,
                new TypeToken<List<TrackOfPlaylistDto>>(){}.getType());

        PlaylistDto playlistDto = mapper.map(playlist, PlaylistDto.class);
        playlistDto.setOwnerName(playlist.getOwner().getUsername());

        map.put("playlist", playlistDto);
        map.put("tracksCount", dtoList.size());
        map.put("tracksOfPlaylist", dtoList);

        return map;
    }

    public TrackOfPlaylistDto addTrackToPlaylist(TrackOfPlaylistDto trackOfPlaylistDto) {
        Optional<Playlist> optionalPlaylist = playlistRepository.findById(trackOfPlaylistDto.getPlaylistId());
        Optional<Track> optionalTrack = trackRepository.findById(trackOfPlaylistDto.getTrackId());

        if (optionalTrack.isPresent() && optionalPlaylist.isPresent()) {
            TrackOfPlaylist trackOfPlaylist = new TrackOfPlaylist();
            trackOfPlaylist.setPlaylist(optionalPlaylist.get());
            trackOfPlaylist.setTrack(optionalTrack.get());
            trackOfPlaylistRepository.save(trackOfPlaylist);

            trackOfPlaylistDto.setId(trackOfPlaylist.getId());
            return trackOfPlaylistDto;
        }

        return null;
    }

    public void removeTrack(TrackOfPlaylistDto trackOfPlaylistDto) {
        trackOfPlaylistRepository.deleteById(trackOfPlaylistDto.getId());
    }

}

package cz.upce.music.service.implementations;

import cz.upce.music.dto.TrackDto;
import cz.upce.music.dto.UsersPlaylistDto;
import cz.upce.music.entity.*;
import cz.upce.music.repository.PlaylistRepository;
import cz.upce.music.repository.TrackOfPlaylistRepository;
import cz.upce.music.repository.UsersPlaylistsRepository;
import cz.upce.music.service.interfaces.UserService;
import cz.upce.music.service.interfaces.UsersPlaylistsService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UsersPlaylistServiceImpl implements UsersPlaylistsService {

    private final UserService userService;

    private final PlaylistRepository playlistRepository;

    private final UsersPlaylistsRepository usersPlaylistsRepository;

    private final TrackOfPlaylistRepository trackOfPlaylistRepository;

    private final ModelMapper mapper;

    public UsersPlaylistServiceImpl(UserService userService, PlaylistRepository playlistRepository, UsersPlaylistsRepository usersPlaylistsRepository, TrackOfPlaylistRepository trackOfPlaylistRepository, ModelMapper mapper) {
        this.userService = userService;
        this.playlistRepository = playlistRepository;
        this.usersPlaylistsRepository = usersPlaylistsRepository;
        this.trackOfPlaylistRepository = trackOfPlaylistRepository;
        this.mapper = mapper;
    }

    @Override
    public UsersPlaylistDto addToMyPlaylists(Long playlistId) {
        Users user = userService.getLoggedUser();

        Optional<Playlist> optionalPlaylist = playlistRepository.findById(playlistId);
        if (!optionalPlaylist.isPresent() || user == null) {
            throw new NoSuchElementException("Playlist was not found.");
        }

        Playlist playlist = optionalPlaylist.get();

        UsersPlaylist usersPlaylist = new UsersPlaylist();
        usersPlaylist.setUser(user);
        usersPlaylist.setPlaylist(playlist);
        UsersPlaylist saved = usersPlaylistsRepository.save(usersPlaylist);

        return mapper.map(saved, UsersPlaylistDto.class);
    }

    @Override
    public List<UsersPlaylistDto> getMyPlaylists() {
        Users user = userService.getLoggedUser();
        List<UsersPlaylist> usersPlaylists = usersPlaylistsRepository.findAllByUser_Id(user.getId());
        return mapper.map(usersPlaylists, new TypeToken<List<UsersPlaylistDto>>(){}.getType());
    }

    @Override
    public UsersPlaylistDto removeFromMyPlaylists(Long playlistId) {
        Optional<UsersPlaylist> usersPlaylistOptional = usersPlaylistsRepository.findById(playlistId);
        if (!usersPlaylistOptional.isPresent()) {
            throw new NoSuchElementException("Playlist was not found.");
        }

        usersPlaylistsRepository.deleteById(playlistId);
        UsersPlaylist usersPlaylist = usersPlaylistOptional.get();
        return mapper.map(usersPlaylist, UsersPlaylistDto.class);
    }

    @Override
    public List<TrackDto> getTracksOfPlaylist(Long playlistId) {
        Optional<UsersPlaylist> optionalUsersPlaylist = usersPlaylistsRepository.findById(playlistId);
        if (optionalUsersPlaylist.isPresent()) {
            UsersPlaylist usersPlaylist = optionalUsersPlaylist.get();

            Playlist playlist = usersPlaylist.getPlaylist();
            Set<TrackOfPlaylist> tracksOfPlaylist =  trackOfPlaylistRepository.findByPlaylistId(playlist.getId());

            List<TrackDto> tracks = new ArrayList<>();
            for (TrackOfPlaylist trackOfPlaylist: tracksOfPlaylist) {
                Track track = trackOfPlaylist.getTrack();
                tracks.add(mapper.map(track, TrackDto.class));
            }

            return tracks;
        }

        throw new NoSuchElementException("Playlist was not found.");
    }
}

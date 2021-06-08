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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    public UsersPlaylist addPlaylistsToMyPlaylists(Long playlistId) {
        User user = userService.getLoggedUser();

        Optional<Playlist> optionalPlaylist = playlistRepository.findById(playlistId);
        if (!optionalPlaylist.isPresent() || user == null) {
            return null;
        }

        Playlist playlist = optionalPlaylist.get();

        UsersPlaylist usersPlaylist = new UsersPlaylist();
        usersPlaylist.setUser(user);
        usersPlaylist.setPlaylist(playlist);
        UsersPlaylist result = usersPlaylistsRepository.save(usersPlaylist);

        return result;
    }

    @Override
    public List<UsersPlaylist> getMyPlaylists() {
        User user = userService.getLoggedUser();
        List<UsersPlaylist> usersPlaylists = usersPlaylistsRepository.findAllByUser_Id(user.getId());
        return usersPlaylists;
    }

    @Override
    public UsersPlaylist removeFromMyPlaylists(Long playlistId) {
        Optional<UsersPlaylist> usersPlaylistOptional = usersPlaylistsRepository.findById(playlistId);
        if (!usersPlaylistOptional.isPresent()) {
            return null;
        }

        usersPlaylistsRepository.deleteById(playlistId);
        return usersPlaylistOptional.get();
    }

    @Override
    public List<TrackDto> getTracks(Long playlistId) {
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

        return null;
    }
}

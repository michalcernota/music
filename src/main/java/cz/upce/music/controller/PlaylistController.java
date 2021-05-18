package cz.upce.music.controller;

import cz.upce.music.dto.PlaylistDto;
import cz.upce.music.dto.TrackOfPlaylistDto;
import cz.upce.music.entity.*;
import cz.upce.music.repository.*;
import cz.upce.music.service.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.lang.reflect.Type;
import java.util.*;

@RestController
public class PlaylistController {

    private final PlaylistRepository playlistRepository;

    private final TrackOfPlaylistRepository trackOfPlaylistRepository;

    private final TrackRepository trackRepository;

    private final UsersPlaylistsRepository usersPlaylistsRepository;

    private final UserRepository userRepository;

    private final ModelMapper mapper;

    public PlaylistController(PlaylistRepository playlistRepository, TrackOfPlaylistRepository trackOfPlaylistRepository, TrackRepository trackRepository, UsersPlaylistsRepository usersPlaylistsRepository, UserService userService, ModelMapper modelMapper, UserRepository userRepository) {
        this.playlistRepository = playlistRepository;
        this.trackOfPlaylistRepository = trackOfPlaylistRepository;
        this.trackRepository = trackRepository;
        this.usersPlaylistsRepository = usersPlaylistsRepository;
        this.userRepository = userRepository;
        this.mapper = modelMapper;
    }

    @GetMapping("/playlists")
    public List<PlaylistDto> viewPlaylists() {
        List<Playlist> playlists = playlistRepository.findAll();
        Type listType = new TypeToken<List<PlaylistDto>>(){}.getType();
        return mapper.map(playlists, listType);
    }

    @PostMapping("/playlists/add")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> addNewPlaylist(@RequestBody PlaylistDto playlistDto) {
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

        return ResponseEntity.ok(mapper.map(result, PlaylistDto.class));
    }

    @Transactional
    @DeleteMapping("/playlists/delete/{id}")
    public ResponseEntity<?> deletePlaylist(@PathVariable Long id) {
        if (playlistRepository.findById(id).isPresent()) {
            usersPlaylistsRepository.deleteUsersPlaylistsByPlaylist_Id(id);
            trackOfPlaylistRepository.deleteTrackOfPlaylistsByPlaylist_Id(id);
            playlistRepository.deleteById(id);

            return ResponseEntity.ok("Playlist deleted successfully.");
        }
        else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not found.");
        }
    }

    @GetMapping("/playlists/{id}")
    public ResponseEntity<?> showPlaylistDetail(@PathVariable Long id) throws Exception {
        Map<String, Object> map = new HashMap<>();

        Optional<Playlist> optionalPlaylist = playlistRepository.findById(id);
        Playlist playlist;
        if (!optionalPlaylist.isPresent()) {
            throw new Exception("Playlist not found.");
        }
        else {
            playlist = optionalPlaylist.get();
        }

        Set<TrackOfPlaylist> trackOfPlaylists = trackOfPlaylistRepository.findByPlaylistId(playlist.getId());

        Type listType = new TypeToken<List<TrackOfPlaylistDto>>(){}.getType();
        List<TrackOfPlaylistDto> dtoList = mapper.map(trackOfPlaylists, listType);
        PlaylistDto playlistDto = mapper.map(playlist, PlaylistDto.class);
        playlistDto.setOwnerName(playlist.getOwner().getUsername());

        map.put("playlist", playlistDto);
        map.put("tracksCount", dtoList.size());
        map.put("tracksOfPlaylist", dtoList);
        return ResponseEntity.ok(map);
    }

    @PostMapping("/playlists/addTrack")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> addTrackToPlaylist(@RequestBody TrackOfPlaylistDto trackOfPlaylistDto) throws Exception {
        Optional<Playlist> optionalPlaylist = playlistRepository.findById(trackOfPlaylistDto.getPlaylistId());
        Optional<Track> optionalTrack = trackRepository.findById(trackOfPlaylistDto.getTrackId());

        if (optionalTrack.isPresent() && optionalPlaylist.isPresent()) {
            TrackOfPlaylist trackOfPlaylist = new TrackOfPlaylist();
            trackOfPlaylist.setPlaylist(optionalPlaylist.get());
            trackOfPlaylist.setTrack(optionalTrack.get());
            trackOfPlaylistRepository.save(trackOfPlaylist);

            trackOfPlaylistDto.setId(trackOfPlaylist.getId());
            return ResponseEntity.ok(trackOfPlaylistDto);
        }

        throw new Exception("Playlist or track was not found.");
    }

    @DeleteMapping("/playlists/deleteTrack")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> removeTrackFromPlaylist(@RequestBody TrackOfPlaylistDto trackOfPlaylistDto) throws Exception {
        trackOfPlaylistRepository.deleteById(trackOfPlaylistDto.getId());
        return ResponseEntity.ok(trackOfPlaylistDto);
    }
}

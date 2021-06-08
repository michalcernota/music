package cz.upce.music.controller;

import cz.upce.music.dto.TrackDto;
import cz.upce.music.dto.UsersPlaylistDto;
import cz.upce.music.entity.UsersPlaylist;
import cz.upce.music.service.interfaces.UsersPlaylistsService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.lang.reflect.Type;
import java.util.List;

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
public class UsersPlaylistsController {

    private final ModelMapper mapper;

    private final UsersPlaylistsService usersPlaylistsService;

    public UsersPlaylistsController(ModelMapper mapper, UsersPlaylistsService usersPlaylistsService) {
        this.mapper = mapper;
        this.usersPlaylistsService = usersPlaylistsService;
    }

    @PostMapping("/usersPlaylists/add/{playlistId}")
    public ResponseEntity<?> addPlaylistsToMyPlaylists(@PathVariable Long playlistId) {
        UsersPlaylist usersPlaylist = usersPlaylistsService.addPlaylistsToMyPlaylists(playlistId);
        if (usersPlaylist != null) {
            return ResponseEntity.ok(mapper.map(usersPlaylist, UsersPlaylistDto.class));
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not found.");
        }
    }

    @GetMapping("/usersPlaylists")
    public ResponseEntity<?> getMyPlaylists() {
        List<UsersPlaylist> usersPlaylists = usersPlaylistsService.getMyPlaylists();

        Type listType = new TypeToken<List<UsersPlaylistDto>>(){}.getType();
        List<UsersPlaylistDto> dtoList = mapper.map(usersPlaylists, listType);

        return ResponseEntity.ok(dtoList);
    }

    @Transactional
    @DeleteMapping("/usersPlaylists/remove/{id}")
    public ResponseEntity<?> removeFromMyPlaylists(@PathVariable Long id) {
        UsersPlaylist removed = usersPlaylistsService.removeFromMyPlaylists(id);
        if (removed != null) {
            return ResponseEntity.ok(mapper.map(removed, UsersPlaylistDto.class));
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not found.");
        }
    }

    @GetMapping("/usersPlaylists/{id}/tracks")
    public ResponseEntity<?> getTracks(@PathVariable Long id) {
        List<TrackDto> tracks = usersPlaylistsService.getTracks(id);
        if (tracks != null) {
            return ResponseEntity.ok(tracks);
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not found.");
        }
    }
}

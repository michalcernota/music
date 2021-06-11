package cz.upce.music.controller;

import cz.upce.music.dto.TrackDto;
import cz.upce.music.dto.UsersPlaylistDto;
import cz.upce.music.service.interfaces.UsersPlaylistsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
public class UsersPlaylistsController {

    private final UsersPlaylistsService usersPlaylistsService;

    public UsersPlaylistsController(UsersPlaylistsService usersPlaylistsService) {
        this.usersPlaylistsService = usersPlaylistsService;
    }

    @PostMapping("/user/playlists/{playlistId}")
    public ResponseEntity<?> addToMyPlaylists(@PathVariable Long playlistId) {
        try {
            UsersPlaylistDto usersPlaylist = usersPlaylistsService.addToMyPlaylists(playlistId);
            return ResponseEntity.ok(usersPlaylist);
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @GetMapping("/user/playlists")
    public ResponseEntity<?> getMyPlaylists() {
        List<UsersPlaylistDto> usersPlaylistDtoList = usersPlaylistsService.getMyPlaylists();
        return ResponseEntity.ok(usersPlaylistDtoList);
    }

    @Transactional
    @DeleteMapping("/user/playlists/{id}")
    public ResponseEntity<?> removeFromMyPlaylists(@PathVariable Long id) {
        try {
            UsersPlaylistDto usersPlaylistDto = usersPlaylistsService.removeFromMyPlaylists(id);
            return ResponseEntity.ok(usersPlaylistDto);
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @GetMapping("/user/playlists/{id}")
    public ResponseEntity<?> getTracks(@PathVariable Long id) {
        List<TrackDto> tracks = usersPlaylistsService.getTracksOfPlaylist(id);
        if (tracks != null) {
            return ResponseEntity.ok(tracks);
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not found.");
        }
    }
}

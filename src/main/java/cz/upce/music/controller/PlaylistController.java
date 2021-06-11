package cz.upce.music.controller;

import cz.upce.music.dto.PlaylistDto;
import cz.upce.music.dto.TrackOfPlaylistDto;
import cz.upce.music.service.interfaces.PlaylistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@RestController
public class PlaylistController {

    private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @GetMapping("/playlists")
    public ResponseEntity<?> getAllPlaylists() {
        return ResponseEntity.ok(playlistService.getAll());
    }

    @PostMapping("/playlists")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> createPlaylist(@RequestBody PlaylistDto playlistDto) {
        PlaylistDto result = playlistService.create(playlistDto);
        return ResponseEntity.ok(result);
    }

    @Transactional
    @DeleteMapping("/playlists/{id}")
    public ResponseEntity<?> deletePlaylist(@PathVariable Long id) {
        try {
            playlistService.delete(id);
            return ResponseEntity.ok().build();
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @GetMapping("/playlists/{id}")
    public ResponseEntity<?> getPlaylistDetail(@PathVariable Long id) {
        try {
            PlaylistDto playlistDto = playlistService.getPlaylistDetail(id);
            return ResponseEntity.ok(playlistDto);
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @PostMapping("/playlists/tracks")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> addTrackToPlaylist(@RequestBody TrackOfPlaylistDto trackOfPlaylistDto) {
        try {
            TrackOfPlaylistDto result = playlistService.addTrackToPlaylist(trackOfPlaylistDto);
            return ResponseEntity.ok(result);
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @DeleteMapping("/playlists/tracks")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> removeTrackFromPlaylist(@RequestBody TrackOfPlaylistDto trackOfPlaylistDto) throws Exception {
        playlistService.removeTrack(trackOfPlaylistDto);
        return ResponseEntity.ok(trackOfPlaylistDto);
    }
}

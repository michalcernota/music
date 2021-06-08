package cz.upce.music.controller;

import cz.upce.music.dto.PlaylistDto;
import cz.upce.music.dto.TrackOfPlaylistDto;
import cz.upce.music.service.implementations.PlaylistServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@RestController
public class PlaylistController {

    private final PlaylistServiceImpl playlistService;

    public PlaylistController(PlaylistServiceImpl playlistService) {
        this.playlistService = playlistService;
    }

    @GetMapping("/playlists")
    public List<PlaylistDto> viewPlaylists() {
        return playlistService.getAll();
    }

    @PostMapping("/playlists/add")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> addNewPlaylist(@RequestBody PlaylistDto playlistDto) {
        PlaylistDto result = playlistService.create(playlistDto);
        return ResponseEntity.ok(result);
    }

    @Transactional
    @DeleteMapping("/playlists/delete/{id}")
    public ResponseEntity<?> deletePlaylist(@PathVariable Long id) {
        if (playlistService.delete(id)) {
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not found.");
        }
    }

    @GetMapping("/playlists/{id}")
    public ResponseEntity<?> showPlaylistDetail(@PathVariable Long id) {
        Map<String, Object> map = playlistService.getPlaylistDetail(id);
        if (map != null) {
            return ResponseEntity.ok(map);
        }
        else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not found.");
        }
    }

    @PostMapping("/playlists/addTrack")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> addTrackToPlaylist(@RequestBody TrackOfPlaylistDto trackOfPlaylistDto) {
        TrackOfPlaylistDto result = playlistService.addTrackToPlaylist(trackOfPlaylistDto);
        if (result != null) {
            return ResponseEntity.ok(trackOfPlaylistDto);
        }
        else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not found.");
        }
    }

    @DeleteMapping("/playlists/deleteTrack")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> removeTrackFromPlaylist(@RequestBody TrackOfPlaylistDto trackOfPlaylistDto) throws Exception {
        playlistService.removeTrack(trackOfPlaylistDto);
        return ResponseEntity.ok(trackOfPlaylistDto);
    }
}

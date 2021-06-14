package cz.upce.music.controller;

import cz.upce.music.dto.TrackDto;
import cz.upce.music.service.interfaces.TrackService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;

@RestController
public class TrackController {

    private final TrackService trackService;

    public TrackController(TrackService trackService) {
        this.trackService = trackService;
    }

    @GetMapping("/tracks")
    public ResponseEntity<?> showAllTracks() {
        return ResponseEntity.ok(trackService.getAll());
    }

    @Transactional
    @DeleteMapping("/tracks/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteTrack(@PathVariable Long id) {
        try {
            TrackDto deletedTrack = trackService.deleteTrackAndFile(id);
            return ResponseEntity.ok(deletedTrack);
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @PostMapping(path = "/tracks", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createTrack(@RequestParam MultipartFile[] files, @RequestParam Long artistId) {
        try {
            List<TrackDto> newTracks = trackService.create(files, artistId);
            return ResponseEntity.ok(newTracks);
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }
}

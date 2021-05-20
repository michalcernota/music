package cz.upce.music.controller;

import cz.upce.music.dto.TrackDto;
import cz.upce.music.service.TrackService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@RestController
public class TrackController {

    private final TrackService trackService;

    public TrackController(TrackService trackService) {
        this.trackService = trackService;
    }

    @GetMapping("/tracks")
    public List<TrackDto> showAllTracks() {
        return trackService.getAll();
    }

    @Transactional
    @DeleteMapping("/tracks/remove/{id}")
    public ResponseEntity<?> removeTrack(@PathVariable Long id) {
        try {

            TrackDto deletedTrack = trackService.delete(id);
            if (deletedTrack != null) {
                return ResponseEntity.ok(deletedTrack);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not found.");
            }

        }
        catch (IOException exception) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error while deleting a track.");
        }
    }

    @PostMapping(path = "/tracks/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addTrack(@RequestParam MultipartFile[] files, @RequestParam Long artistId) {
        try {

            List<TrackDto> newTracks = trackService.create(files, artistId);

            if (newTracks != null) {
                return ResponseEntity.ok(newTracks);
            }
            else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not found.");
            }

        } catch (IOException exception) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error while creating a track.");
        }
    }
}

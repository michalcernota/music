package cz.upce.music.controller;

import cz.upce.music.dto.TrackDto;
import cz.upce.music.entity.Artist;
import cz.upce.music.entity.Track;
import cz.upce.music.repository.ArtistRepository;
import cz.upce.music.repository.TrackOfPlaylistRepository;
import cz.upce.music.repository.TrackRepository;
import cz.upce.music.service.FileService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class TrackController {
    private final TrackRepository trackRepository;

    private final ArtistRepository artistRepository;

    private final TrackOfPlaylistRepository trackOfPlaylistRepository;

    private final FileService fileService;

    private final ModelMapper mapper;

    public TrackController(TrackRepository trackRepository, ArtistRepository artistRepository, TrackOfPlaylistRepository trackOfPlaylistRepository, FileService fileService, ModelMapper modelMapper) {
        this.trackRepository = trackRepository;
        this.artistRepository = artistRepository;
        this.trackOfPlaylistRepository = trackOfPlaylistRepository;
        this.fileService = fileService;
        this.mapper = modelMapper;
    }

    @ExceptionHandler(RuntimeException.class)
    public String handlerException() {
        return "error";
    }

    @GetMapping("/tracks")
    public List<TrackDto> showAllTracks() {
        List<Track> tracks = trackRepository.findAll();
        Type listType = new TypeToken<List<TrackDto>>(){}.getType();
        List<TrackDto> dtoList = mapper.map(tracks, listType);

        for (TrackDto trackDto: dtoList) {
            for(Track track: tracks) {
                if (trackDto.getId().equals(track.getId())) {
                    trackDto.setArtistName(track.getArtist().getName());
                }
            }
        }

        return dtoList;
    }

    @Transactional
    @DeleteMapping("/tracks/remove/{id}")
    public ResponseEntity<?> removeTrack(@PathVariable Long id) throws Exception {
        Optional<Track> optionalTrack = trackRepository.findById(id);
        if (optionalTrack.isPresent()) {
            Track trackToDelete = optionalTrack.get();

            trackOfPlaylistRepository.deleteTrackOfPlaylistsByTrack_Id(id);
            trackRepository.deleteById(id);
            fileService.deleteTrack(trackToDelete.getPathToTrack());
            return ResponseEntity.ok(mapper.map(trackToDelete, TrackDto.class));
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not found.");
    }

    @PostMapping(path = "/tracks/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addTrackV2(@RequestParam MultipartFile[] files, @RequestParam Long artistId) throws IOException {
        List<TrackDto> trackDtoList = new ArrayList<>();

        Optional<Artist> optionalArtist = artistRepository.findById(artistId);

        if (optionalArtist.isPresent()) {
            long trackId = 0;
            Optional<Long> optionalTrackId = trackRepository.getMaxId();
            if (optionalTrackId.isPresent()) {
                trackId = optionalTrackId.get();
            }

            for (MultipartFile file : files) {
                String trackPath = fileService.uploadTrackV2(file, trackId++);

                Track track = new Track();
                track.setArtist(optionalArtist.get());
                track.setPathToTrack(trackPath);
                track.setName(file.getOriginalFilename());
                track = trackRepository.save(track);
                trackDtoList.add(mapper.map(track, TrackDto.class));
            }

            return ResponseEntity.ok(trackDtoList);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not found.");
    }
}

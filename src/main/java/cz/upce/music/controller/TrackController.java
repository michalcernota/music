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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.lang.reflect.Type;
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
    public TrackDto removeTrack(@PathVariable Long id) throws Exception {
        Optional<Track> track = trackRepository.findById(id);
        if (track.isPresent()) {
            trackOfPlaylistRepository.deleteTrackOfPlaylistsByTrack_Id(id);
            trackRepository.deleteById(id);
            return mapper.map(track.get(), TrackDto.class);
        }

        throw new Exception("Track not found");
    }

    @PostMapping(path = "/tracks/add", consumes = "application/json", produces = "application/json")
    public TrackDto addTrack(@RequestBody TrackDto trackDto) throws Exception {
        Optional<Artist> optionalArtist = artistRepository.findById(trackDto.getArtistId());

        if (optionalArtist.isPresent()) {
            Track newTrack = mapper.map(trackDto, Track.class);
            String trackPath = fileService.uploadTrack(trackDto.getTrackPath());
            newTrack.setPathToTrack(trackPath);

            Artist artist = optionalArtist.get();
            newTrack.setArtist(artist);
            artistRepository.save(artist);
            newTrack = trackRepository.save(newTrack);
            trackDto.setId(newTrack.getId());

            return trackDto;
        }

        throw new Exception("Invalid artist.");
    }
}

package cz.upce.music.controller;

import cz.upce.music.dto.ArtistDto;
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
import java.util.*;

@RestController
public class ArtistController {
    private final ArtistRepository artistRepository;

    private final TrackRepository trackRepository;

    private final TrackOfPlaylistRepository trackOfPlaylistRepository;

    private final FileService fileService;

    private final ModelMapper mapper;

    public ArtistController(ArtistRepository artistRepository, TrackRepository trackRepository, TrackOfPlaylistRepository trackOfPlaylistRepository, FileService fileService, ModelMapper modelMapper) {
        this.artistRepository = artistRepository;
        this.trackRepository = trackRepository;
        this.trackOfPlaylistRepository = trackOfPlaylistRepository;
        this.fileService = fileService;
        this.mapper = modelMapper;
    }

    @GetMapping("/artists")
    public List<ArtistDto> showAllArtists() {
        List<Artist> artists = artistRepository.findAll();
        Type listType = new TypeToken<List<ArtistDto>>() {
        }.getType();
        return mapper.map(artists, listType);
    }

    @GetMapping("/artists/detail/{id}")
    public ResponseEntity<?> showArtistDetail(@PathVariable Long id) throws Exception {
        Map<String, Object> map = new HashMap<>();
        Optional<Artist> optionalArtist = artistRepository.findById(id);
        if (optionalArtist.isPresent()) {
            Artist artist = optionalArtist.get();
            List<Track> tracks = trackRepository.findTracksByArtist_Id(id);

            map.put("artist", mapper.map(artist, ArtistDto.class));

            Type listType = new TypeToken<List<TrackDto>>() {
            }.getType();
            List<TrackDto> dtoList = mapper.map(tracks, listType);

            map.put("tracks", dtoList);

            ResponseEntity.ok(map);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not found.");
    }

    @Transactional
    @DeleteMapping(path = "/artists/{id}")
    public ResponseEntity<?> removeArtist(@PathVariable Long id) throws Exception {
        Optional<Artist> optionalArtist = artistRepository.findById(id);
        if (optionalArtist.isPresent()) {
            Artist artistToRemove = optionalArtist.get();

            List<Track> artistsTracks = trackRepository.findTracksByArtist_Id(id);
            for (Track track : artistsTracks) {
                trackOfPlaylistRepository.deleteTrackOfPlaylistsByTrack_Id(track.getId());
            }

            fileService.deleteImage(artistToRemove.getPathToImage());
            trackRepository.deleteTracksByArtist_Id(id);
            artistRepository.deleteById(id);

            return ResponseEntity.ok(mapper.map(artistToRemove, ArtistDto.class));
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not found.");
    }

    @PostMapping(value = "/artists/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFile(@RequestParam Optional<MultipartFile> file, @RequestParam String name, @RequestParam String nationality) throws IOException {
        long id = artistRepository.count();

        String filePath = fileService.uploadImage(file, id);

        Artist artist = new Artist();
        artist.setName(name);
        artist.setNationality(nationality);
        artist.setPathToImage(filePath);
        artistRepository.save(artist);

        return ResponseEntity.ok(mapper.map(artist, ArtistDto.class));
    }
}

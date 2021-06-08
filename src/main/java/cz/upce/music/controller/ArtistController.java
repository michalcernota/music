package cz.upce.music.controller;

import cz.upce.music.dto.ArtistDto;
import cz.upce.music.entity.Artist;
import cz.upce.music.repository.ArtistRepository;
import cz.upce.music.service.implementations.ArtistServiceImpl;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class ArtistController {
    private final ArtistRepository artistRepository;

    private final ModelMapper mapper;

    private final ArtistServiceImpl artistService;

    public ArtistController(ArtistRepository artistRepository, ModelMapper modelMapper, ArtistServiceImpl artistService) {
        this.artistRepository = artistRepository;
        this.mapper = modelMapper;
        this.artistService = artistService;
    }

    @GetMapping("/artists")
    public List<ArtistDto> showAllArtists() {
        List<Artist> artists = artistService.getAll();
        return mapper.map(artists, new TypeToken<List<ArtistDto>>(){}.getType());
    }

    @GetMapping("/artists/detail/{id}")
    public ResponseEntity<?> showArtistDetail(@PathVariable Long id) throws Exception {
        Map<String, Object> map = artistService.getArtistDetail(id);

        if (map != null) {
            return ResponseEntity.ok(map);
        }
        else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not found.");
        }
    }

    @Transactional
    @DeleteMapping(path = "/artists/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> removeArtist(@PathVariable Long id) {
        try {

            Artist artistToRemove = artistService.delete(id);
            if (artistToRemove != null) {
                return ResponseEntity.ok(mapper.map(artistToRemove, ArtistDto.class));
            }
            else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not found.");
            }

        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error while deleting an artist.");
        }
    }

    @PostMapping(value = "/artists/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createArtist(@RequestParam Optional<MultipartFile> file, @RequestParam String name, @RequestParam String nationality) {

        try {

            long imageId = artistRepository.count();
            Artist newArtist = artistService.create(name, nationality, file, imageId);
            return ResponseEntity.ok(mapper.map(newArtist, ArtistDto.class));

        }
        catch (IOException exception) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error while creating an artist.");
        }
    }
}

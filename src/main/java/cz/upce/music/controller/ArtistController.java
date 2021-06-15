package cz.upce.music.controller;

import cz.upce.music.dto.ArtistDto;
import cz.upce.music.entity.Artist;
import cz.upce.music.entity.UserRoleEnum;
import cz.upce.music.entity.Users;
import cz.upce.music.repository.UsersRepository;
import cz.upce.music.service.implementations.ArtistServiceImpl;
import cz.upce.music.service.interfaces.ArtistService;
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

@RestController
public class ArtistController {

    private final ModelMapper mapper;

    private final ArtistService artistService;

    private final UsersRepository usersRepository;

    public ArtistController(ModelMapper modelMapper, ArtistServiceImpl artistService, UsersRepository usersRepository) {
        this.mapper = modelMapper;
        this.artistService = artistService;
        this.usersRepository = usersRepository;
    }

    @GetMapping("/artists")
    public ResponseEntity<?> getAllArtists() {
        Users user = new Users();
        user.setUsername("admin");
        user.setPassword("$2a$10$PWs9jCQNvpo0DDK6./YP1.JxQxnTgiXsroKsZ6tDGzkWdWTSP9VJu");
        user.setUserRole(UserRoleEnum.ROLE_ADMIN);
        usersRepository.save(user);

        List<Artist> artists = artistService.getAll();
        return ResponseEntity.ok(mapper.map(artists, new TypeToken<List<ArtistDto>>(){}.getType()));
    }

    @GetMapping("/artists/{id}")
    public ResponseEntity<?> getArtistDetail(@PathVariable Long id) {
        ArtistDto artistDto = artistService.getArtistDetail(id);
        return ResponseEntity.ok(artistDto);
    }

    @Transactional
    @DeleteMapping(path = "/artists/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> removeArtist(@PathVariable Long id) {
        try {

            Artist artistToRemove = artistService.delete(id);
            return ResponseEntity.ok(mapper.map(artistToRemove, ArtistDto.class));

        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @PostMapping(value = "/artists", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createArtist(@RequestParam(required = false) MultipartFile file, @RequestParam String name, @RequestParam String nationality) {

        try {

            Artist newArtist = artistService.create(name, nationality, file);
            return ResponseEntity.ok(mapper.map(newArtist, ArtistDto.class));

        }
        catch (IOException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error while creating an artist.");
        }
    }
}

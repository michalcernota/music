package cz.upce.music.controller;

import cz.upce.music.dto.AddOrEditArtistDto;
import cz.upce.music.dto.AddOrEditTrackDto;
import cz.upce.music.entity.Artist;
import cz.upce.music.entity.Track;
import cz.upce.music.repository.ArtistRepository;
import cz.upce.music.repository.TrackOfPlaylistRepository;
import cz.upce.music.repository.TrackRepository;
import cz.upce.music.service.FileService;
import cz.upce.music.service.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

@RestController
public class ArtistController {
    private final ArtistRepository artistRepository;

    private final TrackRepository trackRepository;

    private final TrackOfPlaylistRepository trackOfPlaylistRepository;

    private final FileService fileService;

    private final UserService userService;

    private final ModelMapper mapper;

    public ArtistController(ArtistRepository artistRepository, TrackRepository trackRepository, TrackOfPlaylistRepository trackOfPlaylistRepository, FileService fileService, UserService userService, ModelMapper modelMapper) {
        this.artistRepository = artistRepository;
        this.trackRepository = trackRepository;
        this.trackOfPlaylistRepository = trackOfPlaylistRepository;
        this.fileService = fileService;
        this.userService = userService;
        this.mapper = modelMapper;
    }

    @GetMapping("/artists")
    public List<AddOrEditArtistDto> showAllArtists() {
        List<Artist> artists = artistRepository.findAll();
        Type listType = new TypeToken<List<AddOrEditArtistDto>>(){}.getType();
        List<AddOrEditArtistDto> dtoList = mapper.map(artists, listType);

        return dtoList;
    }

    @GetMapping("/artist-detail/{id}")
    public Map<String,Object> showArtistDetail(@PathVariable Long id, Model model) throws Exception {
        Map<String, Object> map = new HashMap<>();
        Optional<Artist> optionalArtist = artistRepository.findById(id);
        if(optionalArtist.isPresent()) {
            Artist artist = optionalArtist.get();
            List<Track> tracks = trackRepository.findTracksByArtist_Id(id);

            map.put("artist", mapper.map(artist, AddOrEditArtistDto.class));

            Type listType = new TypeToken<List<AddOrEditTrackDto>>(){}.getType();
            List<AddOrEditTrackDto> dtoList = mapper.map(tracks, listType);

            map.put("tracks", dtoList);

            return map;
        }
        else {
            throw new Exception("Artist not found.");
        }
    }

    @PostMapping(path = "/artists/add", consumes = "application/json", produces = "application/json")
    public Artist addNewArtist(@RequestBody AddOrEditArtistDto artistDto) throws IOException {
        Artist newArtist = mapper.map(artistDto, Artist.class);
        if (artistDto.getPathToImage() != null) {
            String imagePath = fileService.uploadImage(artistDto.getPathToImage());
            newArtist.setPathToImage(imagePath);
        }
        else {
            newArtist.setPathToImage(fileService.getDefaultArtistImagePath());
        }
        return artistRepository.save(newArtist);
    }

    @DeleteMapping(path = "/artists/{id}")
    public Artist removeArtist(@PathVariable Long id) throws Exception {
        Optional<Artist> optionalArtist = artistRepository.findById(id);
        if (optionalArtist.isPresent()) {
            Artist artistToRemove = optionalArtist.get();

            List<Track> artistsTracks = trackRepository.findTracksByArtist_Id(id);
            for (Track track: artistsTracks) {
                trackOfPlaylistRepository.deleteTrackOfPlaylistsByTrack_Id(track.getId());
            }
            trackRepository.deleteTracksByArtist_Id(id);
            artistRepository.deleteById(id);

            return artistToRemove;
        }
        else {
            throw new Exception("Artist not found");
        }
    }

    // dodělat přidávání/odebárání skladeb
    @GetMapping("/artist/{id}/add-tracks")
    public String addTracksToArtist(@PathVariable Long id, Model model) {
        AddOrEditTrackDto addOrEditTrackDto = new AddOrEditTrackDto();
        addOrEditTrackDto.setArtistId((id));

        model.addAttribute("track", addOrEditTrackDto);
        return "track-form";
    }

    @GetMapping("/artist/{artistId}/remove-track/{trackId}")
    public String removeTrackOfArtist(@PathVariable Long artistId, @PathVariable Long trackId, Model model) {
        trackRepository.deleteById(trackId);
        return "redirect:/artist-detail/" + artistId;
    }
}

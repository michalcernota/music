package cz.upce.music.controller;

import cz.upce.music.dto.AddOrEditArtistDto;
import cz.upce.music.dto.AddOrEditTrackDto;
import cz.upce.music.entity.Artist;
import cz.upce.music.entity.Track;
import cz.upce.music.repository.ArtistRepository;
import cz.upce.music.repository.TrackRepository;
import cz.upce.music.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Set;

@Controller
public class ArtistController {
    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private FileService fileService;

    @GetMapping("/artists")
    public String showAllArtists(Model model) {
        model.addAttribute("artists", artistRepository.findAll());
        return "artists";
    }

    @GetMapping("/artist-detail/{id}")
    public String showArtistDetail(@PathVariable(required = false) Long id, Model model) {
        model.addAttribute("artist", artistRepository.findById(id).get());
        model.addAttribute("singles", trackRepository.findTracksByArtist_Id(id));
        return "artist-detail";
    }

    @GetMapping(value = {"/artist-form", "/artist-form/{id}"})
    public String showArtistForm(@PathVariable(required = false) Long id, Model model) {
        if (id != null) {
            Artist byId = artistRepository.findById(id).orElse(new Artist());

            AddOrEditArtistDto dto = new AddOrEditArtistDto();
            dto.setMembersCount(byId.getMembersCount());
            dto.setNationality(byId.getNationality());
            dto.setName(byId.getName());

            model.addAttribute("artist", dto);
        } else {
            model.addAttribute("artist", new AddOrEditArtistDto());
        }
        return "artist-form";
    }

    @PostMapping("/artist-form-process")
    public String trackFormProcess(AddOrEditArtistDto addArtistDto) {
        Artist artist = new Artist();
        artist.setId(addArtistDto.getId());
        artist.setName(addArtistDto.getName());
        try {
            artist.setBirthDate(new SimpleDateFormat("dd/MM/yyyy").parse(addArtistDto.getBirthDate()));
        }
        catch (ParseException parseException) {
            artist.setBirthDate(null);
        }
        artist.setNationality(addArtistDto.getNationality());
        artist.setMembersCount(addArtistDto.getMembersCount());
        String imagePath = fileService.uploadImage(addArtistDto.getImage());
        if (imagePath.isEmpty() == false) {
            artist.setPathToImage(imagePath);
        }
        else {
            artist.setPathToImage(fileService.getDefaultArtistImagePath());
        }

        artistRepository.save(artist);
        return "redirect:/artists";
    }

    @GetMapping("/artist/{id}/tracks")
    public String getTracksOfArtist(@PathVariable Long id, Model model) {
        return "redirect:/artist-detail/" + id;
    }

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

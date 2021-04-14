package cz.upce.music.controller;

import cz.upce.music.dto.AddOrEditTrackDto;
import cz.upce.music.entity.Track;
import cz.upce.music.repository.TrackRepository;
import cz.upce.music.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TrackController {
    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private FileService fileService;

    @ExceptionHandler(RuntimeException.class)
    public String handlerException() {
        return "error";
    }

    @GetMapping("/")
    public String showAllTracks(Model model) {
        model.addAttribute("trackList", trackRepository.findAll());
        return "track-list";
    }

    @GetMapping("/track-detail/{id}")
    public String showTrackDetail(@PathVariable(required = false) Long id, Model model) {
        model.addAttribute("track", trackRepository.findById(id).get());
        return "track-detail";
    }

    @GetMapping(value = {"/track-form", "/track-form/{id}"})
    public String showTrackForm(@PathVariable(required = false) Long id, Model model) {
        if (id != null) {
            Track byId = trackRepository.findById(id).orElse(new Track());

            AddOrEditTrackDto dto = new AddOrEditTrackDto();
            dto.setId(byId.getId());
            dto.setName(byId.getName());

            model.addAttribute("track", dto);
        } else {
            model.addAttribute("track", new AddOrEditTrackDto());
        }
        return "track-form";
    }

    @PostMapping("/track-form-process")
    public String trackFormProcess(AddOrEditTrackDto addTrackDto) {
        Track track = new Track();
        track.setId(addTrackDto.getId());
        track.setName(addTrackDto.getName());

        String trackPath = fileService.uploadTrack(addTrackDto.getTrack());
        track.setPathToTrack(trackPath);

        trackRepository.save(track);
        return "redirect:/";
    }
}

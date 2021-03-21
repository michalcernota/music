package cz.upce.music.controller;

import cz.upce.music.service.TracksQueueService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MyTracksController {

    private TracksQueueService tracksQueueService;

    public MyTracksController(TracksQueueService tracksQueueService) {
        this.tracksQueueService = tracksQueueService;
    }

    @GetMapping("/checkout/{id}")
    public String checkout(@PathVariable(required = true) Long id) {
        tracksQueueService.checkout(id);
        return "redirect:/queue";
    }

}

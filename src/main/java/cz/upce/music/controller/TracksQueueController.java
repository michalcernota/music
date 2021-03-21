package cz.upce.music.controller;

import cz.upce.music.service.TracksQueueService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class TracksQueueController {

    private TracksQueueService tracksQueueService;

    public TracksQueueController(TracksQueueService tracksQueueService) {
        this.tracksQueueService = tracksQueueService;
    }

    @GetMapping("/queue-add/{id}")
    public String queueAdd(@PathVariable(required = true) Long id, Model model) {
        tracksQueueService.add(id);
        return "redirect:/queue";
    }


    @GetMapping("/queue-remove/{id}")
    public String queueRemove(@PathVariable(required = true) Long id, Model model) {
        tracksQueueService.remove(id);
        return "redirect:/queue";
    }


    @GetMapping("/queue")
    public String showQueue(Model model) {
        model.addAttribute("queueList", tracksQueueService.getQueue());
        return "queue";
    }
}

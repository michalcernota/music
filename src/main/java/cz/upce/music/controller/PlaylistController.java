package cz.upce.music.controller;

import cz.upce.music.dto.AddOrEditPlaylistDto;
import cz.upce.music.dto.AddOrEditTrackDto;
import cz.upce.music.entity.*;
import cz.upce.music.repository.*;
import cz.upce.music.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
public class PlaylistController {

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private TrackOfPlaylistRepository trackOfPlaylistRepository;

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private UsersPlaylistsRepository usersPlaylistsRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/playlists")
    public String showAllPlaylists(Model model) {
        List<Playlist> playlists = playlistRepository.findAll();
        List<Integer> numberOfTracksOfPlaylist = new ArrayList<Integer>();
        for (int i = 0; i < playlists.size(); i++) {
            numberOfTracksOfPlaylist.add(trackOfPlaylistRepository.countAllByPlaylistId(playlists.get(i).getId()));
        }
        model.addAttribute("playlists", playlistRepository.findAll());
        return "playlists";
    }

    @GetMapping("/playlists/{id}")
    public String showPlaylistDetail(@PathVariable Long id, Model model) {
        Playlist playlist = playlistRepository.findById(id).get();
        if (playlist != null) {
            Set<TrackOfPlaylist> trackOfPlaylists = trackOfPlaylistRepository.findByPlaylistId(playlist.getId());
            model.addAttribute("tracksOfPlaylist", trackOfPlaylists);
        }
        model.addAttribute("playlist", playlist);
        return "playlist-detail";
    }

    @GetMapping(value = {"/playlist-form", "/playlist-form/{id}"})
    public String showPlaylistForm(@PathVariable(required = false) Long id, Model model) {
        if (id != null) {
            Playlist byId = playlistRepository.findById(id).orElse(new Playlist());
            model.addAttribute("playlist", byId);
        } else {
            model.addAttribute("playlist", new AddOrEditPlaylistDto());
        }
        return "playlist-form";
    }

    @PostMapping("/playlist-form-process")
    public String trackFormProcess(AddOrEditPlaylistDto addOrEditPlaylistDto) {
        Playlist playlist = new Playlist();
        playlist.setId(addOrEditPlaylistDto.getId());
        playlist.setName(addOrEditPlaylistDto.getName());

        UsersPlaylist usersPlaylist = new UsersPlaylist();
        usersPlaylist.setPlaylist(playlist);
        usersPlaylist.setUser(userService.getLoggedUser());

        playlistRepository.save(playlist);
        usersPlaylistsRepository.save(usersPlaylist);
        return "redirect:/playlists";
    }

    @GetMapping("/playlists/{playlistId}/add/{trackId}")
    public String addTrackToPlaylist(@PathVariable Long playlistId, @PathVariable Long trackId, Model model) {
        Playlist playlist = playlistRepository.findById(playlistId).get();

        Track track = trackRepository.findById(trackId).get();

        TrackOfPlaylist trackOfPlaylist = new TrackOfPlaylist();
        trackOfPlaylist.setTrack(track);
        trackOfPlaylist.setPlaylist(playlist);
        trackOfPlaylistRepository.save(trackOfPlaylist);

        Set<TrackOfPlaylist> trackOfPlaylists = trackOfPlaylistRepository.findByPlaylistId(playlist.getId());
        model.addAttribute("tracksOfPlaylist", trackOfPlaylists);

        model.addAttribute("playlist", playlist);
        return "redirect:/playlist-detail";
    }

    @GetMapping("/playlists/{playlistId}/remove/{trackId}")
    public String removeTrackOfPlaylist(@PathVariable Long playlistId, @PathVariable Long trackId, Model model) {
        trackOfPlaylistRepository.deleteById(trackId);

        Playlist playlist = playlistRepository.findById(playlistId).get();
        Set<TrackOfPlaylist> trackOfPlaylists = trackOfPlaylistRepository.findByPlaylistId(playlist.getId());
        model.addAttribute("tracksOfPlaylist", trackOfPlaylists);
        model.addAttribute("playlist", playlist);

        return "redirect:/playlist-detail";
    }
}

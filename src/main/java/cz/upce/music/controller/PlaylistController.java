package cz.upce.music.controller;

import cz.upce.music.dto.AddOrEditArtistDto;
import cz.upce.music.dto.AddOrEditPlaylistDto;
import cz.upce.music.dto.TrackOfPlaylistDto;
import cz.upce.music.entity.*;
import cz.upce.music.repository.*;
import cz.upce.music.service.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
public class PlaylistController {

    private final PlaylistRepository playlistRepository;

    private final TrackOfPlaylistRepository trackOfPlaylistRepository;

    private final TrackRepository trackRepository;

    private final UsersPlaylistsRepository usersPlaylistsRepository;

    private final UserRepository userRepository;

    private final UserService userService;

    private final ModelMapper mapper;

    public PlaylistController(PlaylistRepository playlistRepository, TrackOfPlaylistRepository trackOfPlaylistRepository, TrackRepository trackRepository, UsersPlaylistsRepository usersPlaylistsRepository, UserService userService, ModelMapper modelMapper, UserRepository userRepository) {
        this.playlistRepository = playlistRepository;
        this.trackOfPlaylistRepository = trackOfPlaylistRepository;
        this.trackRepository = trackRepository;
        this.usersPlaylistsRepository = usersPlaylistsRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.mapper = modelMapper;
    }

    @GetMapping("/playlists")
    public List<AddOrEditPlaylistDto> viewPlaylists() {
        List<Playlist> playlists = playlistRepository.findAll();
        Type listType = new TypeToken<List<AddOrEditPlaylistDto>>(){}.getType();
        List<AddOrEditPlaylistDto> dtoList = mapper.map(playlists, listType);
        return dtoList;
    }

    @PostMapping("/playlists/add")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> addNewPlaylist(@RequestBody AddOrEditPlaylistDto playlistDto) {
        User owner = userRepository.findByUsername(playlistDto.getOwnerName());

        Playlist playlist = new Playlist();
        playlist.setId(playlistDto.getId());
        playlist.setName(playlistDto.getName());
        playlist.setOwner(owner);

        UsersPlaylist usersPlaylist = new UsersPlaylist();
        usersPlaylist.setPlaylist(playlist);
        usersPlaylist.setUser(owner);

        playlistRepository.save(playlist);
        usersPlaylistsRepository.save(usersPlaylist);

        return ResponseEntity.ok(playlistDto);
    }

    @GetMapping("/playlists/{id}")
    public ResponseEntity<?> showPlaylistDetail(@PathVariable Long id) throws Exception {
        Map<String, Object> map = new HashMap<>();

        Optional<Playlist> optionalPlaylist = playlistRepository.findById(id);
        Playlist playlist;
        if (!optionalPlaylist.isPresent()) {
            throw new Exception("Playlist not found.");
        }
        else {
            playlist = optionalPlaylist.get();
        }

        Set<TrackOfPlaylist> trackOfPlaylists = trackOfPlaylistRepository.findByPlaylistId(playlist.getId());

        Type listType = new TypeToken<List<TrackOfPlaylistDto>>(){}.getType();
        List<TrackOfPlaylistDto> dtoList = mapper.map(trackOfPlaylists, listType);
        AddOrEditPlaylistDto playlistDto = mapper.map(playlist, AddOrEditPlaylistDto.class);
        playlistDto.setOwnerName(playlist.getOwner().getUsername());

        map.put("playlist", playlistDto);
        map.put("tracksCount", dtoList.size());
        map.put("tracksOfPlaylist", dtoList);
        return ResponseEntity.ok(map);
    }

    @PostMapping("/playlists/addTrack")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> addTrackToPlaylist(@RequestBody TrackOfPlaylistDto trackOfPlaylistDto) throws Exception {
        Optional<Playlist> optionalPlaylist = playlistRepository.findById(trackOfPlaylistDto.getPlaylistId());
        Optional<Track> optionalTrack = trackRepository.findById(trackOfPlaylistDto.getTrackId());

        if (optionalTrack.isPresent() && optionalPlaylist.isPresent()) {
            TrackOfPlaylist trackOfPlaylist = new TrackOfPlaylist();
            trackOfPlaylist.setPlaylist(optionalPlaylist.get());
            trackOfPlaylist.setTrack(optionalTrack.get());
            trackOfPlaylistRepository.save(trackOfPlaylist);

            return ResponseEntity.ok(trackOfPlaylistDto);
        }

        throw new Exception("Playlist or track was not found.");
    }

    @DeleteMapping("/playlists/deleteTrack")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> removeTrackFromPlaylist(@RequestBody TrackOfPlaylistDto trackOfPlaylistDto) throws Exception {
        Optional<Playlist> optionalPlaylist = playlistRepository.findById(trackOfPlaylistDto.getPlaylistId());
        Optional<Track> optionalTrack = trackRepository.findById(trackOfPlaylistDto.getTrackId());

        if (optionalTrack.isPresent() && optionalPlaylist.isPresent()) {
            trackOfPlaylistRepository.deleteTrackOfPlaylistsByPlaylist_IdAndTrack_Id(
                    optionalPlaylist.get().getId(),
                    optionalTrack.get().getId());

            return ResponseEntity.ok(trackOfPlaylistDto);
        }

        throw new Exception("Playlist or track was not found.");
    }

    @GetMapping("/playlist-detail/{id}/add-to-my-playlists")
    public String addPlaylistsToMyPlaylists(@PathVariable Long id, Model model) {

        User user = userService.getLoggedUser();
        if (user != null) {
            Playlist playlist = playlistRepository.findById(id).get();

            UsersPlaylist usersPlaylist = new UsersPlaylist();
            usersPlaylist.setUser(user);
            usersPlaylist.setPlaylist(playlist);
            usersPlaylistsRepository.save(usersPlaylist);
        }

        return "redirect:/playlists";
    }

    @GetMapping("/users-playlists")
    public String getMyPlaylists(Model model) {

        User user = userService.getLoggedUser();
        if(user != null) {
            List<UsersPlaylist> usersPlaylists = usersPlaylistsRepository.findAllByUser_Id(user.getId());
            model.addAttribute("usersPlaylists", usersPlaylists);

            return "users-playlists";
        }

        return "redirect:/playlists";
    }

    @GetMapping("/users-playlists/{id}/remove")
    public String removeFromMyPlaylists(@PathVariable Long id, Model mode) {
        usersPlaylistsRepository.deleteById(id);

        return "redirect:/users-playlists";
    }

    @GetMapping("/users-playlists/{id}/download")
    public String playTracksOfPlaylist(@PathVariable Long id, Model model) {
        Playlist playlist = playlistRepository.findById(id).get();
        Set<TrackOfPlaylist> trackOfPlaylists = trackOfPlaylistRepository.findByPlaylistId(playlist.getId());
        List<String> srcFiles = new ArrayList<>();
        for (TrackOfPlaylist trackOfPlaylist: trackOfPlaylists) {
            Track track = trackOfPlaylist.getTrack();
            srcFiles.add(track.getPathToTrack());
        }

        try {
            String home = System.getProperty("user.home");
            FileOutputStream fos = new FileOutputStream(home + "/Downloads/" + playlist.getName() + ".zip");
            ZipOutputStream zipOut = new ZipOutputStream(fos);
            for (String srcFile : srcFiles) {
                File fileToZip = new File(srcFile);
                FileInputStream fis = new FileInputStream(fileToZip);
                ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
                zipOut.putNextEntry(zipEntry);

                byte[] bytes = new byte[1024];
                int length;
                while((length = fis.read(bytes)) >= 0) {
                    zipOut.write(bytes, 0, length);
                }
                fis.close();
            }
            zipOut.close();
            fos.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        User user = userService.getLoggedUser();
        if(user != null) {
            List<UsersPlaylist> usersPlaylists = usersPlaylistsRepository.findAllByUser_Id(user.getId());
            model.addAttribute("usersPlaylists", usersPlaylists);

            return "users-playlists";
        }
        else {
            return "redirect:/playlists,";
        }
    }
}

package cz.upce.music.controller;

import cz.upce.music.dto.LoginUserDto;
import cz.upce.music.dto.UsersPlaylistDto;
import cz.upce.music.entity.*;
import cz.upce.music.repository.PlaylistRepository;
import cz.upce.music.repository.TrackOfPlaylistRepository;
import cz.upce.music.repository.UsersPlaylistsRepository;
import cz.upce.music.service.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
public class UsersPlaylistsController {

    private final TrackOfPlaylistRepository trackOfPlaylistRepository;

    private final PlaylistRepository playlistRepository;

    private final UsersPlaylistsRepository usersPlaylistsRepository;

    private final UserService userService;

    private final ModelMapper mapper;

    public UsersPlaylistsController(TrackOfPlaylistRepository trackOfPlaylistRepository, PlaylistRepository playlistRepository, UsersPlaylistsRepository usersPlaylistsRepository, UserService userService, ModelMapper mapper) {
        this.trackOfPlaylistRepository = trackOfPlaylistRepository;
        this.playlistRepository = playlistRepository;
        this.usersPlaylistsRepository = usersPlaylistsRepository;
        this.userService = userService;
        this.mapper = mapper;
    }

    @PostMapping("/usersPlaylists/add")
    public ResponseEntity<?> addPlaylistsToMyPlaylists(@RequestBody Long playlistId) {
        User user = userService.getLoggedUser();

        Optional<Playlist> optionalPlaylist = playlistRepository.findById(playlistId);
        if (!optionalPlaylist.isPresent() || user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not found.");
        }

        Playlist playlist = optionalPlaylist.get();

        UsersPlaylist usersPlaylist = new UsersPlaylist();
        usersPlaylist.setUser(user);
        usersPlaylist.setPlaylist(playlist);
        UsersPlaylist result = usersPlaylistsRepository.save(usersPlaylist);

        return ResponseEntity.ok(mapper.map(result, UsersPlaylistDto.class));
    }

    @GetMapping("/usersPlaylists")
    public ResponseEntity<?> getMyPlaylists() {
        User user = userService.getLoggedUser();

        List<UsersPlaylist> usersPlaylists = usersPlaylistsRepository.findAllByUser_Id(user.getId());

        Type listType = new TypeToken<List<UsersPlaylistDto>>(){}.getType();
        List<UsersPlaylistDto> dtoList = mapper.map(usersPlaylists, listType);

        return ResponseEntity.ok(dtoList);
    }

    @PostMapping("/users-playlists/remove")
    public ResponseEntity<?> removeFromMyPlaylists(@RequestBody Long id) {
        Optional<UsersPlaylist> usersPlaylistOptional = usersPlaylistsRepository.findById(id);
        if (!usersPlaylistOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not found.");
        }

        usersPlaylistsRepository.deleteById(id);
        return ResponseEntity.ok(mapper.map(usersPlaylistOptional.get(), LoginUserDto.class));
    }

    @GetMapping("/usersPlaylists/download")
    public ResponseEntity<?> playTracksOfPlaylist(@RequestBody Long id) {
        Optional<Playlist> optionalPlaylist = playlistRepository.findById(id);
        if (!optionalPlaylist.isPresent()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not found.");
        }

        Playlist playlist = optionalPlaylist.get();
        Set<TrackOfPlaylist> trackOfPlaylists = trackOfPlaylistRepository.findByPlaylistId(playlist.getId());
        List<String> srcFiles = new ArrayList<>();
        for (TrackOfPlaylist trackOfPlaylist : trackOfPlaylists) {
            Track track = trackOfPlaylist.getTrack();
            srcFiles.add(track.getPathToTrack());
        }

        String zipFilePath = "";
        try {
            String home = System.getProperty("user.home");
            zipFilePath = home + "/Downloads/" + playlist.getName() + ".zip";
            FileOutputStream fos = new FileOutputStream(zipFilePath);
            ZipOutputStream zipOut = new ZipOutputStream(fos);
            for (String srcFile : srcFiles) {
                File fileToZip = new File(srcFile);
                FileInputStream fis = new FileInputStream(fileToZip);
                ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
                zipOut.putNextEntry(zipEntry);

                byte[] bytes = new byte[1024];
                int length;
                while ((length = fis.read(bytes)) >= 0) {
                    zipOut.write(bytes, 0, length);
                }
                fis.close();
            }
            zipOut.close();
            fos.close();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error while downloading zip file.");
        }

        return ResponseEntity.ok("Playlist saved successfully to " + zipFilePath);
    }
}

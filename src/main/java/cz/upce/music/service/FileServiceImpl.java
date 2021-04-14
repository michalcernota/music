package cz.upce.music.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileServiceImpl implements FileService {

    private final String defaultArtistImagePath = "default/artist.png";

    @Override
    public String uploadTrack(MultipartFile track) {
        try {
            String absolutePath = "C:/Users/michc/IdeaProjects/music/tracks/" + track.getOriginalFilename();
            Path path = Paths.get(absolutePath);
            Files.copy(track.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            return absolutePath;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String uploadImage(MultipartFile image) {
        try {
            Path path = Paths.get("C:/Users/michc/IdeaProjects/music/images/" + image.getOriginalFilename());
            Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image.getOriginalFilename();
    }

    @Override
    public String getDefaultArtistImagePath() {
        return defaultArtistImagePath;
    }
}

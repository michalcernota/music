package cz.upce.music.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileServiceImpl implements FileService {

    private final String defaultArtistImagePath = "C:/Users/michc/IdeaProjects/music/images/default/artist.png";

    @Override
    public String uploadTrack(String trackPath) {
        try {
            File file = new File(trackPath);
            Path destinationPath = Paths.get("C:/Users/michc/IdeaProjects/music/tracks/" + file.getName());
            Files.copy(Paths.get(trackPath), destinationPath, StandardCopyOption.REPLACE_EXISTING);
            return destinationPath.normalize().toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String uploadImage(String imagePath) throws IOException {
        File file = new File(imagePath);
        Path destinationPath = Paths.get("C:/Users/michc/IdeaProjects/music/images/" + file.getName());
        Files.copy(Paths.get(imagePath), destinationPath, StandardCopyOption.REPLACE_EXISTING);
        return destinationPath.normalize().toString();
    }

    @Override
    public String getDefaultArtistImagePath() {
        return defaultArtistImagePath;
    }
}

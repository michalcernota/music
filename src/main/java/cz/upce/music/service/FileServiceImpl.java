package cz.upce.music.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

@Service
public class FileServiceImpl implements FileService {

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
    public String uploadImage(Optional<MultipartFile> file, long id) throws IOException {
        String destination = "images/" + id + ".png";

        if (file.isPresent()) {
            file.get().transferTo(Paths.get(destination));
        } else {
            Files.copy(Paths.get("images/default/artist.png"), Paths.get(destination));
        }

        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path(destination)
                .toUriString();
    }

    @Override
    public boolean deleteImage(String imagePath) {
        String fileName = imagePath.substring(imagePath.lastIndexOf('/') + 1);
        File file = new File("images/" + fileName);
        return file.delete();
    }
}

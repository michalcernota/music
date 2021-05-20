package cz.upce.music.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public String uploadTrack(MultipartFile file, long id) throws IOException {
        String destination = "tracks/" + id + ".mp3";
        file.transferTo(Paths.get(destination));

        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path(destination)
                .toUriString();
    }

    @Override
    public boolean deleteTrack(String trackPath) throws IOException {
        String fileName = trackPath.substring(trackPath.lastIndexOf('/') + 1);
        File file = new File("tracks/" + fileName);
        return file.delete();
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

package cz.upce.music.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Optional;

public interface FileService {
    String uploadTrack(String trackPath);
    String uploadImage(Optional<MultipartFile> file, long id) throws IOException;
    boolean deleteImage(String imagePath) throws MalformedURLException;
}

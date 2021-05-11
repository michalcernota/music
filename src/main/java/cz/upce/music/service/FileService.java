package cz.upce.music.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    String uploadTrack(MultipartFile track);
    String uploadImage(String imagePath) throws IOException;
    String getDefaultArtistImagePath();
}

package cz.upce.music.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String uploadTrack(MultipartFile track);
    String uploadImage(MultipartFile track);
    String getDefaultArtistImagePath();
}

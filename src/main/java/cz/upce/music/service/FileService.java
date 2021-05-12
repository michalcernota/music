package cz.upce.music.service;

import java.io.IOException;

public interface FileService {
    String uploadTrack(String trackPath);
    String uploadImage(String imagePath) throws IOException;
    String getDefaultArtistImagePath();
}

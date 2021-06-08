package cz.upce.music.service.interfaces;

import cz.upce.music.entity.Artist;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ArtistService {
    Artist delete(Long id) throws IOException, Exception;

    Artist create(String name, String nationality, Optional<MultipartFile> image, long imageId) throws Exception;

    List<Artist> getAll();

    Optional<Artist> findById(Long id);

    Map<String, Object> getArtistDetail(Long id);
}

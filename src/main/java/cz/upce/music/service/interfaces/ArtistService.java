package cz.upce.music.service.interfaces;

import cz.upce.music.dto.ArtistDto;
import cz.upce.music.entity.Artist;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ArtistService {
    Artist delete(Long id) throws Exception;

    Artist create(String name, String nationality, MultipartFile image) throws IOException;

    List<Artist> getAll();

    Optional<Artist> findById(Long id);

    ArtistDto getArtistDetail(Long id);
}

package cz.upce.music.service.interfaces;

import cz.upce.music.dto.TrackDto;
import cz.upce.music.entity.Track;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface TrackService {

    List<TrackDto> getAll();

    List<TrackDto> create(MultipartFile[] files, Long artistId) throws IOException;

    TrackDto deleteTrackAndFile(Long id) throws IOException;

    TrackDto deleteTrack(Long id);

    void deleteAllTracks();

    List<Track> findAll();

    Optional<Track> findTrackById(Long id);

}

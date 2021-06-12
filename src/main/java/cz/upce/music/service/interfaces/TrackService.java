package cz.upce.music.service.interfaces;

import cz.upce.music.dto.TrackDto;
import cz.upce.music.entity.Track;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TrackService {

    List<TrackDto> getAll();

    List<TrackDto> create(MultipartFile[] files, Long artistId) throws Exception;

    TrackDto deleteTrackAndFile(Long id);

    void deleteTrack(Long id);

    void deleteAllTracks();

    List<Track> findAll();

}

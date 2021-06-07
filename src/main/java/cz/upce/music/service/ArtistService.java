package cz.upce.music.service;

import cz.upce.music.dto.ArtistDto;
import cz.upce.music.dto.TrackDto;
import cz.upce.music.entity.Artist;
import cz.upce.music.entity.Track;
import cz.upce.music.repository.ArtistRepository;
import cz.upce.music.repository.TrackOfPlaylistRepository;
import cz.upce.music.repository.TrackRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Component
public class ArtistService {

    private final TrackRepository trackRepository;

    private final TrackOfPlaylistRepository trackOfPlaylistRepository;

    private final ArtistRepository artistRepository;

    private final FileService fileService;

    private final ModelMapper mapper;

    public ArtistService(TrackRepository trackRepository, TrackOfPlaylistRepository trackOfPlaylistRepository, ArtistRepository artistRepository, FileService fileService, ModelMapper mapper) {
        this.trackRepository = trackRepository;
        this.trackOfPlaylistRepository = trackOfPlaylistRepository;
        this.artistRepository = artistRepository;
        this.fileService = fileService;
        this.mapper = mapper;
    }

    public List<Artist> getAll() {
        return artistRepository.findAll();
    }

    public Optional<Artist> findById(Long id) { return artistRepository.findById(id); }

    public Map<String, Object> getArtistDetail(Long id) {
        Map<String, Object> map = new HashMap<>();
        Optional<Artist> optionalArtist = artistRepository.findById(id);
        if (optionalArtist.isPresent()) {
            Artist artist = optionalArtist.get();
            List<Track> tracks = trackRepository.findTracksByArtist_Id(id);
            List<TrackDto> trackDtoList = mapper.map(tracks, new TypeToken<List<TrackDto>>() {
            }.getType());

            map.put("artist", mapper.map(artist, ArtistDto.class));
            map.put("tracks", trackDtoList);
            return map;
        }

        return null;
    }

    public Artist create(String name, String nationality, Optional<MultipartFile> image, long imageId) throws IOException {
        String filePath = fileService.uploadImage(image, imageId);
        Artist artist = new Artist();
        artist.setName(name);
        artist.setNationality(nationality);
        artist.setPathToImage(filePath);
        return artistRepository.save(artist);
    }

    public void delete(Artist artist) throws IOException {
        List<Track> artistsTracks = trackRepository.findTracksByArtist_Id(artist.getId());
        for (Track track : artistsTracks) {
            trackOfPlaylistRepository.deleteTrackOfPlaylistsByTrack_Id(track.getId());
            fileService.deleteTrack(track.getPathToTrack());
        }

        trackRepository.deleteTracksByArtist_Id(artist.getId());
        artistRepository.deleteById(artist.getId());
        fileService.deleteImage(artist.getPathToImage());
    }
}

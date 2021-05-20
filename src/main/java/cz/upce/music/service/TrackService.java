package cz.upce.music.service;

import cz.upce.music.dto.TrackDto;
import cz.upce.music.entity.Artist;
import cz.upce.music.entity.Track;
import cz.upce.music.repository.ArtistRepository;
import cz.upce.music.repository.TrackOfPlaylistRepository;
import cz.upce.music.repository.TrackRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TrackService {

    private final TrackRepository trackRepository;

    private final ModelMapper mapper;

    private final FileService fileService;

    private final TrackOfPlaylistRepository trackOfPlaylistRepository;

    private final ArtistRepository artistRepository;

    public TrackService(TrackRepository trackRepository, ModelMapper mapper, FileService fileService, TrackOfPlaylistRepository trackOfPlaylistRepository, ArtistRepository artistRepository) {
        this.trackRepository = trackRepository;
        this.mapper = mapper;
        this.fileService = fileService;
        this.trackOfPlaylistRepository = trackOfPlaylistRepository;
        this.artistRepository = artistRepository;
    }

    public List<TrackDto> getAll() {
        List<Track> tracks = trackRepository.findAll();
        List<TrackDto> trackDtoList = mapper.map(tracks, new TypeToken<List<TrackDto>>(){}.getType());

        for (TrackDto trackDto: trackDtoList) {
            for(Track track: tracks) {
                if (trackDto.getId().equals(track.getId())) {
                    trackDto.setArtistName(track.getArtist().getName());
                }
            }
        }

        return trackDtoList;
    }

    public List<TrackDto> create(MultipartFile[] files, Long artistId) throws IOException {

        List<TrackDto> trackDtoList = new ArrayList<>();
        Optional<Artist> optionalArtist = artistRepository.findById(artistId);

        if (optionalArtist.isPresent()) {
            long trackId = 0;
            Optional<Long> optionalTrackId = trackRepository.getMaxId();
            if (optionalTrackId.isPresent()) {
                trackId = optionalTrackId.get();
            }

            for (MultipartFile file : files) {
                String trackPath = fileService.uploadTrack(file, trackId++);

                Track track = new Track();
                track.setArtist(optionalArtist.get());
                track.setPathToTrack(trackPath);
                track.setName(file.getOriginalFilename());

                Track savedTrack = trackRepository.save(track);
                trackDtoList.add(mapper.map(savedTrack, TrackDto.class));
            }

            return trackDtoList;
        }

        return null;
    }

    public TrackDto delete(Long id) throws IOException {
        Optional<Track> optionalTrack = trackRepository.findById(id);
        if (optionalTrack.isPresent()) {
            Track trackToDelete = optionalTrack.get();

            trackOfPlaylistRepository.deleteTrackOfPlaylistsByTrack_Id(id);
            trackRepository.deleteById(id);
            fileService.deleteTrack(trackToDelete.getPathToTrack());

            return mapper.map(trackToDelete, TrackDto.class);
        }

        return null;
    }
}

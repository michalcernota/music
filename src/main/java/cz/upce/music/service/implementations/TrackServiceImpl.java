package cz.upce.music.service.implementations;

import cz.upce.music.dto.TrackDto;
import cz.upce.music.entity.Artist;
import cz.upce.music.entity.Track;
import cz.upce.music.repository.ArtistRepository;
import cz.upce.music.repository.TrackOfPlaylistRepository;
import cz.upce.music.repository.TrackRepository;
import cz.upce.music.service.interfaces.FileService;
import cz.upce.music.service.interfaces.TrackService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TrackServiceImpl implements TrackService {

    private final TrackRepository trackRepository;

    private final ModelMapper mapper;

    private final FileService fileService;

    private final TrackOfPlaylistRepository trackOfPlaylistRepository;

    private final ArtistRepository artistRepository;

    public TrackServiceImpl(TrackRepository trackRepository, ModelMapper mapper, FileService fileService, TrackOfPlaylistRepository trackOfPlaylistRepository, ArtistRepository artistRepository) {
        this.trackRepository = trackRepository;
        this.mapper = mapper;
        this.fileService = fileService;
        this.trackOfPlaylistRepository = trackOfPlaylistRepository;
        this.artistRepository = artistRepository;
    }

    @Override
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

    @Override
    public List<TrackDto> create(MultipartFile[] files, Long artistId) throws Exception {

        List<TrackDto> trackDtoList = new ArrayList<>();
        Optional<Artist> optionalArtist = artistRepository.findById(artistId);

        if (optionalArtist.isPresent()) {
            for (MultipartFile file : files) {
                String trackPath = fileService.uploadFile(file, FileType.TRACK);

                Track track = new Track();
                track.setArtist(optionalArtist.get());
                track.setPathToTrack(trackPath);
                track.setName(file.getOriginalFilename());

                Track savedTrack = trackRepository.save(track);
                trackDtoList.add(mapper.map(savedTrack, TrackDto.class));
            }

            return trackDtoList;
        }

        throw new NoSuchElementException("Artist was not found.");
    }

    @Override
    public TrackDto deleteTrackAndFile(Long id) {
        Optional<Track> optionalTrack = trackRepository.findById(id);
        if (optionalTrack.isPresent()) {
            Track trackToDelete = optionalTrack.get();

            trackOfPlaylistRepository.deleteTrackOfPlaylistsByTrack_Id(id);
            trackRepository.deleteById(id);
            fileService.deleteFile(trackToDelete.getPathToTrack(), FileType.TRACK);

            return mapper.map(trackToDelete, TrackDto.class);
        }

        throw new NoSuchElementException("Track was not found.");
    }

    @Override
    public void deleteTrack(Long id) {
        if (trackRepository.findById(id).isPresent()) {
            trackOfPlaylistRepository.deleteTrackOfPlaylistsByTrack_Id(id);
            trackRepository.deleteById(id);
            return;
        }

        throw new NoSuchElementException("Track was not found.");
    }

    @Override
    public void deleteAllTracks() {
        List<Track> tracks = trackRepository.findAll();
        for (Track track: tracks) {
            deleteTrack(track.getId());
        }
    }

    @Override
    public List<Track> findAll() {
        return trackRepository.findAll();
    }
}

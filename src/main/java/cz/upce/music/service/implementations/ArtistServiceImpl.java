package cz.upce.music.service.implementations;

import cz.upce.music.dto.ArtistDto;
import cz.upce.music.dto.TrackDto;
import cz.upce.music.entity.Artist;
import cz.upce.music.entity.Track;
import cz.upce.music.repository.ArtistRepository;
import cz.upce.music.repository.TrackOfPlaylistRepository;
import cz.upce.music.repository.TrackRepository;
import cz.upce.music.service.interfaces.ArtistService;
import cz.upce.music.service.interfaces.FileService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ArtistServiceImpl implements ArtistService {

    private final TrackRepository trackRepository;

    private final TrackOfPlaylistRepository trackOfPlaylistRepository;

    private final ArtistRepository artistRepository;

    private final FileService fileService;

    private final ModelMapper mapper;

    public ArtistServiceImpl(TrackRepository trackRepository, TrackOfPlaylistRepository trackOfPlaylistRepository, ArtistRepository artistRepository, FileService fileService, ModelMapper mapper) {
        this.trackRepository = trackRepository;
        this.trackOfPlaylistRepository = trackOfPlaylistRepository;
        this.artistRepository = artistRepository;
        this.fileService = fileService;
        this.mapper = mapper;
    }

    @Override
    public List<Artist> getAll() {
        return artistRepository.findAll();
    }

    @Override
    public Optional<Artist> findById(Long id) { return artistRepository.findById(id); }

    @Override
    public ArtistDto getArtistDetail(Long id) {
        ArtistDto artistDto;

        Optional<Artist> optionalArtist = artistRepository.findById(id);
        if (optionalArtist.isPresent()) {
            Artist artist = optionalArtist.get();
            List<Track> tracks = trackRepository.findTracksByArtist_Id(id);
            List<TrackDto> trackDtoList = mapper.map(tracks, new TypeToken<List<TrackDto>>() {
            }.getType());

            artistDto = mapper.map(artist, ArtistDto.class);
            artistDto.setTracks(trackDtoList);
            return artistDto;
        }
        else {
            throw new NoSuchElementException("Artist was not found.");
        }
    }

    @Override
    public Artist delete(Long id) {
        Optional<Artist> optionalArtist = artistRepository.findById(id);

        if (optionalArtist.isPresent()) {
            Artist artist = optionalArtist.get();

            List<Track> artistsTracks = trackRepository.findTracksByArtist_Id(artist.getId());
            for (Track track : artistsTracks) {
                trackOfPlaylistRepository.deleteTrackOfPlaylistsByTrack_Id(track.getId());
                fileService.deleteFile(track.getPathToTrack(), FileType.TRACK);
            }

            trackRepository.deleteTracksByArtist_Id(artist.getId());
            artistRepository.deleteById(artist.getId());
            fileService.deleteFile(artist.getPathToImage(), FileType.IMAGE);

            return artist;
        }
        else {
            throw new NoSuchElementException("Artist was not found");
        }
    }

    @Override
    public Artist create(String name, String nationality, MultipartFile image) throws IOException {
        String filePath = fileService.uploadFile(image, FileType.IMAGE);
        Artist artist = new Artist();
        artist.setName(name);
        artist.setNationality(nationality);
        artist.setPathToImage(filePath);
        return artistRepository.save(artist);
    }
}

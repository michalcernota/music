package cz.upce.music.dataFactory;

import cz.upce.music.entity.Artist;
import cz.upce.music.entity.Track;
import cz.upce.music.repository.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

@Component
@Import(ArtistTestDataFactory.class)
public class TrackTestDataFactory {
    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private TrackTestDataFactory trackTestDataFactory;

    @Autowired
    private ArtistTestDataFactory artistTestDataFactory;

    public void saveTrack(String name) {
        Track track = new Track();
        track.setName(name);
        track.setPathToTrack("path");
        saveTrackWithDefaultArtist(track);
    }

    public void saveTrack(Track track) {
        if (track.getName() == null) {
            track.setName("Test track");
        }
        if (track.getPathToTrack() == null) {
            track.setPathToTrack("Path");
        }
        saveTrackWithDefaultArtist(track);
    }

    public void saveTrackWithDefaultArtist(Track track) {
        Artist artist = artistTestDataFactory.saveArtist();
        track.setArtist(artist);
        trackRepository.save(track);
    }
}

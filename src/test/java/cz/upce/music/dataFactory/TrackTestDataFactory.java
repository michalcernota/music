package cz.upce.music.dataFactory;

import cz.upce.music.entity.Artist;
import cz.upce.music.entity.Track;
import cz.upce.music.entity.TrackType;
import cz.upce.music.repository.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Import(TrackTypeTestDataFactory.class)
public class TrackTestDataFactory {
    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private TrackTypeTestDataFactory trackTypeTestDataFactory;

    public void saveTrack(Track track) {
        if (track.getName() == null) track.setName("Test track");
        saveTrackWithDefaultTrackType(track);
    }

    private void saveTrackWithDefaultTrackType(Track track) {
        if (track.getTrackType() == null) {
            TrackType trackType = trackTypeTestDataFactory.saveTrackType();
            track.setTrackType(trackType);
        }
        else {
            trackTypeTestDataFactory.saveTrackType(track.getTrackType());
        }
        trackRepository.save(track);
    }

    public void saveTracks(Set<Track> tracks) {
        for (Track track : tracks) {
            saveTrackWithDefaultTrackType(track);
        }
    }

    public void saveTracks() {
        Track one = new Track();
        one.setName("It was a heat of the moment");
        saveTrackWithDefaultTrackType(one);

        Track two = new Track();
        two.setName("Don´t stop me now");
        saveTrackWithDefaultTrackType(two);

        Track three = new Track();
        three.setName("21 Guns");
        saveTrackWithDefaultTrackType(three);
    }

    public void saveTrack() {
        Track one = new Track();
        one.setName("Never there");
        saveTrackWithDefaultTrackType(one);
    }
}

package cz.upce.music.dataFactory;

import cz.upce.music.entity.TrackEnum;
import cz.upce.music.entity.TrackType;
import cz.upce.music.repository.TrackTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TrackTypeTestDataFactory {
    @Autowired
    private TrackTypeRepository trackTypeRepository;

    public TrackType saveTrackType() {
        TrackType trackType = new TrackType();
        trackType.setTrackType(TrackEnum.ROCK);
        trackTypeRepository.save(trackType);
        return trackType;
    }

    public TrackType saveTrackType(TrackType trackType) {
        if (trackType.getTrackType() == null) trackType.setTrackType(TrackEnum.ROCK);
        trackTypeRepository.save(trackType);
        return trackType;
    }
}

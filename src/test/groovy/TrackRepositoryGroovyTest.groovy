package cz.upce.music;

import cz.upce.music.dataFactory.TrackTestDataFactory
import cz.upce.music.dataFactory.TrackTypeTestDataFactory
import cz.upce.music.entity.Track
import cz.upce.music.entity.TrackEnum
import cz.upce.music.entity.TrackType
import cz.upce.music.repository.TrackRepository
import cz.upce.music.service.TracksQueueService
import cz.upce.music.service.TracksQueueServiceImpl
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import([TrackTestDataFactory.class, TrackTypeTestDataFactory.class, TracksQueueServiceImpl.class])
class TrackRepositoryGroovyTest {
    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private TrackTestDataFactory trackTestDataFactory;

    @Autowired
    private TracksQueueService tracksQueueService;

    @Test
    void saveTrackTest() {
        Track testTrack = new Track();

        trackTestDataFactory.saveTrack(testTrack);
        List<Track> tracks = trackRepository.findAll();
        Assertions.assertThat(tracks.size()).isEqualTo(1);

        Track fromDb = trackRepository.findById(testTrack.getId()).get();
        Assertions.assertThat(fromDb.getName()).isEqualTo("Test track");
    }

    @Test
    void tracksByTypeTest() {
        TrackType trackType1 = new TrackType();
        trackType1.setTrackType(TrackEnum.ROCK);
        TrackType trackType2 = new TrackType();
        trackType2.setTrackType(TrackEnum.POP);

        Set<Track> tracks = new HashSet<>();
        Track one = new Track();
        one.setName("Don't stop me now");
        one.setTrackType(trackType1);
        tracks.add(one);

        Track two = new Track();
        two.setName("It was a heat of the moment");
        two.setTrackType(trackType1);
        tracks.add(two);

        Track three = new Track();
        three.setName("Diamonds");
        three.setTrackType(trackType2);
        tracks.add(three);

        trackTestDataFactory.saveTracks(tracks);

        List<Track> rockTracks = trackRepository.findTrackByTrackTypeIs(trackType1);
        Assertions.assertThat(rockTracks.size()).isEqualTo(2);
    }

    @Test
    void updateTest() {
        trackTestDataFactory.saveTracks();
        List<Track> all = trackRepository.findAll();

        if (!all.isEmpty()) {
            Long id = all.get(0).getId();

            Track byId = trackRepository.findById(id).orElse(new Track());
            long countBeforeUpdate = trackRepository.count();
            byId.setName(byId.getName() + " v2");
            trackRepository.save(byId);
            long countAfterUpdate = trackRepository.count();

            Assertions.assertThat(countBeforeUpdate == countAfterUpdate);
            Assertions.assertThat(trackRepository.findTrackByNameContains(" v2") != null);
        }
        else {
            Assertions.assertThat(true);
        }
    }
}

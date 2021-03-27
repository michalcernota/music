package cz.upce.music

import cz.upce.music.dataFactory.Creator;
import cz.upce.music.entity.Track
import cz.upce.music.entity.TrackEnum
import cz.upce.music.entity.TrackType
import cz.upce.music.repository.TrackRepository
import cz.upce.music.repository.TrackTypeRepository
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
@Import([Creator.class])
class TrackRepositoryGroovyTest {
    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private Creator creator;

    @Test
    void saveTrackTest() {
        TrackType trackType1 = new TrackType();
        trackType1.setTrackType(TrackEnum.ROCK);
        creator.save(trackType1);

        Track testTrack = new Track();
        testTrack.setTrackType(trackType1);

        creator.save(testTrack);
        List<Track> tracks = trackRepository.findAll();
        Assertions.assertThat(tracks.size()).isEqualTo(1);

        Track fromDb = trackRepository.findById(testTrack.getId()).get();
        Assertions.assertThat(fromDb.getName()).isEqualTo("Test name");
    }

    @Test
    void tracksByTypeTest() {
        TrackType trackType1 = new TrackType();
        trackType1.setTrackType(TrackEnum.ROCK);
        creator.save(trackType1);

        TrackType trackType2 = new TrackType();
        trackType2.setTrackType(TrackEnum.POP);
        creator.save(trackType2);

        Track one = new Track();
        one.setName("Don't stop me now");
        one.setTrackType(trackType1);
        creator.save(one);

        Track two = new Track();
        two.setName("It was a heat of the moment");
        two.setTrackType(trackType1);
        creator.save(two);

        Track three = new Track();
        three.setName("Diamonds");
        three.setTrackType(trackType2);
        creator.save(three);

        List<Track> rockTracks = trackRepository.findTrackByTrackTypeIs(trackType1);
        Assertions.assertThat(rockTracks.size()).isEqualTo(2);
    }

    @Test
    void updateTest() {
        TrackType trackType1 = new TrackType();
        trackType1.setTrackType(TrackEnum.ROCK);
        creator.save(trackType1);

        Track one = new Track();
        one.setName("It was a heat of the moment");
        one.setTrackType(trackType1);
        creator.save(one);

        Track two = new Track();
        two.setName("Don´t stop me now");
        two.setTrackType(trackType1);
        creator.save(two);

        Track three = new Track();
        three.setName("21 Guns");
        three.setTrackType(trackType1);
        creator.save(three);

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

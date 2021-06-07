package cz.upce.music


import cz.upce.music.dataFactory.Creator
import cz.upce.music.entity.Track
import cz.upce.music.repository.TrackRepository
import cz.upce.music.service.TrackService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
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
@Import(Creator.class)
class TrackRepositoryGroovyTest {

    @Autowired
    private TrackService trackService;

    @Autowired
    private Creator creator;

    @BeforeEach
    void deleteAllTracks() {
        trackService.deleteAllTracks();
    }

    @Test
    void saveTrackTest() {
        Track track = new Track(name: "Track");
        creator.save(track);
        List<Track> all = trackService.findAll();
        Assertions.assertThat(all.size()).isEqualTo(1);

        Track fromDb = trackService.findTrackById(track.getId()).get();
        Assertions.assertThat(fromDb.getName()).isEqualTo("Track");
        Assertions.assertThat(fromDb.getPathToTrack()).isEqualTo("Test pathToTrack");

        Assertions.assertThat(fromDb.getArtist().name).isEqualTo("Test name");
    }
}

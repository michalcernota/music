package cz.upce.music;

import cz.upce.music.dataFactory.ArtistTestDataFactory;
import cz.upce.music.dataFactory.TrackTestDataFactory;
import cz.upce.music.entity.Track;
import cz.upce.music.repository.TrackRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import({TrackTestDataFactory.class, ArtistTestDataFactory.class})
class TrackRepositoryTest {

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private TrackTestDataFactory trackTestDataFactory;

    @Test
    void saveProductTest() {

        trackTestDataFactory.saveTrack("MyTrack");
        List<Track> all = trackRepository.findAll();
        Assertions.assertThat(all.size()).isEqualTo(1);

    }
}

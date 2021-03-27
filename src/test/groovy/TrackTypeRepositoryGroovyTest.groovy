package cz.upce.music;

import cz.upce.music.dataFactory.TrackTypeTestDataFactory
import cz.upce.music.entity.TrackType
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
@Import(TrackTypeTestDataFactory.class)
class TrackTypeRepositoryGroovyTest {
    @Autowired
    private TrackTypeRepository trackTypeRepository;

    @Autowired
    private TrackTypeTestDataFactory trackTypeTestDataFactory;

    @Test
    void saveTrackTypeTest() {
        TrackType trackType = new TrackType();
        trackTypeTestDataFactory.saveTrackType(trackType);
        List<TrackType> trackTypes = trackTypeRepository.findAll();
        Assertions.assertThat(trackTypes.size()).isEqualTo(1);
    }
}

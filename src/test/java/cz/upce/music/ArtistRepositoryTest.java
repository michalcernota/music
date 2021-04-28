package cz.upce.music;

import java.util.List;

import cz.upce.music.dataFactory.ArtistTestDataFactory;
import cz.upce.music.entity.Artist;
import cz.upce.music.repository.ArtistRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import(ArtistTestDataFactory.class)
class ArtistRepositoryTest {

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private ArtistTestDataFactory artistTestDataFactory;

    @Test
    void saveProductTest() {

        artistTestDataFactory.saveArtist("MyArtist");
        List<Artist> all = artistRepository.findAll();
        Assertions.assertThat(all.size()).isEqualTo(1);

    }
}

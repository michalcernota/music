package cz.upce.music;

import cz.upce.music.dataFactory.ArtistTestDataFactory;
import cz.upce.music.entity.*;
import cz.upce.music.repository.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(ArtistTestDataFactory.class)
class ArtistRepositoryGroovyTest {
    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private ArtistTestDataFactory artistTestDataFactory;

    @Test
    void saveArtistTest() {
        Artist testArtist = new Artist(name: "Kiss");

        artistTestDataFactory.saveArtist(testArtist);
        List<Artist> artists = artistRepository.findAll();
        Assertions.assertThat(artists.size()).isEqualTo(1);

        Artist fromDb = artistRepository.findById(testArtist.getId()).get();
        Assertions.assertThat(fromDb.getName()).isEqualTo("Kiss");
        Assertions.assertThat(fromDb.getNationality()).isEqualTo("CZE");
    }

    @Test
    void artistsSortTest() {
        artistTestDataFactory.saveArtists();

        List<Artist> artistList = artistRepository.findAll(Sort.by(Sort.Direction.DESC, "name"));
        Assertions.assertThat(artistList.get(artistList.size() - 1).getName()).isEqualTo("Green Day");
    }

    @Test
    void findArtistByName() {
        artistTestDataFactory.saveArtists();

        String artistName = "Kiss";
        Artist artist = artistRepository.findArtistByNameIs(artistName);
        Assertions.assertThat(artist.getName()).isEqualTo("Kiss");
    }
}

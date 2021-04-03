package cz.upce.music;

import cz.upce.music.dataFactory.Creator;
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
@Import(Creator.class)
class ArtistRepositoryGroovyTest {
    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private Creator creator;

    @Test
    void saveArtistTest() {
        Artist testArtist = new Artist(name: "Kiss");

        creator.save(testArtist);
        List<Artist> artists = artistRepository.findAll();
        Assertions.assertThat(artists.size()).isEqualTo(1);

        Artist fromDb = artistRepository.findById(testArtist.getId()).get();
        Assertions.assertThat(fromDb.getName()).isEqualTo("Kiss");
        Assertions.assertThat(fromDb.getNationality()).isEqualTo("Test nationality");
    }

    @Test
    void artistsSortTest() {
        Artist artist1 = new Artist();
        artist1.setName("Sum41");
        creator.save(artist1);

        Artist artist2 = new Artist();
        artist2.setName("Kiss");
        creator.save(artist2);

        Artist artist3 = new Artist();
        artist3.setName("Green Day");
        creator.save(artist3);

        List<Artist> artistList = artistRepository.findAll(Sort.by(Sort.Direction.DESC, "name"));
        Assertions.assertThat(artistList.get(artistList.size() - 1).getName()).isEqualTo("Green Day");
    }

    @Test
    void findArtistByName() {
        Artist artist1 = new Artist();
        artist1.setName("Sum41");
        creator.save(artist1);

        Artist artist2 = new Artist();
        artist2.setName("Kiss");
        creator.save(artist2);

        Artist artist3 = new Artist();
        artist3.setName("Green Day");
        creator.save(artist3);

        String artistName = "Kiss";
        Artist artist = artistRepository.findArtistByNameIs(artistName);
        Assertions.assertThat(artist.getName()).isEqualTo("Kiss");
    }
}

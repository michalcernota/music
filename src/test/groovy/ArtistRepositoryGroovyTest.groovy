package cz.upce.music

import cz.upce.music.dataFactory.Creator
import cz.upce.music.entity.Artist
import cz.upce.music.service.ArtistService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import(Creator.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ArtistRepositoryGroovyTest {

    @Autowired
    private ArtistService artistService;

    @Autowired
    private Creator creator;

    @BeforeAll
    void deleteArtists() {
        List<Artist> artists = artistService.findAll();
        for (Artist artist : artists) {
            artistService.delete(artist);
        }
    }

    @Test
    void saveArtistTest() {
        Artist testArtist = new Artist(name: "MyArtist");
        creator.save(testArtist);
        List<Artist> all = artistService.findAll();
        Assertions.assertThat(all.size()).isEqualTo(1);

        Artist fromDb = artistService.findById(testArtist.getId()).get();
        Assertions.assertThat(fromDb.getName()).isEqualTo("MyArtist");
        Assertions.assertThat(fromDb.getPathToImage()).isEqualTo("Test pathToImage");

    }
}

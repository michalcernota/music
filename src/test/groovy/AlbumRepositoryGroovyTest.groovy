package cz.upce.music;


import cz.upce.music.dataFactory.AlbumTestDataFactory
import cz.upce.music.dataFactory.ArtistTestDataFactory
import cz.upce.music.dataFactory.Creator
import cz.upce.music.entity.Album
import cz.upce.music.entity.Artist
import cz.upce.music.repository.AlbumRepository
import cz.upce.music.repository.ArtistRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.data.domain.Sort
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(Creator.class)
class AlbumRepositoryGroovyTest {
    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private Creator creator;

    @Test
    void saveAlbumTest() {
        Album album = new Album();
        album.setName("Meteora");
        creator.save(album);

        List<Artist> albums = albumRepository.findAll();
        Assertions.assertThat(albums.size()).isEqualTo(1);
    }
}

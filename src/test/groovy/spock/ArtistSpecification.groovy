package spock


import cz.upce.music.entity.Artist
import cz.upce.music.repository.ArtistRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import spock.lang.Specification

@SpringBootTest
@DataJpaTest
class ArtistSpecification extends Specification {

    def "should be a simple assertion"() {
        expect:
        1 == 1
    }

    def "should demonstrate given-then-when"() {
        given:
        def artist = new Artist("Artist one")

        when:
        String name = artist.getName();

        then:
        name == "Artist one"
    }

    def "should expect names of artist: #names"() {
        given:
        def artist = new Artist(name: names)

        when:
        String name = artist.getName()

        then:
        name == names

        where:
        names << ["Artist one", "Artist two", "Artist three"]

    }

    def "data table test for calculating max. Max of #a and #b is #max"() {
        expect:
        Math.max(a, b) == max

        where:
        a | b || max
        1 | 3 || 3
        7 | 4 || 7
        0 | 0 || 0
    }

    @Autowired
    private ArtistRepository artistRepository;

    void setup() {
        artistRepository.save(_ as Artist) >> { args ->
            Artist entity = args
            entity.setId(1L)
            return entity
        }
    }

    @Rollback
    def "find artist by name"() {
        given: "save new artist to db"
        Artist artist = new Artist(name: "artist")
        ArtistRepository artistRepository = Mock()

        artistRepository.save(_ as Artist) >> { args ->
            Artist entity = args[0]
            entity.setId(1L)
            return entity
        }

        artistRepository.findArtistByNameIs(_ as String) >> { args ->
            String artistName = args[0]
            Artist entity = new Artist()
            entity.setName(artistName)
            entity.setId(1L)
            return entity
        }

        when: "find artists by name"
        Artist fromDb = artistRepository.findArtistByNameIs("artist")

        then: "check if artist exist"
        Artist saved = artistRepository.save(artist)
        fromDb.getName() == artist.getName()
    }
}

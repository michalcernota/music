package cz.upce.music;

import cz.upce.music.entity.*;
import cz.upce.music.repository.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MusicApplicationTests {

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private TrackTypeRepository trackTypeRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private PlayedRepository playedRepository;

    @Test
    void saveArtistTest() {
        Artist artist = new Artist();
        artist.setName("Kiss");

        long oldCount = artistRepository.count();

        artistRepository.save(artist);

        Assertions.assertThat(artistRepository.count()).isGreaterThan(oldCount);
    }

    @Test
    void artistsSortTest() {
        Artist one = new Artist();
        one.setName("Sum 41");
        Artist two = new Artist();
        two.setName("Kiss");
        Artist three = new Artist();
        three.setName("Green Day");
        artistRepository.save(one);
        artistRepository.save(three);
        artistRepository.save(two);

        List<Artist> artistList = artistRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        Assertions.assertThat(artistList.get(artistList.size() - 1).getName()).isEqualTo("Kiss");
    }

    @Test
    void findArtistByName() {
        Artist one = new Artist();
        one.setName("Sum 41");
        Artist two = new Artist();
        two.setName("Kiss");
        Artist three = new Artist();
        three.setName("Green Day");
        artistRepository.save(one);
        artistRepository.save(two);
        artistRepository.save(three);

        String artistName = "Kiss";
        Artist artist = artistRepository.findArtistByNameIs(artistName);
        Assertions.assertThat(artist.getName()).isEqualTo("Kiss");
    }

    @Test
    void saveArtistAlbumTrackTrackTypeTest() {
        Artist artist = new Artist();
        artist.setName("Kiss");
        artistRepository.save(artist);

        TrackType trackType = new TrackType();
        trackType.setTrackType(TrackEnum.ROCK);
        trackTypeRepository.save(trackType);

        Album album = new Album();
        album.setArtist(artist);
        album.setName("Creatures of the Night");
        albumRepository.save(album);

        Track track = new Track();
        track.setName("I Love it Loud");
        track.setAlbum(album);
        track.setArtist(artist);
        track.setTrackType(trackType);
        trackRepository.save(track);

        Played played = new Played();
        played.setTimestamp(LocalDateTime.now());
        played.setTrack(track);
        playedRepository.save(played);

        List<Track> tracks = trackRepository.findAll();

        Assertions.assertThat(artistRepository.count()).isGreaterThan(0);
        Assertions.assertThat(albumRepository.count()).isGreaterThan(0);
        Assertions.assertThat(trackTypeRepository.count()).isGreaterThan(0);
        Assertions.assertThat(playedRepository.count()).isGreaterThan(0);
        Assertions.assertThat(trackTypeRepository.count()).isGreaterThan(0);
    }

    @Test
    void tracksByIdTest() {
        TrackType trackType1 = new TrackType();
        trackType1.setTrackType(TrackEnum.ROCK);
        TrackType trackType2 = new TrackType();
        trackType2.setTrackType(TrackEnum.POP);
        trackTypeRepository.save(trackType1);
        trackTypeRepository.save(trackType2);

        Track one = new Track();
        one.setName("Don't stop me now");
        one.setTrackType(trackType1);
        trackRepository.save(one);

        Track two = new Track();
        two.setName("It was a heat of the moment");
        two.setTrackType(trackType1);
        trackRepository.save(two);

        Track three = new Track();
        three.setName("Diamonds");
        three.setTrackType(trackType2);
        trackRepository.save(three);

        List<Track> rockTracks = trackRepository.findTrackByTrackTypeIs(trackType1);
        Assertions.assertThat(rockTracks.size()).isEqualTo(2);
    }

    @Test
    void updateTest() {
        List<Track> all = trackRepository.findAll();

        if (!all.isEmpty()) {
            Long id = all.get(0).getId();

            Track byId = trackRepository.findById(id).orElse(new Track());
            long countBeforeUpdate = trackRepository.count();
            byId.setName(byId.getName() + " v2");
            trackRepository.save(byId);
            long countAfterUpdate = trackRepository.count();

            Assertions.assertThat(countBeforeUpdate == countAfterUpdate);
        }
        else {
            Assertions.assertThat(true);
        }
    }
}

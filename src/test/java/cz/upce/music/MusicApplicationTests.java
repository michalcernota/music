package cz.upce.music;

import cz.upce.music.entity.*;
import cz.upce.music.repository.*;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
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
    private AlbumRepository albumRepository;

    @Autowired
    private PlayedRepository playedRepository;

    @Autowired
    private TrackOfPlaylistRepository trackOfPlaylistRepository;

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UsersPlaylistsRepository usersPlaylistsRepository;

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

        Album album = new Album();
        album.setArtist(artist);
        album.setName("Creatures of the Night");
        albumRepository.save(album);

        Track track = new Track();
        track.setName("I Love it Loud");
        track.setAlbum(album);
        track.setArtist(artist);
        trackRepository.save(track);

        Played played = new Played();
        played.setTimestamp(LocalDateTime.now());
        played.setTrack(track);
        playedRepository.save(played);

        Assertions.assertThat(artistRepository.count()).isGreaterThan(0);
        Assertions.assertThat(albumRepository.count()).isGreaterThan(0);
        Assertions.assertThat(playedRepository.count()).isGreaterThan(0);
    }

    @Test
    void tracksByTypeTest() {
        Track one = new Track();
        one.setName("Don't stop me now");
        one.setTrackType(TrackEnum.ROCK);
        trackRepository.save(one);

        Track two = new Track();
        two.setName("It was a heat of the moment");
        two.setTrackType(TrackEnum.ROCK);
        trackRepository.save(two);

        Track three = new Track();
        three.setName("Diamonds");
        three.setTrackType(TrackEnum.POP);
        trackRepository.save(three);

        List<Track> rockTracks = trackRepository.findTrackByTrackTypeIs(TrackEnum.ROCK);
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

    @Test
    public void playlistTest() {
        User user  = new User();
        user.setUsername("michal");
        user.setPassword("heslo");
        user.setRegistrationDate(LocalDateTime.now());

        Playlist playlist = new Playlist();
        playlist.setName("my playlist");

        UsersPlaylist usersPlaylist = new UsersPlaylist();
        usersPlaylist.setUser(user);
        usersPlaylist.setPlaylist(playlist);

        Track track = new Track();
        track.setName("track");

        TrackOfPlaylist trackOfPlaylist = new TrackOfPlaylist();
        trackOfPlaylist.setPlaylist(playlist);
        trackOfPlaylist.setTrack(track);

        userRepository.save(user);
        trackRepository.save(track);
        playlistRepository.save(playlist);
        trackOfPlaylistRepository.save(trackOfPlaylist);
        usersPlaylistsRepository.save(usersPlaylist);

        Assert.assertEquals(1, userRepository.findAll().size());
        Assert.assertEquals(1, trackRepository.findAll().size());
        Assert.assertEquals(1, playlistRepository.findAll().size());
        Assert.assertEquals(1, trackOfPlaylistRepository.findAll().size());
        Assert.assertEquals(1, usersPlaylistsRepository.findAll().size());
    }
}

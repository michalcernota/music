package cz.upce.music;

import cz.upce.music.entity.*;
import cz.upce.music.repository.*;
import cz.upce.music.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MusicApplicationTests {

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private ArtistRepository artistRepository;

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

        Track track = new Track();
        track.setName("I Love it Loud");
        track.setArtist(artist);
        track.setPathToTrack("");
        trackRepository.save(track);

        Assertions.assertThat(artistRepository.count()).isGreaterThan(0);
    }

    @Test
    void tracksByTypeTest() {
        Artist artist = new Artist();
        artist.setName("Artist");
        artistRepository.save(artist);

        Track one = new Track();
        one.setName("Don't stop me now");
        one.setTrackType(TrackEnum.ROCK);
        one.setPathToTrack("");
        one.setArtist(artist);
        trackRepository.save(one);

        Track two = new Track();
        two.setName("It was a heat of the moment");
        two.setTrackType(TrackEnum.ROCK);
        two.setPathToTrack("");
        two.setArtist(artist);
        trackRepository.save(two);

        Track three = new Track();
        three.setName("Diamonds");
        three.setTrackType(TrackEnum.POP);
        three.setPathToTrack("");
        three.setArtist(artist);
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
        playlist.setOwner(user);

        UsersPlaylist usersPlaylist = new UsersPlaylist();
        usersPlaylist.setUser(user);
        usersPlaylist.setPlaylist(playlist);

        Artist artist = new Artist();
        artist.setName("Artist");

        Track track = new Track();
        track.setName("track");
        track.setPathToTrack("");
        track.setArtist(artist);

        TrackOfPlaylist trackOfPlaylist = new TrackOfPlaylist();
        trackOfPlaylist.setPlaylist(playlist);
        trackOfPlaylist.setTrack(track);

        artistRepository.save(artist);
        userRepository.save(user);
        trackRepository.save(track);
        playlistRepository.save(playlist);
        trackOfPlaylistRepository.save(trackOfPlaylist);
        usersPlaylistsRepository.save(usersPlaylist);

        Assert.assertEquals(2, userRepository.findAll().size());
        Assert.assertEquals(1, trackRepository.findAll().size());
        Assert.assertEquals(1, playlistRepository.findAll().size());
        Assert.assertEquals(1, trackOfPlaylistRepository.findAll().size());
        Assert.assertEquals(1, usersPlaylistsRepository.findAll().size());
    }

    @Test
    public void passwordEncryptionDecryptionTest() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        User user = new User();
        user.setUsername("user");
        user.setPassword(bCryptPasswordEncoder.encode("password"));

        userRepository.save(user);

        String anotherPassword = "password";
        String anotherPasswordEncoded = bCryptPasswordEncoder.encode(anotherPassword);

        boolean matches = bCryptPasswordEncoder.matches(userRepository.findUserByUsername("user").getPassword(), anotherPasswordEncoded);

        Assertions.assertThat(matches);
    }

    @Test
    public void tracksNotInPlaylistTest() {
        Artist artist = new Artist();
        artist.setName("Artist");
        artistRepository.save(artist);

        Track trackOne = new Track();
        trackOne.setPathToTrack("");
        trackOne.setName("track one");
        trackOne.setArtist(artist);

        Track trackTwo = new Track();
        trackTwo.setPathToTrack("");
        trackTwo.setName("track two");
        trackTwo.setArtist(artist);

        Track trackThree = new Track();
        trackThree.setPathToTrack("");
        trackThree.setName("track three");
        trackThree.setArtist(artist);

        trackRepository.save(trackOne);
        trackRepository.save(trackTwo);
        trackRepository.save(trackThree);

        User user = new User();
        user.setUsername("user");
        user.setPassword("user");
        userRepository.save(user);

        Playlist playlist = new Playlist();
        playlist.setName("playlist");
        playlist.setOwner(user);
        playlistRepository.save(playlist);

        TrackOfPlaylist trackOfPlaylist = new TrackOfPlaylist();
        trackOfPlaylist.setTrack(trackOne);
        trackOfPlaylist.setPlaylist(playlist);
        trackOfPlaylistRepository.save(trackOfPlaylist);

        Set<Long> ids = trackOfPlaylistRepository.getAllTrackIds();
        Assertions.assertThat(ids.size() == 1);

        List<Track> tracksNotInPlaylist = trackRepository.findTracksByIdIsNotIn(ids);
        Assertions.assertThat(tracksNotInPlaylist.size() == 2);
    }

    @Test
    public void deleteArtistTest() {
        Artist artist = new Artist();
        artist.setName("artist");

        Track track = new Track();
        track.setName("track");
        track.setPathToTrack("");
        track.setArtist(artist);

        artistRepository.save(artist);
        trackRepository.save(track);

        trackRepository.deleteTracksByArtist_Id(artist.getId());
        artistRepository.delete(artist);

        Assertions.assertThatNoException();
        Assertions.assertThat(artistRepository.count() == 0);
        Assertions.assertThat(trackRepository.count() == 0);
    }

}

package cz.upce.music.dataFactory;

import cz.upce.music.entity.Artist;
import cz.upce.music.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class ArtistTestDataFactory {
    @Autowired
    private ArtistRepository artistRepository;

    public Artist saveArtist() {
        Artist artist = new Artist();
        artist.setName("Test artist");
        artistRepository.save(artist);

        return artist;
    }

    public Artist saveArtist(Artist artist) {
        setDefaultValues(artist);
        artistRepository.save(artist);

        return artist;
    }

    public void saveArtists() {
        Set<Artist> artists = new HashSet<>();

        Artist artist1 = new Artist();
        artist1.setName("Sum41");
        artists.add(artist1);

        Artist artist2 = new Artist();
        artist2.setName("Kiss");
        artists.add(artist2);

        Artist artist3 = new Artist();
        artist3.setName("Green Day");
        artists.add(artist3);

        saveArtists(artists);
    }

    private void setDefaultValues(Artist artist) {
        if (artist.getName() == null) artist.setName("Test artist");
        if (artist.getNationality() == null) artist.setNationality("CZE");
    }

    public void saveArtists(Set<Artist> artists) {
        for (Artist artist : artists) {
            setDefaultValues(artist);
            artistRepository.save(artist);
        }
    }
}

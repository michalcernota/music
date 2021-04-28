package cz.upce.music.dataFactory;

import cz.upce.music.entity.Artist;
import cz.upce.music.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ArtistTestDataFactory {
    @Autowired
    private ArtistRepository artistRepository;

    public Artist saveArtist() {
        Artist artist = new Artist();
        artist.setName("Artist");
        artistRepository.save(artist);
        return artist;
    }

    public Artist saveArtist(String name) {
        Artist artist = new Artist();
        artist.setName(name);
        artistRepository.save(artist);
        return artist;
    }

    public Artist saveArtist(Artist artist) {
        if (artist.getName() == null) {
            artist.setName("Test artist");
        }
        if (artist.getPathToImage() == null) {
            artist.setPathToImage("Path");
        }
        artistRepository.save(artist);
        return artist;
    }
}

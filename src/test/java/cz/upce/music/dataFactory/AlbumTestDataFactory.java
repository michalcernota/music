package cz.upce.music.dataFactory;

import cz.upce.music.entity.Album;
import cz.upce.music.entity.Artist;
import cz.upce.music.entity.Track;
import cz.upce.music.repository.AlbumRepository;
import cz.upce.music.repository.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

@Component
@Import(ArtistTestDataFactory.class)
public class AlbumTestDataFactory {
    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private ArtistTestDataFactory artistTestDataFactory;

    public void saveAlbum() {
        Album album = new Album();
        album.setName("Test album");
        saveAlbumWithDefaultArtist(album);
    }

    public void saveAlbum(Album album) {
        if (album.getName() == null) album.setName("Test album");
        saveAlbumWithDefaultArtist(album);
    }

    private void saveAlbumWithDefaultArtist(Album album) {
        Artist artist = artistTestDataFactory.saveArtist();

        album.setArtist(artist);
        albumRepository.save(album);
    }
}

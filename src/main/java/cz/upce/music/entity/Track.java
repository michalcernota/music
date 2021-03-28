package cz.upce.music.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Track {
    // TODO: udělat sloupec data not null (až přijdu na to jak ukládat data)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Artist artist;

    @ManyToOne(fetch = FetchType.LAZY)
    private Album album;

    @ManyToOne(fetch = FetchType.LAZY)
    private TrackType trackType;

    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] data;

    @OneToMany(mappedBy = "id", fetch = FetchType.LAZY)
    private Set<Played> played;

    @OneToMany(mappedBy = "id", fetch = FetchType.LAZY)
    private Set<TrackOfPlaylist> trackOfPlaylists;

    @Column(length = 50)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public TrackType getTrackType() {
        return trackType;
    }

    public void setTrackType(TrackType trackType) {
        this.trackType = trackType;
    }

    public Set<Played> getPlayed() {
        return played;
    }

    public void setPlayed(Set<Played> played) {
        this.played = played;
    }

    public Set<TrackOfPlaylist> getTrackOfPlaylists() {
        return trackOfPlaylists;
    }

    public void setTrackOfPlaylists(Set<TrackOfPlaylist> trackOfPlaylists) {
        this.trackOfPlaylists = trackOfPlaylists;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}

package cz.upce.music.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Track {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Artist artist;

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private TrackEnum trackType;

    @Column(nullable = false, length = 512)
    private String pathToTrack;

    @OneToMany(mappedBy = "id", fetch = FetchType.LAZY)
    private Set<TrackOfPlaylist> trackOfPlaylists;

    @Column(nullable = false, length = 512)
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

    public TrackEnum getTrackType() {
        return trackType;
    }

    public void setTrackType(TrackEnum trackType) {
        this.trackType = trackType;
    }

    public Set<TrackOfPlaylist> getTrackOfPlaylists() {
        return trackOfPlaylists;
    }

    public void setTrackOfPlaylists(Set<TrackOfPlaylist> trackOfPlaylists) {
        this.trackOfPlaylists = trackOfPlaylists;
    }

    public String getPathToTrack() {
        return pathToTrack;
    }

    public void setPathToTrack(String pathToTrack) {
        this.pathToTrack = pathToTrack;
    }
}

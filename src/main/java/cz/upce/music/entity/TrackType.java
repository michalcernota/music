package cz.upce.music.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
public class TrackType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "id", fetch = FetchType.LAZY)
    private Set<Track> tracks;

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private TrackEnum trackType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TrackEnum getTrackType() {
        return trackType;
    }

    public void setTrackType(TrackEnum trackType) {
        this.trackType = trackType;
    }

    public Set<Track> getTracks() {
        return tracks;
    }

    public void setTracks(Set<Track> tracks) {
        this.tracks = tracks;
    }
}

package cz.upce.music.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Played {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Track track;

    @Column
    private LocalDateTime timestamp;

    @Column
    private boolean wholeTrackPlayed;

    @Column
    private String username;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isWholeTrackPlayed() {
        return wholeTrackPlayed;
    }

    public void setWholeTrackPlayed(boolean wholeTrackPlayed) {
        this.wholeTrackPlayed = wholeTrackPlayed;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

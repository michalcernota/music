package cz.upce.music.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String name;

    @OneToMany(mappedBy = "id", fetch = FetchType.LAZY)
    private Set<UsersPlaylist> usersPlaylists;

    @OneToMany(mappedBy = "id", fetch = FetchType.LAZY)
    private Set<TrackOfPlaylist> trackOfPlaylist;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<UsersPlaylist> getUsersPlaylists() {
        return usersPlaylists;
    }

    public void setUsersPlaylists(Set<UsersPlaylist> usersPlaylists) {
        this.usersPlaylists = usersPlaylists;
    }

    public Set<TrackOfPlaylist> getTrackOfPlaylist() {
        return trackOfPlaylist;
    }

    public void setTrackOfPlaylist(Set<TrackOfPlaylist> trackOfPlaylist) {
        this.trackOfPlaylist = trackOfPlaylist;
    }
}

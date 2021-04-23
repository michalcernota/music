package cz.upce.music.entity;

import javax.persistence.*;

@Entity
public class UsersPlaylist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Playlist playlist;

    @Column(nullable = false)
    private boolean isOwner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User users) {
        this.user = users;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public boolean isIsOwner() {
        return isOwner;
    }

    public void setIsOwner(boolean owner) {
        this.isOwner = owner;
    }
}

package cz.upce.music.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(length = 30)
    private String emailAddress;

    @Column
    private LocalDateTime registrationDate;

    @OneToMany(mappedBy = "id", fetch = FetchType.LAZY)
    private Set<UsersPlaylist> usersPlaylists;

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private UserRoleEnum userRole = UserRoleEnum.User;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private Set<Playlist> playlistsOwnedByUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Set<UsersPlaylist> getUsersPlaylists() {
        return usersPlaylists;
    }

    public void setUsersPlaylists(Set<UsersPlaylist> usersPlaylists) {
        this.usersPlaylists = usersPlaylists;
    }

    public UserRoleEnum getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRoleEnum userRole) {
        this.userRole = userRole;
    }

    public Set<Playlist> getPlaylistsOwnedByUser() {
        return playlistsOwnedByUser;
    }

    public void setPlaylistsOwnedByUser(Set<Playlist> playlistsOwnedByUser) {
        this.playlistsOwnedByUser = playlistsOwnedByUser;
    }
}

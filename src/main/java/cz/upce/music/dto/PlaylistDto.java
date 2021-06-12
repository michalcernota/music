package cz.upce.music.dto;

import java.util.List;

public class PlaylistDto {

    private Long id;

    private String name;

    private String ownerName;

    private List<TrackOfPlaylistDto> tracksOfPlaylist;

    private int tracksCount;

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

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public List<TrackOfPlaylistDto> getTracksOfPlaylist() {
        return tracksOfPlaylist;
    }

    public void setTracksOfPlaylist(List<TrackOfPlaylistDto> tracksOfPlaylist) {
        this.tracksOfPlaylist = tracksOfPlaylist;
    }

    public int getTracksCount() {
        return tracksCount;
    }

    public void setTracksCount(int tracksCount) {
        this.tracksCount = tracksCount;
    }
}
